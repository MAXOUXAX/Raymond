package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import me.maxouxax.supervisor.utils.UserUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class CommandPing implements DiscordCommand {

    private final Raymond raymond;

    public CommandPing(Raymond raymond) {
        this.raymond = raymond;
    }

    @Override
    public String name() {
        return "ping";
    }

    @Override
    public String description() {
        return "Permet de récupérer le ping du bot";
    }

    @Override
    public void onCommand(MessageChannelUnion textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        long ping = raymond.getJda().getGatewayPing();
        EmbedCrafter embedCrafter = new EmbedCrafter(raymond)
                .setTitle("DiscordAPI ping", raymond.getConfig().getWebsiteUrl())
                .setThumbnailUrl(UserUtils.getAvatarUrl(member) + "?size=256")
                .addField(new MessageEmbed.Field("Ping", ping + "ms", true));
        if (ping > 300) {
            embedCrafter.setColor(Color.RED);
            embedCrafter.setDescription("Mauvais ping");
        } else {
            embedCrafter.setColor(Color.GREEN);
            embedCrafter.setDescription("Bon ping");
        }
        messageContextInteractionEvent.replyEmbeds(embedCrafter.build()).queue();
    }

    @Override
    public int power() {
        return 0;
    }

}
