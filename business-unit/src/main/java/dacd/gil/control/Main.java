package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

public class Main {
    public static void main(String[] args) throws CustomException {
        FinalListClass finalListClass = new FinalListClass();
        Controller controller = new Controller(finalListClass);
        TopicReceiver topicReceiverHotel = new TopicReceiver("Víctor", "0909", "prediction.Hotel");
        TopicReceiver topicReceiverWeather = new TopicReceiver("Víctor", "0910", "prediction.Weather");

        topicReceiverHotel.start(controller);
        topicReceiverWeather.start(controller);
    }
}
