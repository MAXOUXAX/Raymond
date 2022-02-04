package me.maxouxax.raymond.commands.register.console;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.commands.ConsoleCommand;

public class CommandConsoleSay {

    private final Raymond raymond;
    private final CommandMap commandMap;

    public CommandConsoleSay(CommandMap commandMap) {
        this.commandMap = commandMap;
        this.raymond = Raymond.getInstance();
    }

    @ConsoleCommand(name = "say", help = "say", description = "Permet de faire dire n'importe quoi au BOT")
    public void say(String[] args) {
        raymond.getLogger().info("Hey, it's working!");
    }

}
