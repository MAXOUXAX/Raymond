package me.maxouxax.raymond.commands;

import me.maxouxax.raymond.commands.slashannotations.Option;
import me.maxouxax.raymond.commands.slashannotations.Subcommand;
import me.maxouxax.raymond.commands.slashannotations.SubcommandGroup;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SimpleCommand {

    private final String name, description, help, exemple;
    private final Object object;
    private final Method method;
    private final int power;
    private final boolean guildOnly;
    private final List<OptionData> optionData = new ArrayList<>();
    private final List<SubcommandData> subcommandData = new ArrayList<>();
    private final List<SubcommandGroupData> subcommandGroupData = new ArrayList<>();

    public SimpleCommand(String name, String description, String help, String exemple, Object object, Method method, int power, boolean guildOnly){
        super();
        this.name = name;
        this.description = description;
        this.help = help;
        this.exemple = exemple;
        this.object = object;
        this.method = method;
        this.power = power;
        this.guildOnly = guildOnly;

        List<Option> options = Arrays.stream(method.getAnnotationsByType(Option.class)).collect(Collectors.toList());
        List<Subcommand> subcommands = Arrays.stream(method.getAnnotationsByType(Subcommand.class)).collect(Collectors.toList());
        List<SubcommandGroup> subcommandGroups = Arrays.stream(method.getAnnotationsByType(SubcommandGroup.class)).collect(Collectors.toList());

        options.forEach(option -> {
            optionData.add(new OptionData(option.type(), option.name(), option.description(), option.isRequired()));
        });
        subcommands.forEach(subcommand -> {
            subcommandData.add(new SubcommandData(subcommand.name(), subcommand.description()));
        });
        subcommandGroups.forEach(subcommandGroup -> {
            subcommandGroupData.add(new SubcommandGroupData(subcommandGroup.name(), subcommandGroup.description()));
        });
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

    public String getExemple() {
        return exemple;
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

    public boolean isGuildOnly() {
        return guildOnly;
    }

    public OptionData[] getOptionsData() {
        return optionData.toArray(new OptionData[0]);
    }

    public SubcommandData[] getSubcommandsData() {
        return subcommandData.toArray(new SubcommandData[0]);
    }

    public SubcommandGroupData[] getSubcommandsGroups() {
        return subcommandGroupData.toArray(new SubcommandGroupData[0]);
    }
}