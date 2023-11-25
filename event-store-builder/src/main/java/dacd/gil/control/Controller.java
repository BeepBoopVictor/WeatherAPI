package dacd.gil.control;

import dacd.gil.model.Weather;
import java.util.ArrayList;

public class Controller {
        private final Listener listener;
        private final SQLiteWeatherStore SQLWeatherStore;

        public Controller(Listener listener, SQLiteWeatherStore SQLWeatherStore) {
            this.listener = listener;
            this.SQLWeatherStore = SQLWeatherStore;
        }

        public void execute(){
            ArrayList<Weather> weathers = this.listener.getWeather();
            for (Weather weather: weathers){
                this.SQLWeatherStore.save(weather);
            }
        }
}
