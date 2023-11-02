package dacd.gil.control;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();
        Date horaInicio = new Date();
        horaInicio.setHours(0);
        horaInicio.setMinutes(0);
        horaInicio.setSeconds(0);

        // Programa la tarea para ejecutarse cada 6 horas durante 5 días
        timer.scheduleAtFixedRate(new MiTarea(), horaInicio, 6 * 60 * 60 * 1000); // 6 horas en milisegundos
        try {
            Thread.sleep(5 * 24 * 60 * 60 * 1000); // 5 días en milisegundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Cancela la tarea y cierra el timer
        timer.cancel();


    }

    static class MiTarea extends TimerTask {
        public void run() {
            weatherControl weatherControl = new weatherControl();
            weatherControl.execute(); // Llama a tu función execute() aquí
        }
    }
}
