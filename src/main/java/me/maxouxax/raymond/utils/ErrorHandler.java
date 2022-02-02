package me.maxouxax.raymond.utils;

import java.util.Arrays;

public class ErrorHandler {

    private final me.maxouxax.raymond.Raymond raymond;

    public ErrorHandler() {
        this.raymond = me.maxouxax.raymond.Raymond.getInstance();
    }

    public void handleException(Throwable exception){
        raymond.getLogger().error("Une erreur est survenue !\n"+exception.getMessage());
        exception.printStackTrace();
        raymond.getLogger().error(exception.getMessage()+"\n"+Arrays.toString(exception.getStackTrace()), false);
    }



}
