package me.maxouxax.raymond.commands.register.console;

import me.maxouxax.supervisor.Supervisor;
import me.maxouxax.supervisor.commands.ConsoleCommand;

public class CommandConsoleHelp implements ConsoleCommand {

    private final Supervisor supervisor;

    public CommandConsoleHelp() {
        this.supervisor = Supervisor.getInstance();
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "Affiche l'entièreté des commandes disponibles";
    }

    @Override
    public String help() {
        return "help";
    }

    @Override
    public String example() {
        return "help";
    }

    @Override
    public void onCommand(String[] args) {
        for (ConsoleCommand command : supervisor.getCommandManager().getConsoleCommands()) {
            supervisor.getLogger().info(command.name() + " - " + command.description() + " - " + command.help(), true);
        }
    }

}
