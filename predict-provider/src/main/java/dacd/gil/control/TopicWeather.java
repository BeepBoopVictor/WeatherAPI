package dacd.gil.control;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicWeather implements SendWeatherTopic{

    private final String brokerURL;

    public TopicWeather(){
        this.brokerURL = "tcp://localhost:61616";
    }


    @Override
    public void sendWeather(String jsonWeather) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("prediciton.Weather");
            MessageProducer producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage(jsonWeather);
            producer.send(message);
            System.out.println("JCG printing@@ '" + message.getText() + "'");
            connection.close();
        } catch (JMSException e) {throw new RuntimeException(e);}
    }
}
