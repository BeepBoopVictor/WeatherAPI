package dacd.gil.model;

import java.time.Instant;

public class Weather {
    public double temp;
    public int humidity;
    public double rain;
    public double windSpeed;
    public double clouds;
    public Location location;
    public Instant ts;

    public Weather(double temp, int humidity, double rain, double windSpeed, double clouds, Location location, Instant ts) {
        this.temp = temp;
        this.humidity = humidity;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.clouds = clouds;
        this.location = location;
        this.ts = ts;
    }
}
