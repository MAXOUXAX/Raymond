package me.maxouxax.raymond.schedule;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.json.JSONObject;

public class Teacher {

    private final String name;
    private final String email;

    @BsonCreator
    public Teacher(@BsonProperty("name") final String name,
                   @BsonProperty("email") final String email) {
        this.name = name;
        this.email = email;
    }

    public Teacher(JSONObject jsonObject) {
        this.name = jsonObject.getString("name");
        this.email = jsonObject.getString("email");
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
