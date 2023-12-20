package dacd.gil.model;

import java.time.Instant;

public class HotelPriceWeather {
    private String hotelKey;
    private String location;
    private Instant day;
    private String priceStatus;
    private Weather weather;

    public HotelPriceWeather(String hotelKey, String location, Instant day, Weather weather, String priceStatus) {
        this.hotelKey = hotelKey;
        this.location = location;
        this.day = day;
        this.weather = weather;
        this.priceStatus = priceStatus;
    }

    public String getHotelKey() {
        return hotelKey;
    }

    public String getLocation() {
        return location;
    }

    public Instant getDay() {
        return day;
    }

    public Weather getWeather() {
        return weather;
    }

    public String getPriceStatus() {
        return priceStatus;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
