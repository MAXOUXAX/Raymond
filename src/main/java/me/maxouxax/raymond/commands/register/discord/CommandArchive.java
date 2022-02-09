package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.Command;
import me.maxouxax.raymond.serversconfig.ServerConfig;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.List;

public class CommandArchive {

    private final Raymond raymond;

    public CommandArchive(){
        this.raymond = Raymond.getInstance();
    }

    @Command(name="archive", power = 150, help = "archive", example = "archive")
    public void archive(User user, TextChannel textChannel, SlashCommandInteractionEvent slashCommandInteractionEvent, String[] args) {
        Guild guild = textChannel.getGuild();
        ServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(guild.getId());
        if (serverConfig.isArchived()) {
            slashCommandInteractionEvent.getHook().sendMessage("This server is already archived.").queue();
        } else {
            slashCommandInteractionEvent.getHook().sendMessage("This server is now archived.").queue();
            List<GuildChannel> channelsList = guild.getChannels();
            Role atEveryone = guild.getRolesByName("@everyone", true).get(0);
            channelsList.forEach(channel -> {
                HashMap<String, List<PermissionOverride>> permissionBeforeArchive = serverConfig.getPermissionBeforeArchive();
                permissionBeforeArchive.put(channel.getId(), channel.getPermissionContainer().getPermissionOverrides());
                serverConfig.setPermissionBeforeArchive(permissionBeforeArchive, true);
                channel.getPermissionContainer().getPermissionOverrides().forEach(permissionOverride -> {
                    permissionOverride.delete().queue();
                });
                channel.getPermissionContainer().putPermissionOverride(atEveryone).deny(Permission.VIEW_CHANNEL).deny(Permission.MESSAGE_SEND).deny(Permission.ALL_VOICE_PERMISSIONS).queue();
            });
            serverConfig.setArchived(true, true);
        }
    }

    @Command(name="unarchive", power = 150, help = "unarchive", example = "unarchive")
    public void unarchive(User user, TextChannel textChannel, SlashCommandInteractionEvent slashCommandInteractionEvent, String[] args) {
        Guild guild = textChannel.getGuild();
        ServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(guild.getId());
        if(!serverConfig.isArchived()){
            slashCommandInteractionEvent.getHook().sendMessage("This server is not archived.").queue();
        }else {
            slashCommandInteractionEvent.getHook().sendMessage("This server is no longer archived.").queue();
            serverConfig.setArchived(false, true);
        }
        /*List<GuildChannel> channelsList = guild.getChannels();
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
         */
    }

}
