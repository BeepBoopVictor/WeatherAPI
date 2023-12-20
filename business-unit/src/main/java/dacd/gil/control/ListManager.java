package dacd.gil.control;

import dacd.gil.model.Hotel;
import dacd.gil.model.Weather;

import java.util.ArrayList;
import java.util.OptionalInt;

public interface ListManager {
    public void addWeather(Integer position, Weather weather);
    public OptionalInt checkWeather(String date, String island);
    public void addHotel(Hotel hotel);
    public boolean checkHotel(String date, String hotelKey);
    ArrayList<Hotel> convertToHotel(String jsonString);
    Weather convertToWeather(String jsonString);
}
