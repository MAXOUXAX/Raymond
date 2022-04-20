package me.maxouxax.raymond.config;

import me.maxouxax.supervisor.supervised.Config;

public class RaymondConfig extends Config {

    private UnivConfig univConfig;

    public RaymondConfig() {
        super();
    }

    public UnivConfig getUnivConfig() {
        return univConfig;
    }

    public void setUnivConfig(UnivConfig univConfig) {
        this.univConfig = univConfig;
    }

}
