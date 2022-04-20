package me.maxouxax.raymond.schedule;

import org.json.JSONObject;

public class Room extends LabelledType {

    public Room(String label) {
        super(label);
    }

    public Room(JSONObject jsonObject) {
        super(jsonObject.getString("label"));
    }

}
