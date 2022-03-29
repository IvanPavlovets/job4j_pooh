package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Реализация работы режима topic.
 * В этом режиме для каждого потребителя своя уникальная очередь
 * с данными. status = "204" - нет данных.
 * 1) Если POST - отправитель посылает запрос на добавление данных
 * Добавление текстовых данных (temperature=18) происходит в каждую
 * очередь (queue) из внутреней карты (map) которая находиться во
 * внешней карте topics.
 * POST /topic/weather -d "temperature=18".
 * Сообщение помещается в конец каждой индивидуальной очереди получателей.
 * Если топика нет в сервисе, то данные игнорируются.
 * 2) Если GET - получатель посылает запрос на получение данных
 * с указанием топика. Если топик отсутствует, то создается новый.
 * А если топик присутствует, то сообщение забирается из начала
 * индивидуальной очереди получателя и удаляется.
 * GET /topic/weather/1
 */
public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String,
            ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        String status = "400";
        String text = "";
        if ("POST".equals(req.getHttpRequestType())) {
            for (var map : topics.values()) {
                ConcurrentLinkedQueue<String> queue = map.get(req.getSourceName());
                if (queue != null) {
                    queue.add(req.getParam());
                }
            }
            status = "200";
            text = "Posts are added";
        }
        if ("GET".equals(req.getHttpRequestType())) {
            if (topics.get(req.getParam()) == null) {
                topics.putIfAbsent(req.getParam(), new ConcurrentHashMap<>());
                topics.get(req.getParam()).putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            }
            if (topics.get(req.getParam()).get(req.getSourceName()) == null) {
                topics.get(req.getParam()).putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            } else {
                text = topics.get(req.getParam()).get(req.getSourceName()).poll();
                if (text == null) {
                    text = "";
                }
            }
            status = "200";
        }
        return new Resp(text, status);
    }
}
