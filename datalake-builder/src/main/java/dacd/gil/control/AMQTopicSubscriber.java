package dacd.gil.control;

import dacd.gil.control.exception.StoreException;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;


public class AMQTopicSubscriber implements Subscriber{
    private final String brokerUrl;
    private final String topicName;
    private final String consumerName;
    private final String clientID;

    public AMQTopicSubscriber(String topicName, String consumerName, String clientID) {
        this.brokerUrl = "tcp://localhost:61616";
        this.topicName = topicName;
        this.consumerName = consumerName;
        this.clientID = clientID;
    }

    @Override
    public void start(Listener listener) throws StoreException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.brokerUrl);

        Session session;
        Topic topic;
        try {
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(this.clientID);
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic(this.topicName);
        } catch (JMSException e) {
            throw new StoreException(e);
        }

        MessageConsumer consumer = null;
        try {
            consumer = session.createDurableSubscriber(topic, this.consumerName);
        } catch (JMSException e) {
            throw new StoreException(e);
        }

        try {
            consumer.setMessageListener(message -> {
                try {
                    String text = ((TextMessage) message).getText();
                    listener.consume(text, this.topicName);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            throw new StoreException(e);
        }

        System.out.println("Running");
    }
}