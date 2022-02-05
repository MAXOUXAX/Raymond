package me.maxouxax.raymond.database;

import me.maxouxax.raymond.database.sql.DatabaseAccess;

public enum Databases {

    MARIADB("MARIADB", DatabaseAccess.class),
    MONGODB("MONGODB", me.maxouxax.raymond.database.nosql.DatabaseAccess.class),
    ;

    private String name;
    private Class<? extends IDatabaseAccess> classType;

    <T extends IDatabaseAccess> Databases(String name, Class<T> classType) {
        this.name = name;
        this.classType = classType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<? extends IDatabaseAccess> getClassType() {
        return classType;
    }

    public void setClassType(Class<? extends IDatabaseAccess> classType) {
        this.classType = classType;
    }

}
