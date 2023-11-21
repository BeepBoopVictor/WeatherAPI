package dacd.gil.control;

import dacd.gil.model.Weather;

public interface SendWeatherTopic {
    public void sendWeather(String jsonWeather);
}
