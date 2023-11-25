package dacd.gil.control;

import com.google.gson.Gson;
import dacd.gil.model.Weather;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.ArrayList;

public class Listener implements WeatherReceiver{

    public Listener() {}

    @Override
    public ArrayList<Weather> getWeather() {
        String brokerUrl = "tcp://localhost:61616";

        String topicName = "prediciton.Weather";

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        try {
            Connection connection = connectionFactory.createConnection();

            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(topicName);

            MessageConsumer consumer = session.createConsumer(topic);

            ArrayList<String> weatherJson = new ArrayList<>();
            ArrayList<Weather> weathers = new ArrayList<>();
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        if (message instanceof TextMessage) {
                            TextMessage textMessage = (TextMessage) message;
                            System.out.println("Received message: " + textMessage.getText());
                            weatherJson.add(textMessage.getText());
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            System.out.println("Waiting for messages. Please wait   :3");

            Thread.sleep(Long.MAX_VALUE);

            consumer.close();
            session.close();
            connection.close();

            for(String weatherString: weatherJson){
                weathers.add(jsonToWeather(weatherString));
            }
            return weathers;
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    public static Weather jsonToWeather(String jsonWeather){
        Gson gson = new Gson();
        return gson.fromJson(jsonWeather, Weather.class);
    }
}
