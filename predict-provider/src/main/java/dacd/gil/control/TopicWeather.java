package dacd.gil.control;

import dacd.gil.model.Weather;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicWeather implements SendWeatherTopic{

    private final String brokerURL;

    public TopicWeather(){
        this.brokerURL = "tcp://localhost:61616";
    }


    @Override
    public void sendWeather(String jsonWeather) {
        /*ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.brokerURL);
        try(Connection connection = connectionFactory.createConnection()){
            connection.start();
            Session session = connection.createSession(false, TopicSession.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic("prediciton.Weather");

            MessageProducer producer = session.createProducer(topic);

        } catch (JMSException e) {throw new RuntimeException(e);}*/
    }
}
