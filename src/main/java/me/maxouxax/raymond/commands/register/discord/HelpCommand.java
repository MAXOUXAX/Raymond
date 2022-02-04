package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.commands.ConsoleCommand;
import me.maxouxax.raymond.commands.SimpleConsoleCommand;

public class HelpCommand {

    private final CommandMap commandMap;

    public HelpCommand(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    /*@Command(name="help", description="Affiche l'entièreté des commandes disponibles", help = ".help", example = ".help")
    private void help(User user, MessageChannel channel, Guild guild){
        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setTitle("Aide » Liste des commandes")
            .setColor(3447003);

        for(SimpleCommand command : commandMap.getDiscordCommands()){
            if(guild != null && command.getPower() > commandMap.getPowerUser(guild, user)) continue;

            embedCrafter.addField(command.getName(), command.getDescription(), true);
        }

        if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
        ((UserImpl)user).getPrivateChannel().sendMessage(embedCrafter.build()).queue();

        channel.sendMessage(user.getAsMention()+", veuillez regarder vos message privés.").queue();

    }*/

    @ConsoleCommand(name="help", description="Affiche l'entièreté des commandes disponibles", help = "help")
    private void help(){
        for(SimpleConsoleCommand command : commandMap.getConsoleCommands()){
            Raymond.getInstance().getLogger().info(command.getName() + " - " + command.getDescription() + " - " + command.getHelp(), true);
        }
    }

}
