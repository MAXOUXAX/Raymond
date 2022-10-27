package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.commands.slashannotations.Option;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandGame implements DiscordCommand {

    @Override
    public String name() {
        return "game";
    }

    @Override
    public String description() {
        return "Permet de modifier le jeu du BOT";
    }

    @Override
    @Option(name = "nom-du-jeu", description = "Nom du jeu", type = OptionType.STRING, isRequired = true)
    public void onCommand(MessageChannelUnion textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        Raymond.getInstance().getJda().getPresence().setActivity(Activity.playing(messageContextInteractionEvent.getOption("nom-du-jeu").getAsString()));
        messageContextInteractionEvent.reply("Jeu mis à jour avec succès !").setEphemeral(true).queue();
    }

    @Override
    public int power() {
        return 150;
    }

}
