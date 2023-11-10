package dacd.gil.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import dacd.gil.model.Location;
import dacd.gil.model.Weather;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenWeatherMapProvider implements WeatherProvides {
    private final String template_url;
    public String apiKEY;

    public OpenWeatherMapProvider(String apiKEY) {
        this.template_url = "https://api.openweathermap.org/data/2.5/forecast?lat=#&lon=#";
        this.apiKEY = apiKEY;
    }

    public ArrayList<Weather> weatherGet(Location location, Instant instant) {
        URL url = getUrl(location, instant);
        String jsonWeather = getStringBuilder(url);
        try {
            return parseJsonData(jsonWeather, location, instant);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Weather> parseJsonData(String jsonData, Location location, Instant instant) throws JsonProcessingException {
        ArrayList<Weather> weathers = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        JsonNode list = jsonNode.get("list");

        for (JsonNode item: list){
            double temperature = item.get("main").get("temp").asDouble();
            int humidity = item.get("main").get("humidity").asInt();
            int clouds = item.get("clouds").get("all").asInt();
            double windSpeed = item.get("wind").get("speed").asDouble();
            String time = item.get("dt_txt").asText();

            double rain = 0.0;
            if (item.has("pop")) {
                rain = item.get("pop").asDouble();
            }
            if(item.get("dt_txt").asText().endsWith("00:00:00")){
                weathers.add(new Weather(temperature, humidity, rain, windSpeed, clouds, location, time));
            }
        }
        return weathers;
    }

    private static String getStringBuilder(URL url) {
        StringBuilder informationString;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }

            scanner.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return informationString.toString();
    }

    private URL getUrl(Location location, Instant instant) {
        String baseUrl = this.template_url; // URL base con '#'
        String url = baseUrl.replace("#", location.lat + "").replace("#", location.lon + "") + "&appid=" + this.apiKEY;
        URL returnURL = null;
        try {
            returnURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return returnURL;
    }
}
