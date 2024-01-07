package dacd.gil.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FileEventStoreBuilder implements Storage{
    private final String path;
    public FileEventStoreBuilder(String fileDirectory) {
        this.path = fileDirectory;
    }

    @Override
    public void consume(String weather, String topicName) {
        JsonParser parser = new JsonParser();
        JsonObject data = (JsonObject) parser.parse(weather);
        String ssValue = data.get("ss").toString().replace("\"", "");

        String pTimeValue = data.get("ts").toString().replace("\"", "");

        File file3 = createFiles(ssValue, topicName);

        SimpleDateFormat baseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateOnly = newDateFormat.format(baseDateFormat.parse(pTimeValue));
            File fileToWrite = new File(file3 + File.separator + dateOnly + ".events");
            if(!fileToWrite.exists()){}
            else{if(fileToWrite.mkdir()){System.out.println("File created succesfully");}}

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite, true))) {
                writer.write(weather);
                writer.newLine();
            }
            catch (IOException e){e.printStackTrace();}
        } catch (ParseException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    private File createFiles(String ssValue, String topicName) {
        File file = new File(this.path + File.separator + "eventstore");
        if(file.exists()){}
        else{if(file.mkdir()){System.out.println("Directory created succesfully");}
        else{System.out.println("Directory not created");}}

        File file2 = new File(file + File.separator + topicName);
        if(file2.exists()){}
        else{if(file2.mkdir()){System.out.println("Directory created succesfully");}
        else{System.out.println("Directory not created");}}

        File file3 = new File(file2 + File.separator + ssValue);
        if(file3.exists()){}
        else{if(file3.mkdir()){System.out.println("Directory created succesfully");}
        else{System.out.println("Directory not created");}}
        return file3;
    }
}