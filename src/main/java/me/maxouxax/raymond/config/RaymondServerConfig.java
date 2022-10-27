package me.maxouxax.raymond.config;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.schedule.UnivSchedule;
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
                               @BsonProperty("archived") final boolean archived,
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

    public RaymondServerConfig(String serverId, boolean archived) {
        this(serverId, new HashMap<>(), archived, new HashMap<>(), "", "", "", "", "", new UnivServerConfig());
    }

    @Override
    public ServerConfig getDefault(String serverId) {
        return new RaymondServerConfig(serverId, false);
    }

    public void save() {
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

    /**
     * Regular getters
     */

    @Override
    public String getServerId() {
        return serverId;
    }

    public Map<String, Long> getUsersPower() {
        return usersPower;
    }

    public boolean isArchived() {
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

    /**
     * Regular setters (saving enabled)
     */

    @BsonIgnore
    public void setServerId(String serverId) {
        this.serverId = serverId;
        save();
    }

    @BsonIgnore
    public void setUsersPower(Map<String, Long> usersPower) {
        this.usersPower = usersPower;
        save();
    }

    @BsonIgnore
    public void setArchived(Boolean archived) {
        this.archived = archived;
        save();
    }

    @BsonIgnore
    public void setPermissionBeforeArchive(HashMap<String, List<ChannelPermission>> permissionBeforeArchive) {
        this.permissionBeforeArchive = permissionBeforeArchive;
        save();
    }

    @BsonIgnore
    public void setRulesBanner(String rulesBanner) {
        this.rulesBanner = rulesBanner;
        save();
    }

    @BsonIgnore
    public void setRulesThumbnail(String rulesThumbnail) {
        this.rulesThumbnail = rulesThumbnail;
        save();
    }

    @BsonIgnore
    public void setRulesModerationThumbnail(String rulesModerationThumbnail) {
        this.rulesModerationThumbnail = rulesModerationThumbnail;
        save();
    }

    @BsonIgnore
    public void setRulesAttentionThumbnail(String rulesAttentionThumbnail) {
        this.rulesAttentionThumbnail = rulesAttentionThumbnail;
        save();
    }

    @BsonIgnore
    public void setRulesTextChannelId(String rulesTextChannelId) {
        this.rulesTextChannelId = rulesTextChannelId;
        save();
    }

    @BsonIgnore
    public void setUserPower(User user, long power) {
        usersPower.put(user.getId(), power);
        save();
    }

    @BsonIgnore
    public void updateUnivSchedule(UnivSchedule univSchedule){
        if(this.univServerConfig != null) this.univServerConfig.setUnivSchedule(univSchedule);
        save();
    }

}
