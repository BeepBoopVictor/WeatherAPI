package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.*;


public class    TopicReceiver implements Subscriber {
    private String brokerUrl;
    private static Map<String, Manager> mapManager;
    private static final Object lock = new Object();

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
            connection.setClientID("12345");
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
                synchronized (lock){
                    manageEventsSend(topic, (TextMessage) message);
                }
            });
        } catch (JMSException e) {
            throw new CustomException("", e);
        }
    }

    private static void manageEventsSend(String topic, TextMessage message) {
        try{
            String text = message.getText();
            Manager manager = mapManager.get(topic);
            manager.manageEvents(text);
        } catch (JMSException | CustomException e) {
            e.printStackTrace();
        }
    }

}
