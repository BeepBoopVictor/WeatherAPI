package dacd.gil.control;

import dacd.gil.control.exceptions.StoreException;

public interface WeatherStore {
    void save(String weather) throws StoreException;
}
