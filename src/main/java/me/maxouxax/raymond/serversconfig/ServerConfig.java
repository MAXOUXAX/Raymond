package me.maxouxax.raymond.serversconfig;

import me.maxouxax.raymond.Raymond;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ServerConfig {

    @BsonProperty(value = "server_id")
    private String serverId;

    @BsonProperty(value = "archived")
    private Boolean archived;

    /**
     * Default constructor for MongoDB
     */
    @SuppressWarnings("unused")
    public ServerConfig() {
    }

    public ServerConfig(String serverId, Boolean archived) {
        this.serverId = serverId;
        this.archived = archived;
    }

    public static ServerConfig getDefault(String serverId){
        return new ServerConfig(serverId, false);
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
        save();
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
        save();
    }

    public void save(){
        Raymond.getInstance().getServerConfigsManager().saveServerConfig(this);
    }

}
