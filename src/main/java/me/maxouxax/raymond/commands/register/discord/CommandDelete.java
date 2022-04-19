package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.commands.slashannotations.Option;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.concurrent.CompletableFuture;

public class CommandDelete implements DiscordCommand {

    @Override
    public String name() {
        return "delete";
    }

    @Override
    public String description() {
        return "Permet de nettoyer un nombre x de message du salon";
    }

    @Override
    @Option(name = "nombre-de-messages", description = "Nombre de messages à supprimer", type = OptionType.INTEGER, isRequired = true)
    public void onCommand(TextChannel textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        long messagesToDelete = messageContextInteractionEvent.getOption("nombre-de-messages").getAsLong();
        if (messagesToDelete > 100 || messagesToDelete < 2) {
            messageContextInteractionEvent.reply("Dû à une limitation de Discord, le nombre de messages à supprimer doit être compris entre 2 et 100").setEphemeral(true).queue();
        } else {
            messageContextInteractionEvent.reply("Suppression en cours...").queue();
            MessageHistory history = new MessageHistory(textChannel);
            messageContextInteractionEvent.getHook().editOriginal("Suppression en cours... (Récupération de l'historique)").queue();
            history.retrievePast(Math.toIntExact(messagesToDelete)).queue(messages -> {
                messageContextInteractionEvent.getHook().editOriginal("Suppression en cours... (Supression de " + messagesToDelete + " messages)").queue();
                CompletableFuture.allOf(textChannel.purgeMessages(messages).toArray(CompletableFuture[]::new)).thenAccept(unused -> {
                    messageContextInteractionEvent.getHook().editOriginal("Suppression de " + messagesToDelete + " messages terminée !").queue();
                });
            });
        }
    }

    @Override
    public int power() {
        return 50;
    }

}
