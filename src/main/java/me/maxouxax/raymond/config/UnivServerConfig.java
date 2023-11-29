package me.maxouxax.raymond.config;

import me.maxouxax.raymond.schedule.UnivSchedule;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class UnivServerConfig {

    @BsonProperty("univ_schedule")
    private UnivSchedule univSchedule;

    @BsonProperty("discord_forum_channel_id")
    private String discordForumChannelId;

    @BsonProperty("discord_forum_role_id")
    private String discordForumRoleId;

    @BsonProperty("crous_restaurant_name")
    private String crousRestaurantName;

    @BsonProperty("discord_crous_menu_channel_id")
    private String discordCrousMenuChannelId;

    @BsonProperty("discord_crous_menu_role_id")
    private String discordCrousMenuRoleId;

    public UnivServerConfig() {
        this.univSchedule = new UnivSchedule();
    }

    @BsonCreator
    public UnivServerConfig(@BsonProperty("univ_schedule") final UnivSchedule univSchedule,
                            @BsonProperty("discord_forum_channel_id") final String discordForumChannelId,
                            @BsonProperty("discord_forum_role_id") final String discordForumRoleId,
                            @BsonProperty("crous_restaurant_name") final String crousRestaurantName,
                            @BsonProperty("discord_crous_menu_channel_id") final String discordCrousMenuChannelId,
                            @BsonProperty("discord_crous_menu_role_id") final String discordCrousMenuRoleId
    ) {
        this.univSchedule = univSchedule;
        this.discordForumChannelId = discordForumChannelId;
        this.discordForumRoleId = discordForumRoleId;
        this.crousRestaurantName = crousRestaurantName;
        this.discordCrousMenuChannelId = discordCrousMenuChannelId;
        this.discordCrousMenuRoleId = discordCrousMenuRoleId;
    }

    public UnivSchedule getUnivSchedule() {
        return univSchedule;
    }

    public String getDiscordForumChannelId() {
        return discordForumChannelId;
    }

    public String getDiscordForumRoleId() {
        return discordForumRoleId;
    }

    public String getCrousRestaurantName() {
        return crousRestaurantName;
    }

    public String getDiscordCrousMenuChannelId() {
        return discordCrousMenuChannelId;
    }

    public String getDiscordCrousMenuRoleId() {
        return discordCrousMenuRoleId;
    }

    public void setUnivSchedule(UnivSchedule univSchedule) {
        this.univSchedule = univSchedule;
    }

    public void setDiscordForumChannelId(String discordForumChannelId) {
        this.discordForumChannelId = discordForumChannelId;
    }

    public void setDiscordForumRoleId(String id) {
        this.discordForumRoleId = id;
    }

    public void setCrousRestaurantName(String crousRestaurantName) {
        this.crousRestaurantName = crousRestaurantName;
    }

    public void setDiscordCrousMenuChannelId(String discordCrousMenuChannelId) {
        this.discordCrousMenuChannelId = discordCrousMenuChannelId;
    }

    public void setDiscordCrousMenuRoleId(String discordCrousMenuRoleId) {
        this.discordCrousMenuRoleId = discordCrousMenuRoleId;
    }
}
