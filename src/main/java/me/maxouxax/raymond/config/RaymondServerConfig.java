package me.maxouxax.raymond.config;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.jda.pojos.ChannelPermission;
import me.maxouxax.supervisor.serversconfig.ServerConfig;
import me.maxouxax.supervisor.serversconfig.ServerConfigsManager;
import net.dv8tion.jda.api.entities.User;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaymondServerConfig extends ServerConfig {

    @BsonProperty(value = "_id")
    @BsonId
    String serverId;

    @BsonProperty(value = "usersPower")
    Map<String, Long> usersPower = new HashMap<>();

    @BsonProperty(value = "archived")
    private boolean archived;

    @BsonProperty(value = "permission_before_archive")
    private HashMap<String, List<ChannelPermission>> permissionBeforeArchive;

    @BsonProperty(value = "rules_banner")
    private String rulesBanner;

    @BsonProperty(value = "rules_thumbnail")
    private String rulesThumbnail;

    @BsonProperty(value = "rules_moderation_thumbnail")
    private String rulesModerationThumbnail;

    @BsonProperty(value = "rules_attention_thumbnail")
    private String rulesAttentionThumbnail;

    @BsonProperty(value = "rules_textchannel_id")
    private String rulesTextChannelId;

    /**
     * Default constructor for MongoDB
     */
    @SuppressWarnings("unused")
    public RaymondServerConfig() {
        super();
    }

    public RaymondServerConfig(String serverId, boolean archived) {
        super(serverId);
        this.serverId = serverId;
        this.archived = archived;
        this.permissionBeforeArchive = new HashMap<>();
        this.rulesBanner = "";
        this.rulesThumbnail = "";
        this.rulesModerationThumbnail = "";
        this.rulesAttentionThumbnail = "";
        this.rulesTextChannelId = "";
    }

    public ServerConfig getDefault(String serverId, ServerConfigsManager serverConfigsManager) {
        return new RaymondServerConfig(serverId, false);
    }

    public Map<String, Long> getUsersPower() {
        return usersPower;
    }

    /**
     * Do not use this method, use {@link RaymondServerConfig#setUsersPower(Map, boolean)}  instead if you want to save the server config
     *
     * @param usersPower The UsersPower object representing users power
     */
    public void setUsersPower(Map<String, Long> usersPower) {
        setUsersPower(usersPower, false);
    }

    @Override
    public long getPowerFromUser(String userId) {
        return usersPower.getOrDefault(userId, 0L);
    }

    public ServerConfigsManager getServerConfigsManager() {
        return Raymond.getInstance().getServerConfigsManager();
    }

    public void setServerId(String serverId, boolean save) {
        super.setServerId(serverId);
        this.serverId = serverId;
        if (save) save();
    }

    /**
     * Do not use this method, use {@link RaymondServerConfig#setServerId(String, boolean)} instead if you want to save the server config
     *
     * @param serverId The server id
     */
    public void setServerId(String serverId) {
        setServerId(serverId, false);
    }

    public void setUsersPower(Map<String, Long> usersPower, boolean save) {
        this.usersPower = usersPower;
        if (save) save();
    }

    public void setUserPower(User user, long power, boolean save) {
        usersPower.put(user.getId(), power);
        if (save) save();
    }

    public void save() {
        getServerConfigsManager().saveServerConfig(this);
    }

    public void setArchived(boolean archived, boolean save) {
        this.archived = archived;
        if (save) save();
    }

    public HashMap<String, List<ChannelPermission>> getPermissionBeforeArchive() {
        return permissionBeforeArchive;
    }

    public void setPermissionBeforeArchive(HashMap<String, List<ChannelPermission>> permissionBeforeArchive) {
        setPermissionBeforeArchive(permissionBeforeArchive, false);
    }

    public void setPermissionBeforeArchive(HashMap<String, List<ChannelPermission>> permissionBeforeArchive, boolean save) {
        this.permissionBeforeArchive = permissionBeforeArchive;
        if (save) save();
    }

    public void setRulesBanner(String rulesBanner, boolean save) {
        this.rulesBanner = rulesBanner;
        if (save) save();
    }

    public void setRulesThumbnail(String rulesThumbnail, boolean save) {
        this.rulesThumbnail = rulesThumbnail;
        if (save) save();
    }

    public void setRulesAttentionThumbnail(String rulesAttentionThumbnail, boolean save) {
        this.rulesAttentionThumbnail = rulesAttentionThumbnail;
        if (save) save();
    }

    public void setRulesModerationThumbnail(String rulesModerationThumbnail, boolean save) {
        this.rulesModerationThumbnail = rulesModerationThumbnail;
        if (save) save();
    }

    public void setRulesTextChannelId(String rulesTextChannelId, boolean save) {
        this.rulesTextChannelId = rulesTextChannelId;
        if (save) save();
    }

    public boolean isArchived() {
        return archived;
    }

    /**
     * Do not use this method, use {@link RaymondServerConfig#setServerId(String, boolean)} instead if you want to save the server config
     *
     * @param archived {@code true} if the server is archived, {@code false} otherwise
     */
    public void setArchived(boolean archived) {
        setArchived(archived, false);
    }

    public String getRulesBanner() {
        return rulesBanner;
    }

    public void setRulesBanner(String rulesBanner) {
        setRulesBanner(rulesBanner, false);
    }

    public String getRulesThumbnail() {
        return rulesThumbnail;
    }

    public void setRulesThumbnail(String rulesThumbnail) {
        setRulesThumbnail(rulesThumbnail, false);
    }

    public String getRulesModerationThumbnail() {
        return rulesModerationThumbnail;
    }

    public void setRulesModerationThumbnail(String rulesModerationThumbnail) {
        setRulesModerationThumbnail(rulesModerationThumbnail, false);
    }

    public String getRulesAttentionThumbnail() {
        return rulesAttentionThumbnail;
    }

    public void setRulesAttentionThumbnail(String rulesAttentionThumbnail) {
        setRulesAttentionThumbnail(rulesAttentionThumbnail, false);
    }

    public String getRulesTextChannelId() {
        return rulesTextChannelId;
    }

    public void setRulesTextChannelId(String rulesTextChannelId) {
        setRulesTextChannelId(rulesTextChannelId, false);
    }
}
