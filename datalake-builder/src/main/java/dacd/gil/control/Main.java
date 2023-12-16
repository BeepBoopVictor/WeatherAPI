package dacd.gil.control;

import dacd.gil.control.exception.StoreException;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) throws StoreException {
        AMQTopicSubscriber amqWeatherSubscriber = new AMQTopicSubscriber("prediction.Weather", "Victor", "123");
        AMQTopicSubscriber amqHotelSubscriber = new AMQTopicSubscriber("prediction.Hotel", "Victor", "321");
        FileEventStoreBuilder fileEventStoreBuilder = new FileEventStoreBuilder(args[0]);

        amqWeatherSubscriber.start(fileEventStoreBuilder);
        amqHotelSubscriber.start(fileEventStoreBuilder);
    }
}
