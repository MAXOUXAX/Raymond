package me.maxouxax.raymond.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsoleCommand {

    String name();
    String description() default "Sans description.";
    String help() default "Aucune aide n'a été fournie";

}
