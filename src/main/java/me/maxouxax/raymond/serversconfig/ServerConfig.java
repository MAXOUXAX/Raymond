package me.maxouxax.raymond.serversconfig;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.jda.pojos.ChannelPermission;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;
import java.util.List;

public class ServerConfig {

    @BsonProperty(value = "_id")
    @BsonId
    private String serverId;

    @BsonProperty(value = "archived")
    private boolean archived;

    @BsonProperty(value = "permission_before_archive")
    private HashMap<String, List<ChannelPermission>> permissionBeforeArchive;

    /**
     * Default constructor for MongoDB
     */
    @SuppressWarnings("unused")
    public ServerConfig() {
    }

    public ServerConfig(String serverId, boolean archived) {
        this.serverId = serverId;
        this.archived = archived;
        this.permissionBeforeArchive = new HashMap<>();
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

    /**
     * Do not use this method, use {@link ServerConfig#setServerId(String, boolean)} instead if you want to save the server config
     * @param serverId The server id
     */
    public void setServerId(String serverId) {
        setServerId(serverId, false);
    }

    public void setArchived(boolean archived, boolean save){
        this.archived = archived;
        if(save) save();
    }

    /**
     * Do not use this method, use {@link ServerConfig#setServerId(String, boolean)} instead if you want to save the server config
     * @param archived {@code true} if the server is archived, {@code false} otherwise
     */
    public void setArchived(boolean archived) {
        setArchived(archived, false);
    }

    public boolean isArchived() {
        return archived;
    }

    public HashMap<String, List<ChannelPermission>> getPermissionBeforeArchive() {
        return permissionBeforeArchive;
    }

    public void setPermissionBeforeArchive(HashMap<String, List<ChannelPermission>> permissionBeforeArchive, boolean save){
        this.permissionBeforeArchive = permissionBeforeArchive;
        if(save) save();
    }

    public void setPermissionBeforeArchive(HashMap<String, List<ChannelPermission>> permissionBeforeArchive) {
        setPermissionBeforeArchive(permissionBeforeArchive, false);
    }

    public void save(){
        Raymond.getInstance().getServerConfigsManager().saveServerConfig(this);
    }

}
