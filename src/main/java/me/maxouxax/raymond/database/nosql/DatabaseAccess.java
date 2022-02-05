package me.maxouxax.raymond.database.nosql;

import com.mongodb.client.MongoClient;
import me.maxouxax.raymond.database.DatabaseCredentials;
import me.maxouxax.raymond.database.IDatabaseAccess;

public class DatabaseAccess implements IDatabaseAccess {

    private final DatabaseCredentials databaseCredentials;
    private MongoClient mongoClient;

    public DatabaseAccess(DatabaseCredentials databaseCredentials) {
        this.databaseCredentials = databaseCredentials;
    }

    private void setupMongo() {
        this.mongoClient = null;
    }

    @Override
    public String getType() {
        return databaseCredentials.getType();
    }

    @Override
    public void initPool(){
        setupMongo();
    }

    @Override
    public void closePool(){
        this.mongoClient.close();
    }

    public MongoClient getConnection() {
        return this.mongoClient;
    }

}
