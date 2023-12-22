package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

public class Main {
    public static void main(String[] args) throws CustomException {
        Controller controller = new Controller();
        TopicReceiver topicReceiverHotel = new TopicReceiver("Víctor", "0909", "prediction.Hotel");
        TopicReceiver topicReceiverWeather = new TopicReceiver("Víctor", "0910", "prediction.Weather");

        topicReceiverHotel.start(controller);
        topicReceiverWeather.start(controller);
    }
}
