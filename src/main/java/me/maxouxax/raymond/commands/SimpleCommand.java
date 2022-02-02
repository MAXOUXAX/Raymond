package me.maxouxax.raymond.commands;

import java.lang.reflect.Method;

public final class SimpleCommand {

    private final String name, description, help, example;
    private final Command.ExecutorType executorType;
    private final Object object;
    private final Method method;
    private final int power;

    public SimpleCommand(String name, String description, String help, String example, Command.ExecutorType executorType, Object object, Method method, int power){
        this.name = name;
        this.description = description;
        this.help = help;
        this.example = example;
        this.executorType = executorType;
        this.object = object;
        this.method = method;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHelp() {
        return help;
    }

    public String getExample() {
        return example;
    }

    public Command.ExecutorType getExecutorType() {
        return executorType;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }

    public int getPower() {
        return power;
    }
}