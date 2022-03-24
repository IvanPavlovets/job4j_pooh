package ru.job4j.pooh;

/**
 * Класс - ответ от сервиса.
 */
public class Resp {
    /**
     * текст ответа
     */
    private final String text;
    /**
     * HTTP response status codes.
     * Запрос прошел, то статус = 200,
     *  а нет данных, то статус = 204.
     */
    private final String status;

    public Resp(String text, String status) {
        this.text = text;
        this.status = status;
    }

    public String text() {
        return text;
    }

    public String status() {
        return status;
    }
}
