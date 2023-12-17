package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

public class Main {
    public static void main(String[] args) throws CustomException {
        TopicReceiver topicReceiverHotel = new TopicReceiver("Víctor", "0909");
        weatherStore weatherStore = new weatherStore(args[0]);
        topicReceiverHotel.start(weatherStore);
    }
}
