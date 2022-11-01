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

    public UnivServerConfig() {
        this.univSchedule = new UnivSchedule();
    }

    @BsonCreator
    public UnivServerConfig(@BsonProperty("univ_schedule") final UnivSchedule univSchedule,
                            @BsonProperty("discord_forum_channel_id") final String discordForumChannelId,
                            @BsonProperty("discord_forum_role_id") final String discordForumRoleId) {
        this.univSchedule = univSchedule;
        this.discordForumChannelId = discordForumChannelId;
        this.discordForumRoleId = discordForumRoleId;
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

    public void setUnivSchedule(UnivSchedule univSchedule) {
        this.univSchedule = univSchedule;
    }

    public void setDiscordForumChannelId(String discordForumChannelId) {
        this.discordForumChannelId = discordForumChannelId;
    }

    public void setDiscordForumRoleId(String id) {
        this.discordForumRoleId = id;
    }

}
