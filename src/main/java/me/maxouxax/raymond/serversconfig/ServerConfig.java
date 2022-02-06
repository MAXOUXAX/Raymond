package me.maxouxax.raymond.serversconfig;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class ServerConfig {

    public static final ServerConfig DEFAULT = new ServerConfig("0");
    @BsonProperty(value = "server_id")
    private String serverId;

    public ServerConfig() {
    }

    public ServerConfig(String serverId) {
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

}
