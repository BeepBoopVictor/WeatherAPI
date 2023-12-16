package dacd.gil.control;

import dacd.gil.control.exception.CustomException;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicHotel implements SendHotel {
    private final String brokerURL;

    public TopicHotel() {
        this.brokerURL = "tcp://localhost:61616";
    }

    @Override
    public void sendHotel(String hotelJSON) throws CustomException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("prediction.Hotel");
            MessageProducer producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage(hotelJSON);
            if(message != null) {
                producer.send(message);
            }
            System.out.println("JCG printing@@ '" + message.getText() + "'");
            connection.close();
        } catch (JMSException e) {
            throw new CustomException("Error sending", e);
        }
    }
}
