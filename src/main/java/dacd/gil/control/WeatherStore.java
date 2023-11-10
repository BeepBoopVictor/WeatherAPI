package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.sql.Statement;
import java.time.Instant;
import java.util.Optional;

public interface WeatherStore {

    void save(Weather weather, Statement statement, int count);

    //public Optional<Weather> loadWeather(Location location, Instant instant);
}
