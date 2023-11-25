package dacd.gil.control;

import dacd.gil.model.Location;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class weatherControl {
    private final List<Location> locations;
    private final OpenWeatherMapProvider openWeatherMapProvider;
    private final TopicWeather topicWeather;

    public weatherControl(OpenWeatherMapProvider openWeatherMapProvider, TopicWeather topicWeather) {
        this.locations = List.of(new Location[]{
                new Location("GranCanaria", 28.1, -15.41),
                new Location("Tenerife", 28.46, -16.25),
                new Location("Fuerteventura", 28.2, -14.00),
                new Location("Lanzarote", 28.95, -13.76),
                new Location("LaPalma", 28.71, -17.9),
                new Location("ElHierro", 27.75, -18),
                new Location("LaGomera", 28.1, -17.11),
                new Location("LaGraciosa", 28.05, -15.44),
        });
        this.openWeatherMapProvider = openWeatherMapProvider;
        this.topicWeather = topicWeather;
    }

    public void execute(){
        ArrayList<String> weathers;
        Instant actualInstant = Instant.now();

        for(Location location: locations){
            weathers = this.openWeatherMapProvider.weatherGet(location, actualInstant);
            for (String weather: weathers){
                System.out.println(weather);
                this.topicWeather.sendWeather(weather);
            }
        }
    }
}
