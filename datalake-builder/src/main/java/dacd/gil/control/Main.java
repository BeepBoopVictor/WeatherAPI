package dacd.gil.control;

import dacd.gil.control.exception.StoreException;

public class Main {
    public static void main(String[] args) throws StoreException {
        FileEventStoreBuilder fileEventStoreBuilder = new FileEventStoreBuilder(args[0]);
        AMQTopicSubscriber amqSubscriber = new AMQTopicSubscriber(fileEventStoreBuilder);

        amqSubscriber.start();
    }
}
