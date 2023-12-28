package dacd.gil.model;

import java.time.Instant;

public class HotelPriceWeather {
    private String hotelKey;
    private String location;
    private Instant day;
    private String priceStatus;
    private double temp;
    private int humidity;
    private double rain;
    private double windSpeed;
    private double clouds;


    public HotelPriceWeather() {
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

    public String getPriceStatus() {
        return priceStatus;
    }

    public void setWeather(Weather weather) {
        this.temp = weather.getTemp();
        this.humidity = weather.getHumidity();
        this.rain = weather.getRain();
        this.windSpeed = weather.getWindSpeed();
        this.clouds = weather.getClouds();
    }

    public void setHotel(Hotel hotel){
        this.hotelKey = hotel.getHotelKey();
        this.location = hotel.getLocation();
        this.day = hotel.getDay();
        this.priceStatus = hotel.getPriceStatus();
    }

    public double getTemp() {
        return temp;
    }
}
