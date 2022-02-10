package me.maxouxax.raymond.jda.pojos;

import net.dv8tion.jda.api.entities.PermissionOverride;

/**
 * POJO for the {@link PermissionOverride} interface
 */
public class ChannelPermission {

    long allowedRaw;
    long deniedRaw;
    boolean isMemberPermission;
    String channelId;
    String guildId;
    String holderId;

    public ChannelPermission() {
    }

    public ChannelPermission(long allowedRaw, long deniedRaw, boolean isMemberPermission, String channelId, String guildId, String holderId) {
        this.allowedRaw = allowedRaw;
        this.deniedRaw = deniedRaw;
        this.isMemberPermission = isMemberPermission;
        this.channelId = channelId;
        this.guildId = guildId;
        this.holderId = holderId;
    }

    public ChannelPermission(PermissionOverride permissionOverride) {
        this.allowedRaw = permissionOverride.getAllowedRaw();
        this.deniedRaw = permissionOverride.getDeniedRaw();
        this.isMemberPermission = permissionOverride.isMemberOverride();
        this.channelId = permissionOverride.getChannel().getId();
        this.guildId = permissionOverride.getGuild().getId();
        this.holderId = permissionOverride.getPermissionHolder().getId();
    }

    /**
     * This is the raw binary representation (as a base 10 long) of the permissions allowed by this override.
     * @return Never-negative long containing the binary representation of the allowed permissions of this override.
     */
    public long getAllowedRaw() {
        return allowedRaw;
    }

    public void setAllowedRaw(long allowedRaw) {
        this.allowedRaw = allowedRaw;
    }

    /**
     * This is the raw binary representation (as a base 10 long) of the permissions denied by this override.
     * @return Never-negative long containing the binary representation of the denied permissions of this override.
     */
    public long getDeniedRaw() {
        return deniedRaw;
    }

    public void setDeniedRaw(long deniedRaw) {
        this.deniedRaw = deniedRaw;
    }

    /**
     * Used to determine if this ChannelPermission relates to a specific Member.
     * @return {@code true} if this override is a user override.
     */
    public boolean isMemberPermission() {
        return isMemberPermission;
    }

    public void setMemberPermission(boolean memberPermission) {
        isMemberPermission = memberPermission;
    }

    /**
     * @return The ID of the channel this ChannelPermission is for.
     */
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * @return The ID of the guild this ChannelPermission is for.
     */
    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    /**
     * @return The ID of the user or role that this ChannelPermission is for.
     */
    public String getHolderId() {
        return holderId;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }

}
