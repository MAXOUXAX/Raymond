package me.maxouxax.raymond.database;

public interface IDatabaseAccess {

    String getType();
    void initPool();
    void closePool();

}
