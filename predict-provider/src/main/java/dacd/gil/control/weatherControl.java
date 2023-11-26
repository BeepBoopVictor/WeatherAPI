package dacd.gil.control;

import com.google.gson.Gson;
import dacd.gil.model.Location;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class weatherControl {
    private final ArrayList<Location> locations;
    private final OpenWeatherMapProvider openWeatherMapProvider;
    private final TopicWeather topicWeather;

    public weatherControl(OpenWeatherMapProvider openWeatherMapProvider, TopicWeather topicWeather, String textPath) {
        /*this.locations = List.of(new Location[]{
                new Location("GranCanaria", 28.1, -15.41),
                new Location("Tenerife", 28.46, -16.25),
                new Location("Fuerteventura", 28.2, -14.00),
                new Location("Lanzarote", 28.95, -13.76),
                new Location("LaPalma", 28.71, -17.9),
                new Location("ElHierro", 27.75, -18),
                new Location("LaGomera", 28.1, -17.11),
                new Location("LaGraciosa", 28.05, -15.44),
        });*/
        this.locations = readLocations(textPath);
        this.openWeatherMapProvider = openWeatherMapProvider;
        this.topicWeather = topicWeather;
    }

    public ArrayList<Location> readLocations(String textPath){
        Gson gson = new Gson();
        Location location;
        ArrayList<Location> locations = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(textPath))){
            String line;
            while ((line = br.readLine()) != null){
                location = gson.fromJson(line, Location.class);
                locations.add(location);
            }
            return locations;
        }catch (IOException e){throw new RuntimeException(e);}
    }

    public void execute(){
        ArrayList<String> weathers;
        Instant actualInstant = Instant.now();

        for(Location location: locations){
            weathers = this.openWeatherMapProvider.weatherGet(location, actualInstant);
            for (String weather: weathers){
                this.topicWeather.sendWeather(weather);
            }
        }
    }
}
