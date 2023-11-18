package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;

public class SQLiteWeatherStore implements WeatherStore {

    public SQLiteWeatherStore() {}

    @Override
    public void save(Weather weather, Statement statement){
        try {
            createTable(statement, weather.getLocation().getName());
            if (searchDate(weather.getLocation().getName(), statement, weather.getTs())){
                updateValue(statement, weather);
            } else {
                insert(statement, weather);
            }
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    public static boolean searchDate(String tableName, Statement statement, String date){
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
        } catch(SQLException e){e.printStackTrace(); return false;}
    }

    public void updateValue(Statement statement, Weather weather) {
        String query = "UPDATE " + weather.getLocation().getName() + " SET Temperature = ?, Rainfall = ?, Humidity = ?, Clouds = ?, WindSpeed = ? WHERE Date = ?";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query)) {
            preparedStatement.setDouble(2, weather.getTemp());
            preparedStatement.setDouble(3, weather.getRain());
            preparedStatement.setInt(4, weather.getHumidity());
            preparedStatement.setDouble(5, weather.getClouds());
            preparedStatement.setDouble(6, weather.getWindSpeed());
            preparedStatement.setString(1, weather.getTs());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
    }

    private void insert(Statement statement, Weather weather) {
        String insertQuery = "INSERT INTO " + weather.getLocation().getName() + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setString(1, weather.getTs());
            preparedStatement.setDouble(2, weather.getTemp());
            preparedStatement.setDouble(3, weather.getRain());
            preparedStatement.setInt(4, weather.getHumidity());
            preparedStatement.setDouble(5, weather.getClouds());
            preparedStatement.setDouble(6, weather.getWindSpeed());

            preparedStatement.execute();
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    private void createTable(Statement statement, String name) throws SQLException {
            statement.execute("CREATE TABLE IF NOT EXISTS " + name + " (" +
                    "Date TEXT," +
                    "Temperature REAL," +
                    "Rainfall REAL," +
                    "Humidity REAL," +
                    "Clouds REAL," +
                    "WindSpeed REAL" +
                    ");");
    }

    public Optional<Weather> loadWeather(Statement statement, Location location, Instant instant) {
        try {
            String selectDataSQL = "SELECT * FROM " + location.getName() + " WHERE date = ?";

            try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(selectDataSQL)) {
                preparedStatement.setObject(1, instant);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Weather loadedWeather = getWeather(location, instant, resultSet);
                    return Optional.of(loadedWeather);
                }
            }
        } catch (SQLException e) {e.printStackTrace();}
        return Optional.empty();
    }

    private static Weather getWeather(Location location, Instant instant, ResultSet resultSet) throws SQLException {
        double temperature = resultSet.getDouble("temperature");
        double rainfall = resultSet.getDouble("rain");
        int humidity = resultSet.getInt("humidity");
        int clouds = resultSet.getInt("clouds");
        double windSpeed = resultSet.getDouble("wind_speed");

        return new Weather(temperature, humidity, rainfall, windSpeed, clouds, location, instant.toString());
    }
}
