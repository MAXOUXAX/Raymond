package me.maxouxax.raymond.config;

import me.maxouxax.supervisor.serversconfig.ServerConfig;
import me.maxouxax.supervisor.serversconfig.ServerConfigsManager;

public class RaymondServerConfigsManager extends ServerConfigsManager {

    public RaymondServerConfigsManager() {
        super("raymond");
    }

    @Override
    public <T extends ServerConfig> Class<T> getServerConfigImpl() {
        return (Class<T>) RaymondServerConfig.class;
    }

    @Override
    public ServerConfig getDefault(String serverId) {
        return new RaymondServerConfig(serverId, false);
    }

    @Override
    public RaymondServerConfig getServerConfig(String serverId) {
        return (RaymondServerConfig) super.getServerConfig(serverId);
    }
}
