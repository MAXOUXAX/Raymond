package me.maxouxax.raymond.database.nosql;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import me.maxouxax.raymond.database.DatabaseCredentials;
import me.maxouxax.raymond.database.IDatabaseAccess;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DatabaseAccess implements IDatabaseAccess {

    private final DatabaseCredentials databaseCredentials;
    private MongoClient mongoClient;

    public DatabaseAccess(DatabaseCredentials databaseCredentials) {
        this.databaseCredentials = databaseCredentials;
    }

    private void setupMongo() {
        ConnectionString connectionString = new ConnectionString(databaseCredentials.toURI());
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        this.mongoClient = MongoClients.create(clientSettings);
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

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

}
