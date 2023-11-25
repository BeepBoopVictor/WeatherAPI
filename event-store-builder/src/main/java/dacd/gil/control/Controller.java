package dacd.gil.control;

import dacd.gil.model.Weather;

import java.util.ArrayList;

public class Controller {
    public class Control {
        private final Listener listener;
        private final SQLWeatherStore SQLWeatherStore;

        public Control(Listener listener, SQLWeatherStore SQLWeatherStore) {
            this.listener = listener;
            this.SQLWeatherStore = SQLWeatherStore;
        }

        public void execute(){
            ArrayList<Weather> weathers = this.listener.getWeather();

        }
    }

}
