package dacd.gil.model;
import java.time.Instant;
import java.util.Date;

public class Weather {
    private double temp;
    private int humidity;
    private double rain;
    private double windSpeed;
    private double clouds;
    private Location location;
    private Instant predictionTime;
    private Instant ts;
    private String ss;

    public Weather(double temp, int humidity, double rain, double windSpeed, double clouds, Location location, Instant predictionTime, Instant ts) {
        this.temp = Math.round((temp - 272.1) * 100) / 100d;
        this.humidity = humidity;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.clouds = clouds;
        this.location = location;
        this.predictionTime = predictionTime;
        this.ss = "predict-provider";
        this.ts = ts;
    }

    public double getTemp() {
        return temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getRain() {
        return rain;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getClouds() {
        return clouds;
    }

    public Location getLocation() {
        return location;
    }

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public Instant getTs() {return ts;}

    public String getSs() {return ss;}
}
