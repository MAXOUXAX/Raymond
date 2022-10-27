package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.commands.slashannotations.Option;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandPower implements DiscordCommand {

    @Override
    public String name() {
        return "power";
    }

    @Override
    public String description() {
        return "Permet de définir le power d'un utilisateur";
    }

    @Override
    @Option(name = "power", type = OptionType.INTEGER, required = true, description = "Power à attribuer")
    @Option(name = "utilisateur", type = OptionType.USER, required = true, description = "Utilisateur auquel attribuer le power")
    public void onRootCommand(MessageChannelUnion textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        long power = messageContextInteractionEvent.getOption("power").getAsLong();
        Member memberTo = messageContextInteractionEvent.getOption("utilisateur").getAsMember();
        Raymond.getInstance().getServerConfigsManager().getServerConfig(messageContextInteractionEvent.getGuild().getId()).setUserPower(memberTo.getUser(), power);
        messageContextInteractionEvent.reply("Le power de " + memberTo.getAsMention() + " est maintenant de " + power).setEphemeral(true).queue();
    }

    @Override
    public int power() {
        return 150;
    }

}
