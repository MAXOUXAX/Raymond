package me.maxouxax.raymond.serversconfig;

import me.maxouxax.raymond.Raymond;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ServerConfig {

    @BsonProperty(value = "_id")
    @BsonId
    private String serverId;

    @BsonProperty(value = "archived")
    private boolean archived;

    /**
     * Default constructor for MongoDB
     */
    @SuppressWarnings("unused")
    public ServerConfig() {
    }

    public ServerConfig(String serverId, boolean archived) {
        this.serverId = serverId;
        this.archived = archived;
    }

    public static ServerConfig getDefault(String serverId){
        return new ServerConfig(serverId, false);
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId, boolean save){
        this.serverId = serverId;
        if(save) save();
    }

    public void setArchived(boolean archived, boolean save){
        this.archived = archived;
        if(save) save();
    }

    public boolean isArchived() {
        return archived;
    }

    /**
     * Do not use this method, use {@link ServerConfig#setServerId(String, boolean)} instead if you want to save the server config
     * @param serverId The server id
     */
    public void setServerId(String serverId) {
        setServerId(serverId, false);
    }

    /**
     * Do not use this method, use {@link ServerConfig#setServerId(String, boolean)} instead if you want to save the server config
     * @param archived {@code true} if the server is archived, {@code false} otherwise
     */
    public void setArchived(boolean archived) {
        setArchived(archived, false);
    }

    public void save(){
        Raymond.getInstance().getServerConfigsManager().saveServerConfig(this);
    }

}
