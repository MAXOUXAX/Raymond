package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.RaymondServerConfig;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.jda.pojos.ChannelPermission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.List;

public class CommandUnarchive implements DiscordCommand {

    private final Raymond raymond;

    public CommandUnarchive() {
        this.raymond = Raymond.getInstance();
    }

    @Override
    public String name() {
        return "unarchive";
    }

    @Override
    public int power() {
        return 150;
    }

    @Override
    public String description() {
        return "Permet de restaurer un serveur";
    }

    @Override
    public void onCommand(TextChannel textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        Guild guild = textChannel.getGuild();
        RaymondServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(guild.getId());
        if (!serverConfig.isArchived()) {
            messageContextInteractionEvent.reply("Ce serveur n'est pas archivé, utilisez /archive pour l'archiver").setEphemeral(true).queue();
        } else {
            messageContextInteractionEvent.reply("Serveur en cours de restauration...").queue();
            List<GuildChannel> channelsList = guild.getChannels();
            channelsList.forEach(channel -> {
                HashMap<String, List<ChannelPermission>> permissionBeforeArchive = serverConfig.getPermissionBeforeArchive();
                channel.getPermissionContainer().getPermissionOverrides().forEach(permissionOverride -> permissionOverride.delete().queue());

                permissionBeforeArchive.get(channel.getId()).forEach(channelPermission -> {
                    IPermissionHolder permissionHolder = channelPermission.isMemberPermission() ? channel.getGuild().getMemberById(channelPermission.getHolderId()) : channel.getGuild().getRoleById(channelPermission.getHolderId());
                    if (permissionHolder != null) {
                        channel.getPermissionContainer().putPermissionOverride(permissionHolder)
                                .setAllow(channelPermission.getAllowedRaw())
                                .setDeny(channelPermission.getDeniedRaw())
                                .queue();
                    }
                });
            });
            serverConfig.getPermissionBeforeArchive().clear();
            serverConfig.save();
            serverConfig.setArchived(false);
            messageContextInteractionEvent.getHook().editOriginal("Serveur restauré avec succès").queue();
        }
    }

}
