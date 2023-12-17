package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

public interface SaveWeather {
    void save(String JsonWeather) throws CustomException;
}
