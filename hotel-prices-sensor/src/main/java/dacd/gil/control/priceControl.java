package dacd.gil.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dacd.gil.control.exception.CustomException;
import dacd.gil.model.hotelValues;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class priceControl {

    private PriceProvider priceProvider;
    private TopicHotel topicHotel;

    public priceControl(PriceProvider priceProvider, TopicHotel topicHotel) {
        this.priceProvider = priceProvider;
        this.topicHotel = topicHotel;
    }

    public void execute(String textPath) throws CustomException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new OpenWeatherMapProvider.InstantAdapter()).create();
        ArrayList<hotelValues> hotelValuesArrayList = readHotelTokens(textPath);
        ArrayList<String> hotelArrayList = new ArrayList<>();
        String checkOutTime = getCheckOut();
        for(hotelValues tokens: hotelValuesArrayList){
            hotelArrayList.add(gson.toJson(this.priceProvider.priceGet(tokens, checkOutTime)));
            System.out.println(gson.toJson(this.priceProvider.priceGet(tokens, checkOutTime)));
        }

        for(String hotel: hotelArrayList){
            this.topicHotel.sendHotel(hotel);
        }
    }

    private String getCheckOut(){
        LocalDate today = LocalDate.now();
        LocalDate fiveDays = today.plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fiveDays.format(formatter);
    }

    private static ArrayList<hotelValues> readHotelTokens(String textPath) throws CustomException {
        Gson gson = new Gson();
        hotelValues hotelValues;
        ArrayList<hotelValues> hotelValuesArrayList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(textPath + "\\APIkeys.txt"))){
            String line;
            while ((line = br.readLine()) != null){
                hotelValues = gson.fromJson(line, hotelValues.class);
                hotelValuesArrayList.add(hotelValues);
            }
            return hotelValuesArrayList;
        }catch (IOException e){
            throw new CustomException("Error reading the hotelTokens", e);
        }
    }
}
