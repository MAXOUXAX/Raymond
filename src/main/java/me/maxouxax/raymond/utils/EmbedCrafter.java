package me.maxouxax.raymond.utils;

import me.maxouxax.raymond.Raymond;
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
    private String authorName, authorUrl, authorIconUrl;
    private String footerText, footerIconUrl;

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

    public String getAuthorName() {
        return authorName;
    }

    public EmbedCrafter setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public EmbedCrafter setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }

    public String getAuthorIconUrl() {
        return authorIconUrl;
    }

    public EmbedCrafter setAuthorIconUrl(String authorIconUrl) {
        this.authorIconUrl = authorIconUrl;
        return this;
    }

    public String getFooterText() {
        return footerText;
    }

    public String getFooterIconUrl() {
        return footerIconUrl;
    }

    public MessageEmbed build(){
        Raymond bot = Raymond.getInstance();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(color);
        if(footerText == null || !footerText.equals("noFooter")) {
            embedBuilder
                    .setFooter(bot.getGlobalConfigManager().getStringValue("embedFooter"), bot.getGlobalConfigManager().getStringValue("embedIconUrl"))
                    .setTimestamp(OffsetDateTime.now(ZoneId.of("Europe/Paris")));
        }
        fields.forEach(embedBuilder::addField);
        if(footerText != null && !footerText.equals("noFooter")){
            if(footerIconUrl != null){
                embedBuilder.setFooter(footerText, footerIconUrl);
            }else {
                embedBuilder.setFooter(footerText);
            }
        }
        if(title != null){
            if(url != null){
                embedBuilder.setTitle(title, url);
            }else {
                embedBuilder.setTitle(title);
            }
        }
        if(authorName != null){
            if(authorUrl != null) {
                if (authorIconUrl != null) {
                    embedBuilder.setAuthor(authorName, authorUrl, authorIconUrl);
                }else{
                    embedBuilder.setAuthor(authorName, authorUrl);
                }
            }else {
                embedBuilder.setAuthor(authorName);
            }
        }
        if(description != null) embedBuilder.setDescription(description);
        if(thumbnailUrl != null) embedBuilder.setThumbnail(thumbnailUrl);
        if(imageUrl != null) embedBuilder.setImage(imageUrl);
        return embedBuilder.build();
    }

    public EmbedCrafter setAuthor(String authorName, String authorUrl, String authorIconUrl) {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.authorIconUrl = authorIconUrl;
        return this;
    }

    public EmbedCrafter forceFooter(String text, String iconUrl) {
        this.footerText = text;
        this.footerIconUrl = iconUrl;
        return this;
    }

    public EmbedCrafter forceFooter(String text) {
        this.footerText = text;
        return this;
    }

    public EmbedCrafter noFooter() {
        this.footerText = "noFooter";
        return this;
    }
}
