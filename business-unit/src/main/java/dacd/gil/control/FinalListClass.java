package dacd.gil.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dacd.gil.model.HotelPriceWeather;
import dacd.gil.model.Hotel;
import dacd.gil.model.Weather;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class FinalListClass implements ListManager{
    private final List<HotelPriceWeather> hotelPriceWeatherList = new ArrayList<>();

    public FinalListClass() {
    }

    @Override
    public void addWeather(Integer position, Weather weather){
        hotelPriceWeatherList.get(position).setWeather(weather);
    }

    @Override
    public OptionalInt checkWeather(String date, String island) {
        return IntStream.range(0, hotelPriceWeatherList.size())
                .filter(i -> hotelPriceWeatherList.get(i).getDay().equals(date)
                        && hotelPriceWeatherList.get(i).getLocation().equals(island))
                .findFirst();
    }

    @Override
    public boolean checkHotel(String date, String hotelKey){
        return hotelPriceWeatherList.stream()
                .anyMatch(item -> item.getDay().equals(date) && item.getHotelKey().equals(hotelKey));
    }

    @Override
    public void addHotel(Hotel hotel){
        hotelPriceWeatherList.add(new HotelPriceWeather(hotel.getHotelKey(), hotel.getLocation(), hotel.getDay(), null, hotel.getPriceStatus()));
    }


    public List<String> getHotelCheapDate(String hotelJson){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(hotelJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<String> cheapPrices = getList(jsonNode, "cheapPrices");
        return cheapPrices;
    }

    public List<String> getHotelAverageDate(String hotelJson){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(hotelJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<String> averagePrices = getList(jsonNode, "averagePrices");
        return averagePrices;
    }

    public List<String> getHotelExpensiveDate(String hotelJson){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(hotelJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<String> highPrices = getList(jsonNode, "highPrices");
        return highPrices;
    }

    private static List<String> getList(JsonNode jsonNode, String listName) {
        List<String> priceList = new ArrayList<>();
        JsonNode listNode = jsonNode.get(listName);

        if (listNode != null && listNode.isArray()) {
            for (JsonNode priceNode : listNode) {
                if (priceNode.isTextual()) {
                    priceList.add(priceNode.asText());
                }
            }
        }
        return priceList;
    }


    public Weather convertToWeather(String jsonString){
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        double temp = jsonObject.get("temp").getAsDouble();
        int humidity = jsonObject.get("humidity").getAsInt();
        double rain = jsonObject.get("rain").getAsDouble();
        double windSpeed = jsonObject.get("windSpeed").getAsDouble();
        double clouds = jsonObject.get("clouds").getAsDouble();
        String predictionDateString = jsonObject.get("predictionTime").getAsString();
        LocalDate predictionDate = LocalDate.parse(predictionDateString);
        Instant predictionTime = predictionDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        String location = jsonObject.getAsJsonObject("location").get("name").getAsString();


        return new Weather(temp, humidity, rain, windSpeed, clouds, predictionTime, location);
    }

    public ArrayList<Hotel> convertToHotel(String jsonString){
        ArrayList<Hotel> hotelList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        String location = jsonObject.getAsJsonObject("hotelValues").get("location").getAsString();
        String hotelKey = jsonObject.getAsJsonObject("hotelValues").get("hotelToken").getAsString();
        List<String> cheapPrices = getHotelCheapDate(jsonString);
        List<String> averagePrices = getHotelAverageDate(jsonString);
        List<String> expensivePrices = getHotelExpensiveDate(jsonString);
        for (String priceStatus: cheapPrices){
            hotelList.add(new Hotel(location, hotelKey, "cheap", Instant.parse(priceStatus)));
        }
        for (String priceStatus: averagePrices){
            hotelList.add(new Hotel(location, hotelKey, "average", Instant.parse(priceStatus)));
        }
        for (String priceStatus: expensivePrices){
            hotelList.add(new Hotel(location, hotelKey, "expensive", Instant.parse(priceStatus)));
        }

        return hotelList;
    }

    public void convertToJson(){
        Gson gson = new Gson();
        for(HotelPriceWeather hotelPriceWeather: hotelPriceWeatherList){
            System.out.println(gson.toJson(hotelPriceWeather));
        }
    }

}