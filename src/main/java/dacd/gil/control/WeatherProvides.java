package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.net.MalformedURLException;
import java.time.Instant;
import java.util.Map;

public interface WeatherProvides {
    public Weather weatherGet(Location location, Instant instant) throws MalformedURLException;
}
