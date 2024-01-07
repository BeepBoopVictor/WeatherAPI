package dacd.gil.control;

import com.google.gson.Gson;
import dacd.gil.control.exception.StoreException;
import dacd.gil.model.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class weatherControl {
    private final ArrayList<Location> locations;
    private final OpenWeatherMapProvider openWeatherMapProvider;
    private final TopicWeather topicWeather;

    public weatherControl(OpenWeatherMapProvider openWeatherMapProvider, TopicWeather topicWeather, String textPath) {
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

    public void execute() throws StoreException {
        ArrayList<String> weathers;
        for(Location location: locations){
            weathers = this.openWeatherMapProvider.weatherGet(location);
            for (String weather: weathers){
                this.topicWeather.sendWeather(weather);
            }
        }
    }
}
