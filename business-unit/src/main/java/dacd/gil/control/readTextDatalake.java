package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

import java.io.*;
import java.util.*;

public class readTextDatalake implements datalakeLoader{
    private Map<String, Manager> mapHotel;
    public readTextDatalake(Map<String, Manager> mapHotel) {
        this.mapHotel = mapHotel;
    }

    @Override
    public void readEvents(String datalakePath, String topic) throws CustomException {
        try {
            List<String> events = findMostRecentFile(datalakePath, topic);
            Manager manager = mapHotel.get(topic);

            for (String event : events) {
                manager.manageEvents(event);
            }
        } catch (IOException e) {
            throw new CustomException("Error getting the file", e);
        }
    }

    public List<String> findMostRecentFile(String datalakePath, String topic) throws IOException {
        File directory = new File(datalakePath);
        File[] files = directory.listFiles((dir, name) -> name.matches("\\d{8}.events"));

        if (files == null || files.length == 0) {
            return null;
        }

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        File mostRecent = files[0];
        String routeToMostRecentFile = mostRecent.getAbsolutePath();
        List<String> content = getLastLines(routeToMostRecentFile, topic);

        return content;
    }

    public List<String> getLastLines(String routeToMostRecentFile, String topic) throws IOException {
        int amountOfLines = topic.equals("prediction.Hotel") ? 5 : 40;

        try (BufferedReader br = new BufferedReader(new FileReader(routeToMostRecentFile))) {
            String line;
            List<String> lastLines = new ArrayList<>();
            String[] buffer = new String[amountOfLines];
            int count = 0;

            while ((line = br.readLine()) != null) {
                buffer[count % amountOfLines] = line;
                count++;
            }

            int lineNumber = Math.min(count, amountOfLines);
            for (int i = 0; i < lineNumber; i++) {
                if (buffer[(count + i) % amountOfLines] != null) {
                    lastLines.add(buffer[(count + i) % amountOfLines]);
                }
            }

            return lastLines;
        }
    }
}
