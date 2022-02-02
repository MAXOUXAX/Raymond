package me.maxouxax.raymond.utils;

import me.maxouxax.raymond.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {

    private final Map<String, String> configKeys = new HashMap<>();

    public ConfigurationManager() {
        try {
            loadData();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void loadData() throws SQLException {
        Connection connection = DatabaseManager.getDatabaseAccess().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM settings");

        final ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            String key = resultSet.getString("key");
            String value = resultSet.getString("value");
            configKeys.put(key, value);
        }
        connection.close();
    }

    public String getStringValue(String key){
        return configKeys.getOrDefault(key, "");
    }

    public Long getLongValue(String key){
        return Long.valueOf(configKeys.getOrDefault(key, "0"));
    }

}
