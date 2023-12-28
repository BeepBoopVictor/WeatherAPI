package dacd.gil.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dacd.gil.model.HotelPriceWeather;
import dacd.gil.model.Weather;

import java.time.Instant;

public class weatherManager implements Manager{
    private dataMartSQL dataStore;
    public weatherManager(dataMartSQL dataStore) {
        this.dataStore = dataStore;
    }

    public void manageEvents(String jsonString){
        HotelPriceWeather hotelPriceWeather = new HotelPriceWeather();
        hotelPriceWeather.setWeather(convertToWeather(jsonString));
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
