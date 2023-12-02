package dacd.gil.control;

import dacd.gil.control.exceptions.StoreException;
import dacd.gil.model.Location;

import java.net.MalformedURLException;
import java.time.Instant;
import java.util.ArrayList;

public interface WeatherProvides {
    ArrayList<String> weatherGet(Location location, Instant instant) throws StoreException;
}
