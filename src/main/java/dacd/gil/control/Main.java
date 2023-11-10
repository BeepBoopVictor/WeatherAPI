package dacd.gil.control;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        weatherControl weatherController = new weatherControl();
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                weatherController.execute();
            }
        }, new Date(), 6 * 60 * 60 * 1000);

        try {
            Thread.sleep(5 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.cancel();
    }
}
