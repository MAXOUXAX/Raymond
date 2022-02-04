package me.maxouxax.raymond.commands.slashannotations;

import java.lang.reflect.Method;

public class SimpleInteraction {

    private final String id;
    private final Object object;
    private final Method method;

    public SimpleInteraction(String id, Object object, Method method) {
        this.id = id;
        this.object = object;
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }
}
