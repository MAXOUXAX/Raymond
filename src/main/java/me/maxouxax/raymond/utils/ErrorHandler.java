package me.maxouxax.raymond.utils;

import me.maxouxax.supervisor.Supervisor;

import java.util.Arrays;

public class ErrorHandler {

    public void handleException(Throwable exception) {
        Supervisor.getInstance().getLogger().error("Une erreur est survenue !\n" + exception.getMessage());
        exception.printStackTrace();
        Supervisor.getInstance().getLogger().error(exception.getMessage() + "\n" + Arrays.toString(exception.getStackTrace()), false);
    }


}
