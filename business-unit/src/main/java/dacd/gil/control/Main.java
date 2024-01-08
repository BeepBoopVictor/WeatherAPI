package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import dacd.gil.view.*;

public class Main {
    public static void main(String[] args) throws CustomException, SQLException {
        dataMartSQL dataStore = new dataMartSQL(args[0]);
        sparkInitializer sparkInitializer = new sparkInitializer(args[0]);
        try {
            Map<String, Manager> mapManager = new HashMap<>();
            mapManager.put(args[1], new weatherManager(dataStore));
            mapManager.put(args[2], new hotelManager(dataStore));
            TopicReceiver topicReceiver = new TopicReceiver(mapManager);
            readTextDatalake readTextDatalake = new readTextDatalake(mapManager);
            readTextDatalake.readEvents(args[4], "prediction.Weather");
            readTextDatalake.readEvents(args[3], "prediction.Hotel");

            sparkInitializer.init();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                try {
                    topicReceiver.start(dataStore);
                } catch (CustomException e) {
                    e.printStackTrace();
                }
            });

            executorService.shutdown();
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    dataStore.dropTables();
                } catch (CustomException e) {
                    e.printStackTrace();
                }
            }));
        }
    }
}
