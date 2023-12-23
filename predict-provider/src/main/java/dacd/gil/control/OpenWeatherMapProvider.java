package dacd.gil.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dacd.gil.control.exception.StoreException;
import dacd.gil.model.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenWeatherMapProvider implements WeatherProvides {
    private final String template_url;
    private final String apiKEY;

    public OpenWeatherMapProvider(String apiKEY) {
        this.template_url = "https://api.openweathermap.org/data/2.5/forecast?lat=#&lon=!";
        this.apiKEY = apiKEY;
    }

    public ArrayList<String> weatherGet(Location location, Instant instant) throws StoreException {
        URL url = getUrl(location);
        String jsonWeather = getStringBuilder(url);
        try {
            return parseJsonData(jsonWeather, location);
        }
        catch (JsonProcessingException e) {throw new StoreException("Error" + e);}
    }

    private ArrayList<String> parseJsonData(String jsonData, Location location) throws JsonProcessingException {
        ArrayList<String> weathers = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        JsonNode list = jsonNode.get("list");
        for (JsonNode item: list){introduceWeathers(location, item, weathers);}
        return weathers;
    }

    private static void introduceWeathers(Location location, JsonNode item, ArrayList<String> weathers) {
        Weather weather;
        Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
        String jsonWeather;
        double temperature = item.get("main").get("temp").asDouble();
        int humidity = item.get("main").get("humidity").asInt();
        int clouds = item.get("clouds").get("all").asInt();
        double windSpeed = item.get("wind").get("speed").asDouble();
        String time_str = item.get("dt_txt").asText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(time_str, formatter);
        Instant time = localDateTime.toInstant(ZoneOffset.UTC);

        double rain = 0.0;
        if (item.has("pop")) {rain = item.get("pop").asDouble();}
        if(item.get("dt_txt").asText().endsWith("00:00:00")){
            weather = new Weather(temperature, humidity, rain, windSpeed, clouds, location, time, Instant.now());
            jsonWeather = convertToJson(weather, gson);
            weathers.add(jsonWeather);
        }
    }

    private static String convertToJson(Weather weather, Gson gson){
        return gson.toJson(weather);
    }

    public static class InstantAdapter extends TypeAdapter<Instant> {
        @Override
        public void write(JsonWriter out, Instant value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public Instant read(JsonReader in) throws IOException {
            return Instant.parse(in.nextString());
        }
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
        } catch (IOException e) {throw new RuntimeException(e);}
        return informationString.toString();
    }

    public URL getUrl(Location location) {
        String url = this.template_url.replace("#", location.getLat() + "").replace("!", location.getLon() + "") + "&appid=" + this.apiKEY + "&units=metric";
        URL returnURL;
        try {returnURL = new URL(url);}
        catch (MalformedURLException e) {throw new RuntimeException(e);}
        System.out.println(returnURL);
        return returnURL;
    }
}
