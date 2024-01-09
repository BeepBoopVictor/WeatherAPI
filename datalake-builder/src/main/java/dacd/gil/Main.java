package dacd.gil;

import dacd.gil.control.AMQTopicSubscriber;
import dacd.gil.control.FileEventStoreBuilder;
import dacd.gil.control.exception.StoreException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        FileEventStoreBuilder fileEventStoreBuilder = new FileEventStoreBuilder(args[0]);
        AMQTopicSubscriber amqSubscriber = new AMQTopicSubscriber(fileEventStoreBuilder);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                amqSubscriber.start();
            } catch (StoreException e) {
                e.printStackTrace();
            }
        });
    }
}
