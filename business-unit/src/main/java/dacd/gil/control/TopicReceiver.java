package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;


public class TopicReceiver implements Subscriber{
    private final String brokerUrl;
    private final String topicName;
    private final String consumerName;
    private final String clientID;

    public TopicReceiver(String consumerName, String clientID) {
        this.brokerUrl = "tcp://localhost:61616";
        this.topicName = "prediction.Weather";
        this.consumerName = consumerName;
        this.clientID = clientID;
    }

    @Override
    public void start(SaveWeather saveWeather) throws CustomException {
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
                    saveWeather.save(text);
                } catch (JMSException | CustomException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            throw new CustomException("Error listening the message", e);
        }

        System.out.println("Running");
    }

    public void processMessage(){

    }
}