package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.Command;
import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandVersion {

    private final Raymond raymond;
    private final CommandMap commandMap;

    public CommandVersion(CommandMap commandMap) {
        this.commandMap = commandMap;
        this.raymond = Raymond.getInstance();
    }

    @Command(name="version",description="Affiche les informations sur la version du BOT", help = ".version", example = ".version", guildOnly = false)
    private void version(TextChannel channel, SlashCommandInteractionEvent slashCommandEvent){
        try{
            EmbedCrafter embedCrafter = new EmbedCrafter();
            embedCrafter.setTitle("Raymond by MAXOUXAX — Équipier polyvalent", raymond.getGlobalConfigManager().getStringValue("websiteUrl"))
                .setColor(3447003)
                .addField("Je suis en version", raymond.getVersion(), true)
                .addField("Je gère", commandMap.getDiscordCommands().size()+" commandes Discord", true);
            slashCommandEvent.getHook().sendMessageEmbeds(embedCrafter.build()).queue();
        }catch (Exception e) {
            raymond.getErrorHandler().handleException(e);
            slashCommandEvent.getHook().sendMessage("An error occured. > " + e.getMessage()).queue();
        }
    }

}
