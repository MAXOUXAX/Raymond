package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.RaymondServerConfig;
import me.maxouxax.supervisor.interactions.commands.DiscordCommand;
import me.maxouxax.supervisor.jda.pojos.ChannelPermission;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CommandArchive implements DiscordCommand {

    private final Raymond raymond;

    public CommandArchive() {
        this.raymond = Raymond.getInstance();
    }

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
    public void onRootCommand(MessageChannelUnion textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        if(!textChannel.getType().isGuild()) return;
        GuildMessageChannel guildChannel = textChannel.asGuildMessageChannel();

        Guild guild = guildChannel.getGuild();
        RaymondServerConfig raymondServerConfig = raymond.getServerConfigsManager().getServerConfig(guild.getId());
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
            raymondServerConfig.setPermissionBeforeArchive(permissionBeforeArchive);
            raymondServerConfig.setArchived(true);
            raymondServerConfig.saveConfig();
            messageContextInteractionEvent.reply("Serveur archivé !").queue();
        }
    }

}
