package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", null, "temperature=18")
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", null, null)
        );
        assertThat(result.text(), is("temperature=18"));
        assertThat(result.status(), is("200"));
    }

    @Test
    public void whenPostThenGetQueueToo() {
        QueueService queueService = new QueueService();
        String weatherSourceName = "weather";
        String trafficSourceName = "traffic";
        String paramForWeatherSourceName = "temperature=18";
        String paramForTrafficSourceName = "cars=20188";

        queueService.process(
                new Req("POST", "queue", weatherSourceName, paramForWeatherSourceName)
        );
        queueService.process(
                new Req("POST", "queue", trafficSourceName, paramForTrafficSourceName)
        );
        Resp resultFromWeatherSource1 = queueService.process(
                new Req("GET", "queue", weatherSourceName, null)
        );
        Resp resultFromWeatherSource2 = queueService.process(
                new Req("GET", "queue", weatherSourceName, null)
        );
        Resp resultFromTrafficSource1 = queueService.process(
                new Req("GET", "queue", trafficSourceName, null)
        );
        Resp resultFromTrafficSource2 = queueService.process(
                new Req("GET", "queue", trafficSourceName, null)
        );
        queueService.process(
                new Req("POST", "queue", weatherSourceName, paramForWeatherSourceName)
        );
        Resp resultFromWeatherSource3 = queueService.process(
                new Req("GET", "queue", weatherSourceName, null)
        );


        Resp resultFromWeatherSource4 = queueService.process(
                new Req("HEAD", "queue", weatherSourceName, null)
        );
        assertThat(resultFromWeatherSource1.text(), is("temperature=18"));
        assertThat(resultFromWeatherSource2.text(), is(""));
        assertThat(resultFromTrafficSource1.text(), is("cars=20188"));
        assertThat(resultFromTrafficSource2.text(), is(""));
        assertThat(resultFromWeatherSource3.text(), is("temperature=18"));

        assertThat(resultFromWeatherSource4.text(), is(""));
        assertThat(resultFromWeatherSource4.status(), is("501"));
    }
}
