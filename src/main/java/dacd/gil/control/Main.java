package dacd.gil.control;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        weatherControl weatherController = new weatherControl();
        Timer timer = new Timer();

        // Configura una tarea que se ejecutará cada 6 horas
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                weatherController.execute();
            }
        }, new Date(), 6 * 60 * 60 * 1000); // 6 horas en milisegundos

        // Programa la ejecución durante 5 días
        try {
            Thread.sleep(5 * 24 * 60 * 60 * 1000); // 5 días en milisegundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Cancela la tarea y detiene el Timer después de 5 días
        timer.cancel();
    }
}
