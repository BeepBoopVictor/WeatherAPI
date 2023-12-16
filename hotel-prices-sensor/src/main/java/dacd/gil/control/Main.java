package dacd.gil.control;

import dacd.gil.control.exception.CustomException;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        priceControl priceControl = new priceControl(new PriceProvider(), new TopicHotel());
        /*Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    priceControl.execute(args[0]);
                } catch (CustomException e) {
                    e.printStackTrace();
                }
            }
        }, new Date(), 6 * 60 * 60 * 1000);*/
        try {
            priceControl.execute(args[0]);
        } catch (CustomException e) {
            e.printStackTrace();
        }

    }
}
