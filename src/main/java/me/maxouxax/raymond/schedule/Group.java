package me.maxouxax.raymond.schedule;

import org.json.JSONObject;

public class Group extends LabelledType{

    public Group(String label) {
        super(label);
    }

    public Group(JSONObject jsonObject) {
        super(jsonObject.getString("label"));
    }

}
