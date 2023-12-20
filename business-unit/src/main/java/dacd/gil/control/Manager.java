package dacd.gil.control;

public interface Manager {
    void managerGeneral(String jsonString, String topicName);
    void manageHotel(String jsonString);
    void manageWeather(String jsonString);
}
