package dacd.gil.control;

import dacd.gil.control.exception.StoreException;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) throws StoreException {
        FileEventStoreBuilder fileEventStoreBuilder = new FileEventStoreBuilder(args[0]);
        AMQTopicSubscriber amqSubscriber = new AMQTopicSubscriber(fileEventStoreBuilder);

        amqSubscriber.start();
    }
}
