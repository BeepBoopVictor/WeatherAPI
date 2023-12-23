package dacd.gil.model;
import java.time.Instant;

public class Weather {
    private double temp;
    private int humidity;
    private double rain;
    private double windSpeed;
    private double clouds;
    private Instant predictionTime;
    private String location;

    public Weather(double temp, int humidity, double rain, double windSpeed, double clouds, Instant predictionTime, String location) {
        this.temp = temp;
        this.humidity = humidity;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.clouds = clouds;
        this.predictionTime = predictionTime;
        this.location = location;
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

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public String getLocation() {
        return location;
    }

}
