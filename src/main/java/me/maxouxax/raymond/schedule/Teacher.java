package me.maxouxax.raymond.schedule;

import org.json.JSONObject;

public class Teacher {

    private String name;
    private String email;

    public Teacher(String name, String email) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
