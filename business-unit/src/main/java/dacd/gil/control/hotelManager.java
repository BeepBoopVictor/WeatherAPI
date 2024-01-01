package dacd.gil.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dacd.gil.control.Exception.CustomException;
import dacd.gil.model.Hotel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class hotelManager implements Manager{
    private dataMartSQL dataMartSQL;
    public hotelManager(dataMartSQL dataMartSQL) {
        this.dataMartSQL = dataMartSQL;
    }

    @Override
    public void manageEvents(String jsonString) throws CustomException {
        ArrayList<Hotel> hotels = convertToHotel(jsonString);
        Map<String, String> hotelMap = new HashMap<>();
        for(Hotel hotel: hotels){
            hotelMap.put("object", "Hotel");
            hotelMap.put("location", hotel.getLocation());
            hotelMap.put("hotelKey", hotel.getHotelKey());
            hotelMap.put("priceStatus", hotel.getPriceStatus());
            hotelMap.put("day", hotel.getDay().toString());
            dataMartSQL.save(hotelMap);
        }

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
            hotelList.add(new Hotel(location, hotelKey, "cheap", Instant.parse(priceStatus + "T00:00:00Z")));
        }
        for (String priceStatus: averagePrices){
            hotelList.add(new Hotel(location, hotelKey, "average", Instant.parse(priceStatus + "T00:00:00Z")));
        }
        for (String priceStatus: expensivePrices){
            hotelList.add(new Hotel(location, hotelKey, "expensive", Instant.parse(priceStatus + "T00:00:00Z")));
        }

        return hotelList;
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

}
