package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.RaymondServerConfig;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.jda.pojos.ChannelPermission;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CommandArchive implements DiscordCommand {

    private final Raymond raymond;

    public CommandArchive() {
        this.raymond = Raymond.getInstance();
    }

    /*@Command(name="unarchive", power = 150, help = "unarchive", example = "unarchive")
    public void unarchive(User user, TextChannel textChannel, SlashCommandInteractionEvent slashCommandInteractionEvent, String[] args) {
        Guild guild = textChannel.getGuild();
        ServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(guild.getId());
        if (!serverConfig.isArchived()) {
            slashCommandInteractionEvent.getHook().sendMessage("Ce serveur n'est pas archivé, utilisez /archive pour l'archiver").queue();
        } else {
            slashCommandInteractionEvent.getHook().sendMessage("Serveur restauré !").queue();
            serverConfig.setArchived(false, true);
        }
        List<GuildChannel> channelsList = guild.getChannels();
        channelsList.forEach(channel -> {
            HashMap<String, List<ChannelPermission>> permissionBeforeArchive = serverConfig.getPermissionBeforeArchive();
            channel.getPermissionContainer().getPermissionOverrides().forEach(permissionOverride -> permissionOverride.delete().queue());

            permissionBeforeArchive.get(channel.getId()).forEach(channelPermission -> {
                IPermissionHolder permissionHolder = channelPermission.isMemberPermission() ? channel.getGuild().getMemberById(channelPermission.getHolderId()) : channel.getGuild().getRoleById(channelPermission.getHolderId());
                if(permissionHolder != null) {
                    channel.getPermissionContainer().putPermissionOverride(permissionHolder)
                            .setAllow(channelPermission.getAllowedRaw())
                            .setDeny(channelPermission.getDeniedRaw())
                            .queue();
                }
            });
        });
        serverConfig.getPermissionBeforeArchive().clear();
        serverConfig.save();
    }*/

    @Override
    public String name() {
        return "archive";
    }

    @Override
    public int power() {
        return 150;
    }

    @Override
    public String description() {
        return "Permet d'archiver un serveur";
    }

    @Override
    public void onCommand(TextChannel textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        Guild guild = textChannel.getGuild();
        RaymondServerConfig raymondServerConfig = (RaymondServerConfig) raymond.getServerConfigsManager().getServerConfig(guild.getId());
        if (raymondServerConfig.isArchived()) {
            messageContextInteractionEvent.reply("Ce serveur est déjà archivé, utilisez /unarchive pour le désarchiver").setEphemeral(true).queue();
        } else {
            List<GuildChannel> channelsList = guild.getChannels();
            Role atEveryone = guild.getRolesByName("@everyone", true).get(0);

            HashMap<String, List<ChannelPermission>> permissionBeforeArchive = raymondServerConfig.getPermissionBeforeArchive();
            //making sure there are no permissions in the config, which should be the case if the unarchive process went well
            if (!permissionBeforeArchive.isEmpty()) permissionBeforeArchive.clear();

            channelsList.forEach(channel -> {
                permissionBeforeArchive.put(channel.getId(),
                        channel.getPermissionContainer().getPermissionOverrides().stream().map(ChannelPermission::new).collect(Collectors.toList())
                );
                channel.getPermissionContainer().getPermissionOverrides().forEach(permissionOverride -> {
                    permissionOverride.getManager().deny(
                                    Permission.MESSAGE_SEND, Permission.MESSAGE_SEND_IN_THREADS,
                                    Permission.VOICE_CONNECT, Permission.VOICE_STREAM, Permission.VOICE_START_ACTIVITIES)
                            .queue();
                });
            });
            raymondServerConfig.setPermissionBeforeArchive(permissionBeforeArchive, true);
            raymondServerConfig.setArchived(true, true);
            messageContextInteractionEvent.reply("Serveur archivé !").queue();
        }
    }

}
