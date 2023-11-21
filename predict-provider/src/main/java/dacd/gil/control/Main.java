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
        ScheduledExecutorService scheduler  = Executors.newSingleThreadScheduledExecutor();
        long actualTime = System.currentTimeMillis();
        long hour12Millis = getHour12Millis(actualTime);
        if (actualTime > hour12Millis){
            hour12Millis = getHour12Millis(actualTime + TimeUnit.DAYS.toMillis(1));
        }

        weatherController.execute();

        long differenceTo12 = hour12Millis - actualTime;
        scheduler.scheduleAtFixedRate(() -> execute(timer, weatherController, args[0]), differenceTo12, 24*60*60*1000, TimeUnit.MILLISECONDS);
    }

    private static void execute(Timer timer, weatherControl weatherController, String apiKey) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                weatherController.execute();
            }
        }, new Date(), 6 * 60 * 60 * 1000);

        try {
            Thread.sleep(5 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {e.printStackTrace();}
        timer.cancel();
    }

    private static long getHour12Millis(long actualTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(actualTime);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
