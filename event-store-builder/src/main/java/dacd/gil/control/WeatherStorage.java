package dacd.gil.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class WeatherStorage implements WeatherStore{
    private final String path;
    public WeatherStorage(String dbpath) {
        this.path = dbpath;
    }

    @Override
    public void save(String weather) {
        JsonParser parser = new JsonParser();
        JsonObject data = (JsonObject) parser.parse(weather);
        String ssValue = data.get("ss").toString().replace("\"", "");

        String pTimeValue = data.get("ts").toString().replace("\"", "");
        System.out.println(pTimeValue);


        File file = new File(this.path + File.separator + "eventstore");
        if(file.exists()){System.out.println("File exists");}
        else{if(file.mkdir()){System.out.println("Directory created succesfully");}
        else{System.out.println("Directory not created");}}

        File file2 = new File(file + File.separator + "prediction.Weather");
        if(file2.exists()){System.out.println("File exists");}
        else{if(file2.mkdir()){System.out.println("Directory created succesfully");}
        else{System.out.println("Directory not created");}}

        File file3 = new File(file2 + File.separator + ssValue);
        if(file3.exists()){System.out.println("File exists");}
        else{if(file3.mkdir()){System.out.println("Directory created succesfully");}
        else{System.out.println("Directory not created");}}

        SimpleDateFormat baseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateOnly = newDateFormat.format(baseDateFormat.parse(pTimeValue));
            File fileDefinitive = new File(file3 + File.separator + dateOnly);
            if(!fileDefinitive.exists()){}
            else{if(fileDefinitive.mkdir()){System.out.println("File created succesfully");}}

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileDefinitive, true))) {
                writer.write(weather);
                writer.newLine();
            }
            catch (IOException e){e.printStackTrace();}
        } catch (ParseException e) {throw new RuntimeException(e);}

    }
}
