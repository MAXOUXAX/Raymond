package me.maxouxax.raymond.commands;

import java.lang.reflect.Method;

public final class SimpleConsoleCommand {

    private final String name, description, help;
    private final Object object;
    private final Method method;

    public SimpleConsoleCommand(String name, String description, String help, Object object, Method method){
        super();
        this.name = name;
        this.description = description;
        this.help = help;
        this.object = object;
        this.method = method;
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

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }

}