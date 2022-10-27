package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.Supervisor;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandVersion implements DiscordCommand {

    private final Raymond raymond;

    public CommandVersion(Raymond raymond) {
        this.raymond = raymond;
    }

    @Override
    public String name() {
        return "version";
    }

    @Override
    public String description() {
        return "Affiche les informations sur la version du BOT";
    }

    @Override
    public void onRootCommand(MessageChannelUnion textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        try {
            EmbedCrafter embedCrafter = new EmbedCrafter(raymond);
            embedCrafter.setTitle("Raymond by MAXOUXAX — Équipier polyvalent", raymond.getConfig().getWebsiteUrl())
                    .setColor(3447003)
                    .addField("Je suis en version", raymond.getVersion(), true)
                    .addField("Je gère", Supervisor.getInstance().getCommandManager().getDiscordCommandsFromSupervised(raymond).size() + " commandes Discord", true);
            messageContextInteractionEvent.replyEmbeds(embedCrafter.build()).queue();
        } catch (Exception e) {
            Supervisor.getInstance().getErrorHandler().handleException(e);
            messageContextInteractionEvent.reply("An error occured. > " + e.getMessage()).queue();
        }
    }

    @Override
    public int power() {
        return 0;
    }
}
