package dacd.gil.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dacd.gil.control.Exception.CustomException;
import dacd.gil.model.Weather;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class weatherManager implements Manager{
    private storeInterface dataStore;
    public weatherManager(storeInterface storeInterface) {
        this.dataStore = storeInterface;
    }

    @Override
    public void manageEvents(String jsonString) throws CustomException {
        Weather weather = convertToWeather(jsonString);
        Map<String, String> weatherMap = new HashMap<>();
        weatherMap.put("object", "weather");
        weatherMap.put("Temperature", String.valueOf(weather.getTemp()));
        weatherMap.put("Rain", String.valueOf(weather.getRain()));
        weatherMap.put("Humidity", String.valueOf(weather.getHumidity()));
        weatherMap.put("Clouds", String.valueOf(weather.getClouds()));
        weatherMap.put("WindSpeed", String.valueOf(weather.getWindSpeed()));
        weatherMap.put("location", String.valueOf(weather.getLocation()));
        weatherMap.put("day", String.valueOf(weather.getPredictionTime()));
        this.dataStore.save(weatherMap);
    }


    public Weather convertToWeather(String jsonString){
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        double temp = jsonObject.get("temp").getAsDouble();
        int humidity = jsonObject.get("humidity").getAsInt();
        double rain = jsonObject.get("rain").getAsDouble();
        double windSpeed = jsonObject.get("windSpeed").getAsDouble();
        double clouds = jsonObject.get("clouds").getAsDouble();
        String predictionDateString = jsonObject.get("predictionTime").getAsString();
        Instant predictionTime = Instant.parse(predictionDateString);
        String location = jsonObject.getAsJsonObject("location").get("name").getAsString();
        return new Weather(temp, humidity, rain, windSpeed, clouds, predictionTime, location);
    }

}
