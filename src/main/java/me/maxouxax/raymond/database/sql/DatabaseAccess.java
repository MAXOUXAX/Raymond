package me.maxouxax.raymond.database.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.maxouxax.raymond.database.DatabaseCredentials;
import me.maxouxax.raymond.database.IDatabaseAccess;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseAccess implements IDatabaseAccess {

    private final DatabaseCredentials databaseCredentials;
    private HikariDataSource hikariDataSource;

    public DatabaseAccess(DatabaseCredentials databaseCredentials) {
        this.databaseCredentials = databaseCredentials;
    }

    private void setupHikariCP(){
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setJdbcUrl(databaseCredentials.toURI());
        hikariConfig.setDriverClassName(org.mariadb.jdbc.Driver.class.getName());
        hikariConfig.setUsername(databaseCredentials.getUser());
        hikariConfig.setPassword(databaseCredentials.getPassword());
        hikariConfig.setMaxLifetime(300*1000);
        hikariConfig.setIdleTimeout(28*1000);
        hikariConfig.setLeakDetectionThreshold(60*1000);
        hikariConfig.setConnectionTimeout(10*1000);

        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public String getType() {
        return databaseCredentials.getType();
    }

    @Override
    public void initPool(){
        setupHikariCP();
    }

    @Override
    public void closePool(){
        this.hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

}
