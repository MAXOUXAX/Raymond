package me.maxouxax.raymond.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class EmbedCrafter {

    private String title;
    private String url;
    private int color = 15528177;
    private String description;
    private final List<MessageEmbed.Field> fields = new ArrayList<>();
    private String thumbnailUrl;
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public EmbedCrafter setTitle(String title) {
        this.title = title;
        return this;
    }

    public EmbedCrafter setTitle(String title, String url) {
        this.title = title;
        this.url = url;
        return this;
    }

    public int getColor() {
        return color;
    }

    public EmbedCrafter setColor(int color) {
        this.color = color;
        return this;
    }

    public EmbedCrafter setColor(Color color) {
        this.color = color.getRGB();
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EmbedCrafter setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<MessageEmbed.Field> getFields() {
        return fields;
    }

    public EmbedCrafter addField(MessageEmbed.Field field) {
        this.fields.add(field);
        return this;
    }

    public EmbedCrafter addField(String name, String value, boolean inline) {
        this.fields.add(new MessageEmbed.Field(name, value, inline));
        return this;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public EmbedCrafter setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public EmbedCrafter setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public MessageEmbed build(){
        me.maxouxax.raymond.Raymond raymond = me.maxouxax.raymond.Raymond.getInstance();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
            .setColor(color)
            .setFooter(raymond.getConfigurationManager().getStringValue("embedFooter"), raymond.getConfigurationManager().getStringValue("embedIconUrl"))
            .setTimestamp(OffsetDateTime.now(ZoneId.of("Europe/Paris")));
        fields.forEach(embedBuilder::addField);
        if(title != null){
            if(url != null){
                embedBuilder.setTitle(title, url);
            }else {
                embedBuilder.setTitle(title);
            }
        }
        if(description != null) embedBuilder.setDescription(description);
        if(thumbnailUrl != null) embedBuilder.setThumbnail(thumbnailUrl);
        if(imageUrl != null) embedBuilder.setImage(imageUrl);
        return embedBuilder.build();
    }

}
