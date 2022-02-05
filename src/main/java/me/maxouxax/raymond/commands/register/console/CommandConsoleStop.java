package me.maxouxax.raymond.commands.register.console;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.ConsoleCommand;

public class CommandConsoleStop {

    private final Raymond bot;

    public CommandConsoleStop(){
        this.bot = Raymond.getInstance();
    }

    @ConsoleCommand(name="stop")
    private void stop(){
        bot.setRunning(false);
    }

}
