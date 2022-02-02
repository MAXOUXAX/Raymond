package me.maxouxax.raymond.utils;

import java.util.Iterator;

public class ListUtils {

    public static String listToString(Iterator<String> iterator){
        StringBuilder stringBuilder = new StringBuilder();
        while(iterator.hasNext()){
            String teacher = iterator.next();
            stringBuilder.append(teacher);
            if (iterator.hasNext())stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

}
