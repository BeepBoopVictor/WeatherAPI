package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;

public class SQLiteWeatherStore implements WeatherStore {
    private int count = 0;

    public SQLiteWeatherStore() {
    }

    @Override
    public void save(Weather weather, Statement statement){
        try {
            createTable(statement, weather.location.name);
            if(getRecordCount(weather.location.name, statement) < 5){
                insert(statement, weather);
            } else {
                count++;
                System.out.println(count);
                if (count < 5){
                    updateValue(statement, weather);
                } else {
                    insert(statement, weather);
                    count = 0;
                }
            }
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    public static int getRecordCount(String tableName, Statement statement) {
        int recordCount = -1;
        try{
            String query = "SELECT COUNT(*) FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                recordCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {e.printStackTrace();}
        return recordCount;
    }

    public void updateValue(Statement statement, Weather weather) {
        String query = "UPDATE " + weather.location.name + " SET Temperature = ?, Rainfall = ?, Humidity = ?, Clouds = ?, WindSpeed = ? WHERE Date = ?";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query)) {
            preparedStatement.setDouble(2, weather.temp);
            preparedStatement.setDouble(3, weather.rain);
            preparedStatement.setInt(4, weather.humidity);
            preparedStatement.setDouble(5, weather.clouds);
            preparedStatement.setDouble(6, weather.windSpeed);
            preparedStatement.setString(1, weather.ts);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
    }

    private void insert(Statement statement, Weather weather) {
        String insertQuery = "INSERT INTO " + weather.location.name + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setString(1, weather.ts);
            preparedStatement.setDouble(2, weather.temp);
            preparedStatement.setDouble(3, weather.rain);
            preparedStatement.setInt(4, weather.humidity);
            preparedStatement.setDouble(5, weather.clouds);
            preparedStatement.setDouble(6, weather.windSpeed);

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

    public Optional<Weather> loadWeather(Location location, Instant instant) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql:dacd/gil/stuff/database.db")) {
            String selectDataSQL = "SELECT * FROM " + location.name + " WHERE date = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectDataSQL)) {
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
