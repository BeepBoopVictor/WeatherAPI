package dacd.gil.control;

import dacd.gil.model.Weather;

import java.sql.*;

public class SQLiteWeatherStore implements WeatherStore{
    private final String path;
    public SQLiteWeatherStore(String dbpath) {
        this.path = dbpath;
    }

    @Override
    public void save(Weather weather){
        try {
            Statement statement = Connect();
            createTable(statement, weather.getLocation().getName());
            if (searchDate(weather.getLocation().getName(), statement, weather.getPredictionTime())){
                updateValue(statement, weather);
            } else {
                insert(statement, weather);
            }
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    private Statement Connect() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + this.path);
        return connection.createStatement();
    }

    public static boolean searchDate(String tableName, Statement statement, String date){
        try{
            ResultSet resultSet = statement.executeQuery("SELECT * from " + tableName);
            while(resultSet.next()){
                String columnValue = resultSet.getString(8);
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
        String query = "UPDATE " + weather.getLocation().getName() + " SET Temperature = ?, Rainfall = ?, Humidity = ?, Clouds = ?, WindSpeed = ?, SS = ?, PredictionTime = ? WHERE Date = ?";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query)) {
            preparedStatement.setDouble(2, weather.getTemp());
            preparedStatement.setDouble(3, weather.getRain());
            preparedStatement.setInt(4, weather.getHumidity());
            preparedStatement.setDouble(5, weather.getClouds());
            preparedStatement.setDouble(6, weather.getWindSpeed());
            preparedStatement.setString(7, weather.getSs());
            preparedStatement.setString(8, weather.getPredictionTime());
            preparedStatement.setString(1, weather.getTs());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
    }

    private void insert(Statement statement, Weather weather) {
        String insertQuery = "INSERT INTO " + weather.getLocation().getName() + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setString(1, weather.getTs());
            preparedStatement.setDouble(2, weather.getTemp());
            preparedStatement.setDouble(3, weather.getRain());
            preparedStatement.setInt(4, weather.getHumidity());
            preparedStatement.setDouble(5, weather.getClouds());
            preparedStatement.setDouble(6, weather.getWindSpeed());
            preparedStatement.setString(7, weather.getSs());
            preparedStatement.setString(8, weather.getPredictionTime());

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
                "WindSpeed REAL," +
                "SS TEXT," +
                "PredictionTime TEXT" +
                ");");
    }
}
