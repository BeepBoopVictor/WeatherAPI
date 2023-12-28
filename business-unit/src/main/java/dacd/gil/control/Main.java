package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws CustomException {
        // Los topicNames como argumentos de programa
        dataMartSQL dataStore = new dataMartSQL(args[0]);
        Map<String, Manager> mapManager = new HashMap<>();
        mapManager.put(args[1], new weatherManager(dataStore));
        mapManager.put(args[2], new hotelManager(dataStore));
        TopicReceiver topicReceiver = new TopicReceiver(mapManager);

        //topicReceiverHotel.readMessages(controller, args[0], 5, "prediction.Hotel");
        //topicReceiverHotel.readMessages(controller, args[1], 40, "prediction.Weather");

        topicReceiver.start(dataStore);
    }
}
