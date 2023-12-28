package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;
import dacd.gil.model.HotelPriceWeather;

import java.util.Map;

public interface storeInterface {
    void save(Map<String, String> objectMap) throws CustomException;
}
