package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.commands.Command;
import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandVersion {

    private final me.maxouxax.raymond.Raymond raymond;
    private final CommandMap commandMap;

    public CommandVersion(CommandMap commandMap) {
        this.commandMap = commandMap;
        this.raymond = me.maxouxax.raymond.Raymond.getInstance();
    }

    @Command(name="version",type= Command.ExecutorType.USER,description="Affiche les informations sur la version du BOT", help = "version", example = "version")
    private void version(MessageChannel channel){
        try{
            EmbedCrafter builder = new EmbedCrafter();
            builder.setTitle("Thérèse • by MAXOUXAX • Amazingly powerful", raymond.getConfigurationManager().getStringValue("websiteUrl"));
            builder.setColor(3447003);
            builder.addField("Je suis en version", raymond.getVersion(), true);
            builder.addField("Je gère", commandMap.getDiscordCommands().size()+" commandes Discord", true);
            channel.sendMessage(builder.build()).queue();
        }catch (Exception e) {
            raymond.getErrorHandler().handleException(e);
            channel.sendMessage("An error occured > " + e.getMessage()).queue();
        }
    }

}
