package ru.job4j.pooh;

/**
 * класс, служит для парсинга входящего запроса.
 */
public class Req {
    /**
     * Тип запроса - GET или POST.
     */
    private final String httpRequestType;
    /**
     * Режим работы - queue или topic.
     */
    private final String poohMode;
    /**
     * имя очереди или топика.
     */
    private final String sourceName;
    /**
     * текстовое содержимое запроса.
     */
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public String getHttpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }

    /**
     * Парсит входящую строку запроса.
     * Пример разбора первой строки запроса POST,
     * 1) POST /queue/weather HTTP/1.1 - последовательно идут:
     * POST - тип, /queue - режим, /weather - имя очереди
     * 2) содержимое запроса - текстовое сообщение запроса:
     * в режиме queue и topic, метод POST - предпоследняя строка запроса.
     * в режиме queue, метод GET - нет текстового сообщения.
     * в режиме topic, метод GET - стоит третим в массиве параметров запроса,
     * после poohMode и sourceName (GET /topic/weather/client407 HTTP/1.1)
     * может быть ()
     * @return Req
     * @param content
     */
    public static Req of(String content) {
        String[] parts = content.split(System.lineSeparator());
        String[] firstLine = parts[0].split(" ");
        String[] modeNameParam = firstLine[1].split("/");
        String text = "";
        if ("POST".equals(firstLine[0])) {
            text = parts[parts.length - 1];
        }
        if ("GET".equals(firstLine[0]) && "topic".equals(modeNameParam[1])) {
            text = modeNameParam[3];
        }
        return new Req(firstLine[0], modeNameParam[1], modeNameParam[2], text);
    }
}
