package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.HashMap;
import java.util.List;

public class CommandLock {

    private final Raymond raymond;
    private final HashMap<GuildChannel, List<PermissionOverride>> channelsHashMap = new HashMap<>();

    public CommandLock(){
        this.raymond = Raymond.getInstance();
    }

    @Command(name="lock", power = 150, help = "lock", example = "lock")
    public void lock(User user, TextChannel textChannel, String[] args) {
        Guild guild = textChannel.getGuild();
        List<GuildChannel> channelsList = guild.getChannels();
        channelsList.forEach(channel -> {
            channelsHashMap.put(channel, channel.getPermissionContainer().getPermissionOverrides());
            channel.getPermissionContainer().getPermissionOverrides().forEach(permissionOverride -> {
                permissionOverride.delete().queue();
            });
            channel.getPermissionContainer().putPermissionOverride(guild.getRoleById("529310963816595467")).deny(Permission.VIEW_CHANNEL).deny(Permission.MESSAGE_SEND).deny(Permission.ALL_VOICE_PERMISSIONS).queue();
        });
    }

    @Command(name="unlock", power = 150, help = "unlock", example = "unlock")
    public void unlock(User user, TextChannel textChannel, String[] args) {
        Guild guild = textChannel.getGuild();
        List<GuildChannel> channelsList = guild.getChannels();
        channelsList.forEach(channel -> {
            List<PermissionOverride> permissions = channelsHashMap.get(channel);
            permissions.forEach(permissionOverride -> {
                channel.getPermissionContainer().putPermissionOverride(permissionOverride.getPermissionHolder())
                        .setAllow(permissionOverride.getAllowedRaw())
                        .setDeny(permissionOverride.getDeniedRaw())
                        .queue();
            });
        });
        channelsHashMap.clear();
    }

}
