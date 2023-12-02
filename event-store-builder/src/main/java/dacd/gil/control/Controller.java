package dacd.gil.control;

import dacd.gil.control.exceptions.StoreException;

import java.util.ArrayList;

public class Controller {
        private final Listener listener;
        private final WeatherStorage weatherStorage;

        public Controller(Listener listener, WeatherStorage weatherStore) {
            this.listener = listener;
            this.weatherStorage = weatherStore;
        }

        public void execute(){
            ArrayList<String> weathers = this.listener.getWeather();
            for (String weather: weathers){
                try{this.weatherStorage.save(weather);}
                catch (StoreException e){e.printStackTrace();};
            }
        }
}
