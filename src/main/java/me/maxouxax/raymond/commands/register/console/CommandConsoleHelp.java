package me.maxouxax.raymond.commands.register.console;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.commands.ConsoleCommand;
import me.maxouxax.raymond.commands.SimpleConsoleCommand;

public class CommandConsoleHelp {

    private final CommandMap commandMap;

    public CommandConsoleHelp(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @ConsoleCommand(name="help", description="Affiche l'entièreté des commandes disponibles", help = "help")
    public void help(){
        for(SimpleConsoleCommand command : commandMap.getConsoleCommands()){
            Raymond.getInstance().getLogger().info(command.getName() + " - " + command.getDescription() + " - " + command.getHelp(), true);
        }
    }

}
