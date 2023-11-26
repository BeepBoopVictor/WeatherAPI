package dacd.gil.control;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        weatherControl weatherController = new weatherControl(new OpenWeatherMapProvider(args[0]), new TopicWeather());
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {weatherController.execute();}
        }, new Date(), 6 * 60 * 60 * 1000);
    }
}
