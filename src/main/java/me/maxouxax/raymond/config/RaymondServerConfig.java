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

    private String serverId;
    private Map<String, Long> usersPower = new HashMap<>();
    private boolean archived;
    private HashMap<String, List<ChannelPermission>> permissionBeforeArchive;
    private String rulesBanner;
    private String rulesThumbnail;
    private String rulesModerationThumbnail;
    private String rulesAttentionThumbnail;
    private String rulesTextChannelId;

    /**
     * Constructor for MongoDB
     */
    @BsonCreator
    public RaymondServerConfig(@BsonProperty("_id") @BsonId final String serverId,
                               @BsonProperty("archived") final boolean archived,
                               @BsonProperty("permission_before_archive") final HashMap<String, List<ChannelPermission>> permissionBeforeArchive,
                               @BsonProperty("rules_banner") final String rulesBanner,
                               @BsonProperty("rules_thumbnail") final String rulesThumbnail,
                               @BsonProperty("rules_moderation_thumbnail") final String rulesModerationThumbnail,
                               @BsonProperty("rules_attention_thumbnail") final String rulesAttentionThumbnail,
                               @BsonProperty("rules_textchannel_id") final String rulesTextChannelId) {
        this.serverId = serverId;
        this.archived = archived;
        this.permissionBeforeArchive = permissionBeforeArchive;
        this.rulesBanner = rulesBanner;
        this.rulesThumbnail = rulesThumbnail;
        this.rulesModerationThumbnail = rulesModerationThumbnail;
        this.rulesAttentionThumbnail = rulesAttentionThumbnail;
        this.rulesTextChannelId = rulesTextChannelId;
    }

    public RaymondServerConfig(String serverId, boolean archived) {
        this(serverId, archived, new HashMap<>(), "", "", "", "", "");
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

    /**
     * Regular setters (saving enabled)
     */

    public void setServerId(String serverId) {
        this.serverId = serverId;
        save();
    }

    public void setUsersPower(Map<String, Long> usersPower) {
        this.usersPower = usersPower;
        save();
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
        save();
    }

    public void setPermissionBeforeArchive(HashMap<String, List<ChannelPermission>> permissionBeforeArchive) {
        this.permissionBeforeArchive = permissionBeforeArchive;
        save();
    }

    public void setRulesBanner(String rulesBanner) {
        this.rulesBanner = rulesBanner;
        save();
    }

    public void setRulesThumbnail(String rulesThumbnail) {
        this.rulesThumbnail = rulesThumbnail;
        save();
    }

    public void setRulesModerationThumbnail(String rulesModerationThumbnail) {
        this.rulesModerationThumbnail = rulesModerationThumbnail;
        save();
    }

    public void setRulesAttentionThumbnail(String rulesAttentionThumbnail) {
        this.rulesAttentionThumbnail = rulesAttentionThumbnail;
        save();
    }

    public void setRulesTextChannelId(String rulesTextChannelId) {
        this.rulesTextChannelId = rulesTextChannelId;
        save();
    }

    public void setUserPower(User user, long power) {
        usersPower.put(user.getId(), power);
        save();
    }

}
