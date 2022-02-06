package me.maxouxax.raymond.serversconfig;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
        return serverConfigs.stream().filter(serverConfig -> serverConfig.getServerId().equals(serverId)).findFirst().orElse(ServerConfig.DEFAULT);
    }

    public List<ServerConfig> getServerConfigs() {
        return serverConfigs;
    }
}
