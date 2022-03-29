package ru.job4j.pooh;

/**
 * Интерфейс режимов работы приложения queue или topic.
 */
public interface Service {
    /**
     * Метод принимает запрос, обрабатывает его, сохроняет в очередь
     * и отсылает ответ.
     * @param req
     * @return Resp
     */
    Resp process(Req req);
}
