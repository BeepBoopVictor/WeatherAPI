package dacd.gil.control;

import dacd.gil.control.exception.StoreException;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) throws StoreException {
        AMQTopicSubscriber amqTopicSubscriber = new AMQTopicSubscriber("prediction.Weather", "Victor", "123");
        FileEventStoreBuilder fileEventStoreBuilder = new FileEventStoreBuilder(args[0]);

        amqTopicSubscriber.start(fileEventStoreBuilder);
    }
}
