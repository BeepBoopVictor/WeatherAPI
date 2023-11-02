package dacd.gil.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Map;
import java.util.Scanner;

public class OpenWeatherMapProvider implements WeatherProvides {
    private final String template_url;
    public String apiKEY;

    public OpenWeatherMapProvider(String apiKEY) {
        this.template_url = "https://api.openweathermap.org/data/2.5/weather?lat=#&lon=#";
        this.apiKEY = apiKEY;
    }

    public Weather weatherGet(Location location, Instant instant) {
        URL url = getUrl(location, instant);
        String jsonWeather = getStringBuilder(url);
        return parseJsonData(jsonWeather, location, instant);
    }

    private Weather parseJsonData(String jsonData, Location location, Instant instant){
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

        double temperature = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        int clouds = jsonObject.getAsJsonObject("clouds").get("all").getAsInt();
        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();

        // Para la lluvia, puedes manejarla de la siguiente manera si está disponible en el JSON.
        double rain = 0.0; // Valor predeterminado si no hay datos de lluvia
        if (jsonObject.has("rain") && jsonObject.getAsJsonObject("rain").has("1h")) {
            rain = jsonObject.getAsJsonObject("rain").get("1h").getAsDouble();
        }

        // Asegúrate de mapear correctamente los campos JSON a los campos de Weather
        return new Weather(temperature, humidity, rain, windSpeed, clouds, location, instant);
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
        //long unixTimestamp = instant.getEpochSecond();
        //url += "&dt=" + unixTimestamp;
        URL returnURL = null;
        try {
            returnURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return returnURL;
    }
}
