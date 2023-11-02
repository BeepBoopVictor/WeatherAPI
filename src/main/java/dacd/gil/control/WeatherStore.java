package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.time.Instant;

public interface WeatherStore {
    public void save(Weather weather);
    public void load(Location location, Instant instant);
}
