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


    public HotelPriceWeather(String hotelKey, String location, Instant day, String priceStatus) {
        this.hotelKey = hotelKey;
        this.location = location;
        this.day = day;
        this.priceStatus = priceStatus;
        this.temp = 0.0;
        this.humidity = 0;
        this.rain = 0.0;
        this.windSpeed = 0.0;
        this.clouds = 0.0;
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

    public double getTemp() {
        return temp;
    }
}
