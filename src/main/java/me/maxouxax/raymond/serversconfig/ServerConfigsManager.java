package me.maxouxax.raymond.serversconfig;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.database.DatabaseManager;
import me.maxouxax.raymond.database.Databases;
import me.maxouxax.raymond.database.nosql.DatabaseAccess;

import java.util.ArrayList;
import java.util.List;

public class ServerConfigsManager {

    private final Raymond raymond;
    private DatabaseAccess databaseAccess;
    private List<ServerConfig> serverConfigs;

    public ServerConfigsManager(Raymond raymond) {
        this.raymond = raymond;
        this.databaseAccess = (DatabaseAccess) DatabaseManager.getDatabaseAccess(Databases.MONGODB.getName());
        loadConfigs();
    }

    private void loadConfigs(){
        MongoClient mongoClient = this.databaseAccess.getMongoClient();
        MongoDatabase database = mongoClient.getDatabase("raymond");
        MongoCollection<ServerConfig> collection = database.getCollection("server_config", ServerConfig.class);
        this.serverConfigs = collection.find().into(new ArrayList<>());
    }

    public ServerConfig getServerConfig(String serverId){
        return serverConfigs.stream().filter(serverConfig -> serverConfig.getServerId().equals(serverId)).findFirst().orElse(ServerConfig.getDefault(serverId));
    }

    public void saveServerConfig(ServerConfig serverConfig){
        String serverId = serverConfig.getServerId();
        if(serverConfig.equals(ServerConfig.getDefault(serverId))) return;
        MongoClient mongoClient = this.databaseAccess.getMongoClient();
        MongoDatabase database = mongoClient.getDatabase("raymond");
        MongoCollection<ServerConfig> collection = database.getCollection("server_config", ServerConfig.class);
        if(collection.countDocuments(Filters.eq("server_id", serverId)) == 0) {
            collection.insertOne(serverConfig);
        } else {
            collection.replaceOne(Filters.eq("server_id", serverId), serverConfig);
        }
    }

    public void saveServerConfig(String serverId){
        ServerConfig serverConfig = getServerConfig(serverId);
        saveServerConfig(serverConfig);
    }

    public List<ServerConfig> getServerConfigs() {
        return serverConfigs;
    }

}
