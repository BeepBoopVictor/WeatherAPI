package dacd.gil.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dacd.gil.control.exception.CustomException;
import dacd.gil.model.Hotel;
import dacd.gil.model.hotelValues;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class PriceProvider implements InterfacePriceProvider{
    private final String urlTemplate;
    public PriceProvider() {
        this.urlTemplate = "https://data.xotelo.com/api/heatmap?hotel_key=#&chk_out=@";
    }

    @Override
    public Hotel priceGet(hotelValues hotelValues, String checkOut) throws CustomException {
        URL url = getURL(hotelValues, checkOut);
        String jsonPrices = getStringBuilder(url);
        try {
            return parseJsonData(jsonPrices, hotelValues);
        } catch (JsonProcessingException e) {
            throw new CustomException("Error processing", e);
        }
    }

    private Hotel parseJsonData(String jsonData, hotelValues hotelValues) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        JsonNode list = jsonNode.get("result").get("heatmap");
        if(list == null){
            return null;
        }
        return introduceHotels(list, hotelValues);
    }

    private static Hotel introduceHotels(JsonNode item, hotelValues hotelValues) {
        JsonNode jsonAveragePrices = item.get("average_price_days");
        JsonNode jsonCheapPrices = item.get("cheap_price_days");
        JsonNode jsonHighPrices = item.get("high_price_days");

        ArrayList<String> averagePrices = new ArrayList<>();
        ArrayList<String> cheapPrices = new ArrayList<>();
        ArrayList<String> highPrices = new ArrayList<>();

        if(jsonAveragePrices.isArray()){
            for(JsonNode dateNode: jsonAveragePrices){
                averagePrices.add(String.valueOf(dateNode).substring(1,11));
            }
        }

        if(jsonCheapPrices.isArray()){
            for(JsonNode dateNode: jsonCheapPrices){
                cheapPrices.add(String.valueOf(dateNode).substring(1,11));
            }
        }

        if(jsonHighPrices.isArray()){
            for(JsonNode dateNode: jsonHighPrices){
                highPrices.add(String.valueOf(dateNode).substring(1,11));
            }
        }



        return new Hotel(hotelValues, averagePrices, highPrices, cheapPrices, Instant.now());
    }


    public URL getURL(hotelValues hotelValues, String checkOut) throws CustomException {
        String urlReplacer = this.urlTemplate.replace("#", hotelValues.getHotelToken()).replace("@", checkOut);
        URL urlReturn;
        try {
            urlReturn = new URL(urlReplacer);
        }
        catch (MalformedURLException e) {
            throw new CustomException("Error creating the URL", e);
        }
        return urlReturn;
    }

    private static String getStringBuilder(URL url) throws CustomException {
        StringBuilder informationString;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            throw new CustomException("Error connecting to the URL", e);
        }
        return informationString.toString();
    }
}
