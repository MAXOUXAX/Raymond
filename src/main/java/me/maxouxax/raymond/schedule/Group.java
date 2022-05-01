package me.maxouxax.raymond.schedule;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.json.JSONObject;

public class Group extends LabelledType{

    @BsonCreator
    public Group(@BsonProperty("label") final String label) {
        super(label);
    }

    public Group(JSONObject jsonObject) {
        super(jsonObject.getString("label"));
    }

}
