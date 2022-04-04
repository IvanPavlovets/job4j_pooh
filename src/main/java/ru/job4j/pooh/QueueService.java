package ru.job4j.pooh;

import java.util.Optional;
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
 */
public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String key = req.getSourceName() != null ? req.getSourceName() : "";
        String text = req.getParam() != null ? req.getParam() : "";
        String method = req.getHttpRequestType();

        switch (method) {
            case "POST":
                queue.putIfAbsent(key, new ConcurrentLinkedQueue<>());
                queue.get(key).add(text);
                return new Resp(text, "200");
            case "GET":
                if (!queue.isEmpty() && !queue.get(key).isEmpty() ) {
                    ConcurrentLinkedQueue<String> inerQueue = queue.get(key);
                    text = inerQueue.poll();
                    return new Resp(text, "200");
                } else {
                    return new Resp("", "204");
                }
            default:
                return new Resp(text, "501");
        }
    }
}
