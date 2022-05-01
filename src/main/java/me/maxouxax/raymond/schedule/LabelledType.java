package me.maxouxax.raymond.schedule;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class LabelledType {

    private final String label;

    @BsonCreator
    public LabelledType(@BsonProperty("label") final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
