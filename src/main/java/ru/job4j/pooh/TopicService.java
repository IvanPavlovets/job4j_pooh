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
        String key = req.getSourceName() != null ? req.getSourceName() : "";
        String text = req.getParam() != null ? req.getParam() : "";
        String method = req.getHttpRequestType();

        switch (method) {
            case "POST":
                for (var map : topics.values()) {
                    ConcurrentLinkedQueue<String> queue = map.get(key);
                    if (queue != null) {
                        queue.add(text);
                    }
                }
                return new Resp(text, "200");
            case "GET":
                if (topics.get(text) == null) {
                    topics.putIfAbsent(text, new ConcurrentHashMap<>());
                    topics.get(text).putIfAbsent(key, new ConcurrentLinkedQueue<>());
                }
                if (topics.get(text).get(key) == null) {
                    topics.get(text).putIfAbsent(key, new ConcurrentLinkedQueue<>());
                } else {
                    ConcurrentLinkedQueue<String> inerQueue = topics.get(text).get(key);
                    text = inerQueue.poll();
                }
                return new Resp(text, "200");
            default:
                return new Resp(text, "501");
        }
    }
}
