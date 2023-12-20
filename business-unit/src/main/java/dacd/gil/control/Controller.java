package dacd.gil.control;

import dacd.gil.model.Hotel;
import dacd.gil.model.Weather;

import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalInt;

public class Controller implements Manager{
    private final ListManager listManager;

    public Controller(ListManager listManager) {
        this.listManager = listManager;
    }

    @Override
    public void managerGeneral(String jsonString, String topicName){
        if (topicName.equals("prediction.Weather")){
            manageWeather(jsonString);
        } else {
            manageHotel(jsonString);
        }
    }

    @Override
    public void manageHotel(String jsonString){
        ArrayList<Hotel> hotels = this.listManager.convertToHotel(jsonString);
        for(Hotel hotel: hotels){
            if(!this.listManager.checkHotel(hotel.getDay().toString(), hotel.getHotelKey())){
                this.listManager.addHotel(hotel);
            }
        }
    }

    @Override
    public void manageWeather(String jsonString) {
        Weather weather = this.listManager.convertToWeather(jsonString);
        OptionalInt position = this.listManager.checkWeather(weather.getPredictionTime().toString(), weather.getLocation());
        if(position.isPresent()){
            this.listManager.addWeather(position.getAsInt(), weather);
        }
    }
}
