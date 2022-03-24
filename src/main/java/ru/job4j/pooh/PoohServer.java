package ru.job4j.pooh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Класс сервера - считывает данные из запроса и отправляет обратно
 * 1) new ServerSocket(9000) - создание серверного сокета,
 * привязаного к порту 9000 (По умолчанию localhost).
 * 2) while (!server.isClosed()) - сервер работает пока его
 * принудительно не закроют.
 * 3) server.accept() - застовляет ждать подключения по указаному порту,
 * работа программы продолжитьс после подключения клиента.
 * После успешного подключения метод возвращает объект Socket,
 * который используется для взаимодействия с клиентом.
 * 4) С помощью объекта Socket программа может получить входной поток
 * и может отправить данные в выходной поток out = socket.getOutputStream()
 * input = socket.getInputStream().
 * 5) В ответ записываем строку out.write("HTTP/1.1 200 OK\r\n".getBytes())
 * 6) Чтение выходного потока input.read(buff). Чтение в buff.
 * Сначала запустить класс сервер и с клиента (curl, браузер) отправить сообщение:
 * "curl -i http://localhost:9000/?msg=Hello" - GET
 * "curl -X POST -d "text=13" http://localhost:9000/queue/weather" - POST
 */
public class PoohServer {
    public static void main(String[] args) throws IOException {
        try(ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                try (OutputStream out = socket.getOutputStream();
                     InputStream input = socket.getInputStream()) {
                    byte[] buff = new byte[1_000_000];
                    var total = input.read(buff);
                    var text = new String(Arrays.copyOfRange(buff, 0, total), StandardCharsets.UTF_8);
                    System.out.println(text);
                    out.write("HTTP/1.1 200 OK\r\n".getBytes());
                    out.write(text.getBytes());
                }
            }
        }
    }
}
