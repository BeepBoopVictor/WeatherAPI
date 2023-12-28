package dacd.gil.control;

import dacd.gil.control.exception.StoreException;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.List;


public class AMQTopicSubscriber implements Subscriber{
    private final String brokerUrl;
    private static Storage storage;

    public AMQTopicSubscriber(Storage storage) {
        this.brokerUrl = "tcp://localhost:61616";
        this.storage = storage;
    }

    @Override
    public void start() throws StoreException{
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
            throw new StoreException("Error in start function", e);
        }
    }

    private static void createSuscription(Session session, String topic) throws StoreException{
        try{
            Topic destination = session.createTopic(topic);
            MessageConsumer consumer = session.createDurableSubscriber(destination, topic);
            consumer.setMessageListener(message -> {
                try{
                    String text = ((TextMessage) message).getText();
                    System.out.println(text);
                    storage.consume(text, topic);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            throw new StoreException("Error creating subscription", e);
        }
    }

}