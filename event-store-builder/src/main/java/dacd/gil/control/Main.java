package dacd.gil.control;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Listener(), new WeatherStorage(args[0]));
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {controller.execute();}
        }, new Date(), 6 * 60 * 60 * 1000);
    }
}