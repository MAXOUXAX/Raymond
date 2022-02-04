package me.maxouxax.raymond.commands.slashannotations;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.annotation.*;

@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Options.class)
public @interface Option {

    OptionType type();
    String name();
    String description();
    boolean isRequired();

}
