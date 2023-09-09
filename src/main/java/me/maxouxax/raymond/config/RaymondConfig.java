package me.maxouxax.raymond.config;

import me.maxouxax.multi4j.MultiConfig;
import me.maxouxax.supervisor.supervised.Config;

public class RaymondConfig extends Config {

    private MultiConfig multiConfig;

    public RaymondConfig() {
        super();
    }

    public MultiConfig getMultiConfig() {
        return multiConfig;
    }

    public void setMultiConfig(MultiConfig multiConfig) {
        this.multiConfig = multiConfig;
    }

}
