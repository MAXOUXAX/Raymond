package me.maxouxax.raymond.commands.register.console;

import me.maxouxax.supervisor.Supervisor;
import me.maxouxax.supervisor.commands.ConsoleCommand;

public class CommandConsoleSay implements ConsoleCommand {

    @Override
    public String name() {
        return "say";
    }

    @Override
    public String description() {
        return "Permet de faire dire n'importe quoi au BOT";
    }

    @Override
    public String help() {
        return "say";
    }

    @Override
    public String example() {
        return "say";
    }

    @Override
    public void onCommand(String[] args) {
        Supervisor.getInstance().getLogger().info("Hey, it's working!");
    }
}
