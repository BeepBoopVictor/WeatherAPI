package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;
import dacd.gil.model.HotelPriceWeather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;


public class dataMartSQL implements storeInterface {
    private String path;
    public dataMartSQL(String dbPath) {
        this.path = dbPath;
    }

    @Override
    public void save(Map<String, String> objectMap) throws CustomException{
        try {
            Statement statement = Connect();
            createTable(statement, objectMap.get("Location"));
            if (searchDate(weather.getLocation().getName(), statement, weather.getTs())){
                updateValue(statement, weather);
            } else {
                insert(statement, weather);
            }
        } catch (SQLException e) {
            throw new CustomException("Error in save function", e);
        }
    }

    private void createTable(Statement statement, String tableName){

    }

    private Statement Connect() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + this.path);
        return connection.createStatement();
    }

}
