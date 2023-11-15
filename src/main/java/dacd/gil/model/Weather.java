package dacd.gil.model;

import java.time.Instant;

public class Weather {
    public double temp;
    public int humidity;
    public double rain;
    public double windSpeed;
    public double clouds;
    public Location location;
    public String ts;

    public Weather(double temp, int humidity, double rain, double windSpeed, double clouds, Location location, String ts) {
        this.temp = Math.round((temp - 272.1) * 100) / 100d;
        this.humidity = humidity;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.clouds = clouds;
        this.location = location;
        this.ts = ts;
    }
}