package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.sql.Statement;
import java.time.Instant;

public interface WeatherStore {

    void save(Weather weather, Statement statement);

    public void load(Location location, Instant instant);
}
