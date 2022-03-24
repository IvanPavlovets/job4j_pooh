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
     * имя - queue или topic.
     */
    private final String sourceName;
    /**
     * содержимое запроса.
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
     * @return Req
     * @param content
     */
    public static Req of(String content) {
        return new Req(null, null, null, null);
    }
}
