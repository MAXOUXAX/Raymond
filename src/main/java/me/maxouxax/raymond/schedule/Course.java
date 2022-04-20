package me.maxouxax.raymond.schedule;

import org.json.JSONObject;

public class Course {

    private String id;
    private String label;
    private String color;
    private String url;
    private String type;

    public Course(String id, String label, String color, String url, String type) {
        this.id = id;
        this.label = label;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
