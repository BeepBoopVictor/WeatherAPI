package dacd.gil;

import dacd.gil.control.PriceController;
import dacd.gil.control.PriceProvider;
import dacd.gil.control.TopicHotel;
import dacd.gil.control.exception.CustomException;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        PriceController PriceController = new PriceController(new PriceProvider(), new TopicHotel());
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    PriceController.execute(args[0]);
                } catch (CustomException e) {
                    e.printStackTrace();
                }
            }
        }, new Date(), 12 * 60 * 60 * 1000);
    }
}
