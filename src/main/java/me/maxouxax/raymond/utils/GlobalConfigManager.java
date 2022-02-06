package me.maxouxax.raymond.utils;

import me.maxouxax.raymond.database.DatabaseManager;
import me.maxouxax.raymond.database.Databases;
import me.maxouxax.raymond.database.sql.DatabaseAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GlobalConfigManager {

    private final Map<String, String> configKeys = new HashMap<>();

    public GlobalConfigManager() {
        try {
            loadData();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void loadData() throws SQLException {
        DatabaseAccess databaseAccess = (DatabaseAccess) DatabaseManager.getDatabaseAccess(Databases.MARIADB.getName());
        Connection connection = databaseAccess.getConnection();
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
