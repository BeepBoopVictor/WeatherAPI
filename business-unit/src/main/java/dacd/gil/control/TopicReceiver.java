package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TopicReceiver implements Subscriber {
    private String brokerUrl;
    private String topicName;
    private String consumerName;
    private String clientID;
    private ArrayList<String> jsonList;

    public TopicReceiver(String consumerName, String clientID, String topicName) {
        this.brokerUrl = "tcp://localhost:61616";
        this.topicName = topicName;
        this.consumerName = consumerName;
        this.clientID = clientID;
        this.jsonList = new ArrayList<>();
    }

    @Override
    public void start(Controller controller) throws CustomException {
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
            throw new CustomException("Error connecting", e);
        }

        MessageConsumer consumer = null;
        try {
            consumer = session.createDurableSubscriber(topic, this.consumerName);
        } catch (JMSException e) {
            throw new CustomException("", e);
        }

        try {
            consumer.setMessageListener(message -> {
                try {
                    String text = ((TextMessage) message).getText();
                    System.out.println(text);
                    this.jsonList.add(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            throw new CustomException("Error listening the message", e);
        }

        System.out.println("Running");
    }
}
