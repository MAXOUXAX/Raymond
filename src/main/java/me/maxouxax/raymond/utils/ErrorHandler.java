package me.maxouxax.raymond.utils;

import me.maxouxax.raymond.Raymond;

import java.util.Arrays;

public class ErrorHandler {

    private final Raymond raymond;

    public ErrorHandler() {
        this.raymond = Raymond.getInstance();
    }

    public void handleException(Throwable exception){
        raymond.getLogger().error("Une erreur est survenue !\n"+exception.getMessage());
        exception.printStackTrace();
        raymond.getLogger().error(exception.getMessage()+"\n"+Arrays.toString(exception.getStackTrace()), false);
    }



}
