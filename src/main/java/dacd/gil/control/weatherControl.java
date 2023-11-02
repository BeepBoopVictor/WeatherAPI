package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.net.MalformedURLException;
import java.time.Instant;
import java.util.List;

public class weatherControl {
    public final List<Location> locations;

    public weatherControl() {
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
    }

    public void execute(){
        Instant instant = Instant.now();
        OpenWeatherMapProvider openWeatherMapProvider = new OpenWeatherMapProvider("8fb79003589f3912f05096709e2dbffd");
        SQLiteWeatherStore sqLiteWeatherStore = new SQLiteWeatherStore();
        Weather weather;
        for(Location location: locations){
            weather = openWeatherMapProvider.weatherGet(location, instant);
            sqLiteWeatherStore.save(weather);
        }
    }
}
