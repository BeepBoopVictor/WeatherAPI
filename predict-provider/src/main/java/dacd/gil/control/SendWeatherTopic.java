package dacd.gil.control;

import dacd.gil.model.Weather;

public interface SendWeatherTopic {
    void sendWeather(String jsonWeather);
}
