package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Реализация работы режима queue.
 * Все потребители получают данные из одной и той же очереди.
 * status = "204" - нет данных.
 * 1) Если POST - отправитель получает запрос на добавление данных,
 * в очередь (с указанием имени очереди (ключ) и текстового
 * сообщения(значение). Если очереди нет в сервисе, то создать
 * новую и поместить в нее переданое сообщение.
 * 2) Если GET - отправитель посылает запрос на получение данных
 * с указанием очереди. Сообщение забирается из начала очереди
 * и удаляется.
 *
 */
public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        String text = "";
        String status = "400";
        if ("POST".equals(req.getHttpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).add(req.getParam());
            text = "Post added";
            status = "200";
        }
        if ("GET".equals(req.getHttpRequestType())) {
            if (!queue.isEmpty()) {
                text =  queue.get(req.getSourceName()).poll();
                if (text == null) {
                    text = "";
                }
            }
        }
        return new Resp(text, status);
    }
}
