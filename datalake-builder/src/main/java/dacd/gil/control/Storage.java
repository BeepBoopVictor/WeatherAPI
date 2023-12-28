package dacd.gil.control;

public interface Storage {
    void consume(String weatherJson, String topicName);
}