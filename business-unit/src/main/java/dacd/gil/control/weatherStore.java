package dacd.gil.control;

import com.google.gson.JsonObject;
import dacd.gil.control.Exception.CustomException;
import jakarta.jms.Session;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class weatherStore implements SaveWeather{
    private String url;
    public weatherStore(String directory) {
        this.url = "jdbc:sqlite:" + directory + "\\weather.db";
    }
    @Override
    public void save(String weatherJson) throws CustomException{
        JSONObject weatherJsonObject = new JSONObject(weatherJson);
        String tableName = getTableName(weatherJsonObject);
        String date = getDate(weatherJsonObject);
        try(Connection connection = DriverManager.getConnection(this.url)){
            try(Statement statement = connection.createStatement()) {
                createTable(tableName, statement);
                if(searchDate(tableName, date, statement)) {
                    updateValue(statement, weatherJsonObject);
                } else {
                    insert(statement, weatherJsonObject);
                }
            }
        }catch (SQLException e){
            throw new CustomException("Error connecting to database", e);
        }
    }

    public void createTable(String name, Statement statement) throws CustomException {
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS " + name + " (" +
                    "Date TEXT," +
                    "Temperature REAL," +
                    "Rainfall REAL," +
                    "Humidity REAL," +
                    "Clouds REAL," +
                    "WindSpeed REAL" +
                    ");");
        } catch (SQLException e) {
            throw new CustomException("Error creating the table", e);
        }
    }

    public void updateValue(Statement statement, JSONObject weatherJson) throws CustomException {
        String query = "UPDATE " + getTableName(weatherJson) + " SET Temperature = ?, Rainfall = ?, Humidity = ?, Clouds = ?, WindSpeed = ? WHERE Date = ?";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query)) {
            preparedStatement.setDouble(2, getTemp(weatherJson));
            preparedStatement.setDouble(3, Double.parseDouble(getRain(weatherJson)));
            preparedStatement.setInt(4, Integer.parseInt(getHumidity(weatherJson)));
            preparedStatement.setDouble(5, Double.parseDouble(getClouds(weatherJson)));
            preparedStatement.setDouble(6, Double.parseDouble(getWindSpeed(weatherJson)));
            preparedStatement.setString(1, getDate(weatherJson));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Error updating the content", e);
        }
    }

    public void insert(Statement statement, JSONObject weatherJson) throws CustomException {
        String insertQuery = "INSERT INTO " + getTableName(weatherJson) + " VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setString(1, getDate(weatherJson));
            preparedStatement.setDouble(2, getTemp(weatherJson));
            preparedStatement.setDouble(3, Double.parseDouble(getRain(weatherJson)));
            preparedStatement.setInt(4, Integer.parseInt(getHumidity(weatherJson)));
            preparedStatement.setDouble(5, Double.parseDouble(getClouds(weatherJson)));
            preparedStatement.setDouble(6, Double.parseDouble(getWindSpeed(weatherJson)));

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new CustomException("Error inserting values", e);
        }

    }


    public boolean searchDate(String tableName, String date, Statement statement) throws CustomException{
        try{
            ResultSet resultSet = statement.executeQuery("SELECT * from " + tableName);
            while(resultSet.next()){
                String columnValue = resultSet.getString(1);
                if (date.equals(columnValue)){
                    resultSet.close();
                    return true;
                }
            }
            resultSet.close();
            return false;
        } catch(SQLException e){
            throw new CustomException("Error searching the date", e);
        }
    }

    public static String getTableName(JSONObject weatherJson){
        JSONObject weatherValuesObject = weatherJson.getJSONObject("location");
        return weatherValuesObject.getString("name");
    }

    public static String getDate(JSONObject weatherJson){
        String weatherTimeObject = weatherJson.getString("predictionTime");
        return weatherTimeObject.substring(1, weatherTimeObject.length()-1);
    }

    public static double getTemp(JSONObject weatherJson){
        double weatherTimeObject = weatherJson.getDouble("temp");
        return weatherTimeObject;
    }

    public static String getRain(JSONObject weatherJson){
        String weatherTimeObject = weatherJson.getString("rain");
        return weatherTimeObject.substring(1, weatherTimeObject.length()-1);
    }

    public static String getHumidity(JSONObject weatherJson){
        String weatherTimeObject = weatherJson.getString("humidity");
        return weatherTimeObject.substring(1, weatherTimeObject.length()-1);
    }

    public static String getWindSpeed(JSONObject weatherJson){
        String weatherTimeObject = weatherJson.getString("windSpeed");
        return weatherTimeObject.substring(1, weatherTimeObject.length()-1);
    }

    public static String getClouds(JSONObject weatherJson){
        String weatherTimeObject = weatherJson.getString("clouds");
        return weatherTimeObject.substring(1, weatherTimeObject.length()-1);
    }
}
