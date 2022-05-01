package me.maxouxax.raymond.schedule;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.json.JSONObject;

public class Course extends LabelledType {

    private final String id;
    private final String color;
    private final String url;
    private final String type;

    @BsonCreator
    public Course(@BsonProperty("id") final String id,
                  @BsonProperty("label") final String label,
                  @BsonProperty("color") final String color,
                  @BsonProperty("url") final String url,
                  @BsonProperty("type") final String type) {
        super(label);
        this.id = id;
        this.color = color;
        this.url = url;
        this.type = type;
    }

    public Course(JSONObject course) {
        this(course.getString("id"), course.getString("label"), course.getString("color"), course.optString("url", null), course.getString("type"));
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

}
