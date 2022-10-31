package me.maxouxax.raymond.config;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.jda.pojos.ChannelPermission;
import me.maxouxax.supervisor.serversconfig.ServerConfig;
import me.maxouxax.supervisor.serversconfig.ServerConfigsManager;
import net.dv8tion.jda.api.entities.User;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaymondServerConfig extends ServerConfig {

    @BsonId
    @BsonProperty("_id")
    private String serverId;

    @BsonProperty("users_power")
    private Map<String, Long> usersPower;

    @BsonProperty("archived")
    private Boolean archived;

    @BsonProperty("permission_before_archive")
    private HashMap<String, List<ChannelPermission>> permissionBeforeArchive;

    @BsonProperty("rules_banner")
    private String rulesBanner;

    @BsonProperty("rules_thumbnail")
    private String rulesThumbnail;

    @BsonProperty("rules_moderation_thumbnail")
    private String rulesModerationThumbnail;

    @BsonProperty("rules_attention_thumbnail")
    private String rulesAttentionThumbnail;

    @BsonProperty("rules_textchannel_id")
    private String rulesTextChannelId;

    @BsonProperty("univ_config")
    private UnivServerConfig univServerConfig;

    /**
     * Constructor for MongoDB
     */
    @BsonCreator
    public RaymondServerConfig(@BsonProperty("_id") final String serverId,
                               @BsonProperty("users_power") final Map<String, Long> usersPower,
                               @BsonProperty("archived") final Boolean archived,
                               @BsonProperty("permission_before_archive") final HashMap<String, List<ChannelPermission>> permissionBeforeArchive,
                               @BsonProperty("rules_banner") final String rulesBanner,
                               @BsonProperty("rules_thumbnail") final String rulesThumbnail,
                               @BsonProperty("rules_moderation_thumbnail") final String rulesModerationThumbnail,
                               @BsonProperty("rules_attention_thumbnail") final String rulesAttentionThumbnail,
                               @BsonProperty("rules_textchannel_id") final String rulesTextChannelId,
                               @BsonProperty("univ_config") final UnivServerConfig univServerConfig) {
        this.serverId = serverId;
        this.usersPower = usersPower == null ? new HashMap<>() : usersPower;
        this.archived = archived;
        this.permissionBeforeArchive = permissionBeforeArchive;
        this.rulesBanner = rulesBanner;
        this.rulesThumbnail = rulesThumbnail;
        this.rulesModerationThumbnail = rulesModerationThumbnail;
        this.rulesAttentionThumbnail = rulesAttentionThumbnail;
        this.rulesTextChannelId = rulesTextChannelId;
        this.univServerConfig = univServerConfig;
    }

    public RaymondServerConfig(String serverId, Boolean archived) {
        this(serverId, new HashMap<>(), archived, new HashMap<>(), "", "", "", "", "", new UnivServerConfig());
    }

    @Override
    public ServerConfig getDefault(String serverId) {
        return new RaymondServerConfig(serverId, false);
    }

    public void saveConfig() {
        getServerConfigsManager().saveServerConfig(this);
    }

    @Override
    public long getPowerFromUser(String userId) {
        return usersPower.getOrDefault(userId, 0L);
    }

    @BsonIgnore
    public ServerConfigsManager getServerConfigsManager() {
        return Raymond.getInstance().getServerConfigsManager();
    }

    @Override
    public String getServerId() {
        return serverId;
    }

    public Map<String, Long> getUsersPower() {
        return usersPower;
    }

    public Boolean isArchived() {
        return archived;
    }

    public HashMap<String, List<ChannelPermission>> getPermissionBeforeArchive() {
        return permissionBeforeArchive;
    }

    public String getRulesBanner() {
        return rulesBanner;
    }

    public String getRulesThumbnail() {
        return rulesThumbnail;
    }

    public String getRulesModerationThumbnail() {
        return rulesModerationThumbnail;
    }

    public String getRulesAttentionThumbnail() {
        return rulesAttentionThumbnail;
    }

    public String getRulesTextChannelId() {
        return rulesTextChannelId;
    }

    public UnivServerConfig getUnivServerConfig() {
        return univServerConfig;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setUsersPower(Map<String, Long> usersPower) {
        this.usersPower = usersPower;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public void setPermissionBeforeArchive(HashMap<String, List<ChannelPermission>> permissionBeforeArchive) {
        this.permissionBeforeArchive = permissionBeforeArchive;
    }

    public void setRulesBanner(String rulesBanner) {
        this.rulesBanner = rulesBanner;
    }

    public void setRulesThumbnail(String rulesThumbnail) {
        this.rulesThumbnail = rulesThumbnail;
    }

    public void setRulesModerationThumbnail(String rulesModerationThumbnail) {
        this.rulesModerationThumbnail = rulesModerationThumbnail;
    }

    public void setRulesAttentionThumbnail(String rulesAttentionThumbnail) {
        this.rulesAttentionThumbnail = rulesAttentionThumbnail;
    }

    public void setRulesTextChannelId(String rulesTextChannelId) {
        this.rulesTextChannelId = rulesTextChannelId;
    }

    public void setUserPower(User user, long power) {
        usersPower.put(user.getId(), power);
    }

}
