package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TopicReceiver implements Subscriber {
    private String brokerUrl;
    private static Map<String, Manager> mapManager;

    public TopicReceiver(Map<String, Manager> mapManager) {
        this.brokerUrl = "tcp://localhost:61616";
        this.mapManager = mapManager;
    }

    @Override
    public void start(storeInterface storeInterface) throws CustomException{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.brokerUrl);
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID("1234");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            createSuscription(session, "prediction.Weather");
            createSuscription(session, "prediction.Hotel");
        } catch (JMSException e) {
            throw new CustomException("", e);
        }
    }

    private static void createSuscription(Session session, String topic) throws CustomException{
        try{
            Topic destination = session.createTopic(topic);
            MessageConsumer consumer = session.createDurableSubscriber(destination, topic);
            consumer.setMessageListener(message -> {
                try{
                    String text = ((TextMessage) message).getText();
                    Manager manager = mapManager.get(topic);
                    manager.manageEvents(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            throw new CustomException("", e);
        }
    }


    /*public void processMessages(Controller controller){
        for (String jsonString : jsonList) {
            controller.managerGeneral(jsonString, topicName);
        }
        for (int i = 0; i < controller.hotelPriceWeatherList.size(); i++){
            if(controller.hotelPriceWeatherList.get(i).getTemp() != 0.0){
                System.out.println(controller.hotelPriceWeatherList.get(i).getHotelKey() + ", " +
                        controller.hotelPriceWeatherList.get(i).getDay() + ", " +
                        controller.hotelPriceWeatherList.get(i).getPriceStatus() + ", " +
                        controller.hotelPriceWeatherList.get(i).getTemp());
            } else {
                System.out.println(controller.hotelPriceWeatherList.get(i).getHotelKey() + ", " +
                        controller.hotelPriceWeatherList.get(i).getDay() + ", " +
                        controller.hotelPriceWeatherList.get(i).getPriceStatus());
            }
        }
        jsonList.clear();
        timer.cancel();
        timer = null;
    }

    public void readMessages(Controller controller, String direction, int amountOfLines, String topicName) throws CustomException {
        try(BufferedReader br = new BufferedReader(new FileReader(direction))){
            String line;
            List<String> lines = new ArrayList<>();

            while ((line = br.readLine()) != null){
                lines.add(line);
            }

            int totalLines = lines.size();
            int start = Math.max(0, totalLines - amountOfLines);;
            int end = totalLines;
            for (int i = start; i < end; i++){
                controller.managerGeneral(lines.get(i), topicName);
            }
        }catch (IOException e){
            throw new CustomException("", e);
        }
    }*/
}
