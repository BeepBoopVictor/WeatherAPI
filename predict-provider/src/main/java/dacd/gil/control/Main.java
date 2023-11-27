package dacd.gil.control;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        weatherControl weatherController = new weatherControl(new OpenWeatherMapProvider(args[0]), new TopicWeather(), args[1]);
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {weatherController.execute();}
        }, new Date(), 6 * 60 * 60 * 1000);
    }
}
