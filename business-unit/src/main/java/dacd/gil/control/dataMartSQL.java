package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class dataMartSQL implements storeInterface {
    private String path;
    private Statement statement;
    public dataMartSQL(String dbPath) throws SQLException {
        this.path = dbPath;
        this.statement = Connect();
    }

    @Override
    public void save(Map<String, String> objectMap) throws CustomException{
        try {
            createTable(statement, objectMap.get("location"));

            if(objectMap.get("object").equals("Hotel")){
                if(!searchDate(statement, objectMap)){
                    insertHotel(statement, objectMap);
                } else {
                    if(isHotelEmpty(statement, objectMap)){
                        updateHotel(statement, objectMap);
                    }else{
                        Map<String, String> fillWithWeather = new HashMap<>();
                        fillWithWeather = weatherMapFiller(statement, objectMap);
                        insertHotel(statement, objectMap);
                        updateWeather(statement, fillWithWeather);
                    }
                }
            } else {
                if(searchDate(statement, objectMap)){
                    updateWeather(statement, objectMap);
                }else{
                    insertWeather(statement, objectMap);
                }
            }
        } catch (SQLException e) {
            throw new CustomException("Error in save function", e);
        }
    }

    private boolean isHotelEmpty(Statement statement, Map<String, String> objectMap) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * from " + objectMap.get("location"));
        while(resultSet.next()){
            String columnValue = resultSet.getString(2);
            String hotelKeyColumn = resultSet.getString(1);
            if (objectMap.get("day").equals(columnValue)){
                if(hotelKeyColumn == null){
                    resultSet.close();
                    return true;
                }
            }
        }
        resultSet.close();
        return false;
    }

    private Map<String, String> weatherMapFiller(Statement statement, Map<String, String> objectMap) throws SQLException {
        Map<String, String> returnWeatherMap = new HashMap<>();
        ResultSet resultSet = statement.executeQuery("SELECT * from " + objectMap.get("location"));
        while(resultSet.next()){
            String columnValue = resultSet.getString(2);
            if (objectMap.get("day").equals(columnValue) && resultSet.getString(4) != null){
                returnWeatherMap.put("Temperature", resultSet.getString(4));
                returnWeatherMap.put("Rain", resultSet.getString(5));
                returnWeatherMap.put("Humidity", resultSet.getString(6));
                returnWeatherMap.put("Clouds", resultSet.getString(7));
                returnWeatherMap.put("WindSpeed", resultSet.getString(8));
                returnWeatherMap.put("location", objectMap.get("location"));
                returnWeatherMap.put("day", objectMap.get("day"));
            }
        }
        resultSet.close();
        return returnWeatherMap;
    }

    private void updateWeather(Statement statement, Map<String, String> objectMap) throws CustomException {
        String query = "UPDATE " + objectMap.get("location") + " SET Temperature = ?, Rainfall = ?, Humidity = ?, Clouds = ?, WindSpeed = ? WHERE Date = ?";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query)) {
            preparedStatement.setDouble(1, Double.parseDouble(objectMap.get("Temperature")));
            preparedStatement.setDouble(2, Double.parseDouble(objectMap.get("Rain")));
            preparedStatement.setDouble(3, Double.parseDouble(objectMap.get("Humidity")));
            preparedStatement.setDouble(4, Double.parseDouble(objectMap.get("Clouds")));
            preparedStatement.setDouble(5, Double.parseDouble(objectMap.get("WindSpeed")));
            preparedStatement.setString(6, objectMap.get("day"));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Error updating weather", e);
        }
    }

    private void updateHotel(Statement statement, Map<String, String> objectMap) throws CustomException {
        String query = "UPDATE " + objectMap.get("location") + " SET HotelKey = ?, PriceStatus = ? WHERE Date = ?";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, objectMap.get("hotelKey"));
            preparedStatement.setString(2, objectMap.get("priceStatus"));
            preparedStatement.setString(3, objectMap.get("day"));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Error updating Hotel", e);
        }
    }

    private void insertHotel(Statement statement, Map<String, String> objectMap) throws CustomException {
        String insertQuery = "INSERT INTO " + objectMap.get("location") + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setString(1, objectMap.get("hotelKey"));
            preparedStatement.setString(2, objectMap.get("day"));
            preparedStatement.setString(3, objectMap.get("priceStatus"));
            preparedStatement.setNull(4, Types.DOUBLE);
            preparedStatement.setNull(5, Types.DOUBLE);
            preparedStatement.setNull(6, Types.DOUBLE);
            preparedStatement.setNull(7, Types.DOUBLE);
            preparedStatement.setNull(8, Types.DOUBLE);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new CustomException("Error inserting hotel", e);
        }
    }

    private void insertWeather(Statement statement, Map<String, String> objectMap) throws CustomException {
        String insertQuery = "INSERT INTO " + objectMap.get("location") + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setNull(1, Types.VARCHAR);
            preparedStatement.setString(2, objectMap.get("day"));
            preparedStatement.setNull(3, Types.VARCHAR);
            preparedStatement.setDouble(4, Double.parseDouble(objectMap.get("Temperature")));
            preparedStatement.setDouble(5, Double.parseDouble(objectMap.get("Rain")));
            preparedStatement.setDouble(6, Double.parseDouble(objectMap.get("Humidity")));
            preparedStatement.setDouble(7, Double.parseDouble(objectMap.get("Clouds")));
            preparedStatement.setDouble(8, Double.parseDouble(objectMap.get("WindSpeed")));
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new CustomException("Error inserting weather", e);
        }
    }

    private boolean findHotel(Statement statement, Map<String, String> objectMap) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * from " + objectMap.get("location"));
        while(resultSet.next()){
            String hotelKey = resultSet.getString(1);
            System.out.println(hotelKey + " " + objectMap.get("hotelKey"));
            String date = resultSet.getString(2);
            System.out.println(date + " " + objectMap.get("day"));
            if (objectMap.get("hotelKey").equals(hotelKey) && objectMap.get("day").equals(date)){
                System.out.println("TRUE");
                resultSet.close();
                return true;
            }
        }
        resultSet.close();
        return false;
    }

    private void createTable(Statement statement, String tableName) throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "HotelKey TEXT," +
                "Date TEXT," +
                "PriceStatus TEXT," +
                "Temperature REAL," +
                "Rainfall REAL," +
                "Humidity REAL," +
                "Clouds REAL," +
                "WindSpeed REAL" +
                ");");
    }

    private boolean searchDate(Statement statement, Map<String, String> objectMap) throws SQLException{
        ResultSet resultSet = statement.executeQuery("SELECT * from " + objectMap.get("location"));
        while(resultSet.next()){
            String columnValue = resultSet.getString(2);
            if (objectMap.get("day").equals(columnValue)){
                resultSet.close();
                return true;
            }
        }
        resultSet.close();
        return false;
    }

    private Statement Connect() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + this.path);
        return connection.createStatement();
    }

}
