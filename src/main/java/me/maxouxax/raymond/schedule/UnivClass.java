package me.maxouxax.raymond.schedule;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.UnivConfig;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.json.JSONObject;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class UnivClass {

    @BsonIgnore
    private UnivConfig univConfig;

    private final String id;

    @BsonProperty("start_date_time")
    private final String startDateTime;

    @BsonProperty("end_date_time")
    private final String endDateTime;

    private final long day;

    private final int duration;
    private final List<String> urls;
    private final Course course;
    private final List<Teacher> teachers;
    private final List<Room> rooms;
    private final List<Group> groups;

    @BsonCreator
    public UnivClass(@BsonProperty("id") final String id,
                     @BsonProperty("start_date_time") final String startDateTime,
                     @BsonProperty("end_date_time") final String endDateTime,
                     @BsonProperty("day") final long day,
                     @BsonProperty("duration") final int duration,
                     @BsonProperty("urls") final List<String> urls,
                     @BsonProperty("course") final Course course,
                     @BsonProperty("teachers") final List<Teacher> teachers,
                     @BsonProperty("rooms") final List<Room> rooms,
                     @BsonProperty("groups") final List<Group> groups) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.day = day;
        this.duration = duration;
        this.urls = urls;
        this.course = course;
        this.teachers = teachers;
        this.rooms = rooms;
        this.groups = groups;
    }

    public UnivClass(JSONObject event) {
        this.univConfig = Raymond.getInstance().getUnivConnector().getUnivConfig();
        this.id = event.getString("id");
        this.startDateTime = event.getString("startDateTime");
        this.endDateTime = event.getString("endDateTime");
        this.day = event.getLong("day");
        this.duration = event.getInt("duration");
        this.urls = event.getJSONArray("urls").toList().stream().map(o -> (String) o).toList();
        this.course = new Course(event.getJSONObject("course"));
        this.teachers = event.getJSONArray("teachers").toList().stream().map(o -> (HashMap<String, String>) o).map(JSONObject::new).map(Teacher::new).toList();
        this.rooms = event.getJSONArray("rooms").toList().stream().map(o -> (HashMap<String, String>) o).map(JSONObject::new).map(Room::new).toList();
        this.groups = event.getJSONArray("groups").toList().stream().map(o -> (HashMap<String, String>) o).map(JSONObject::new).map(Group::new).toList();
    }

    public EmbedCrafter toEmbed() {
        EmbedCrafter embed = new EmbedCrafter(Raymond.getInstance());
        embed.setTitle(course.getLabel());
        embed.setColor(Color.decode(course.getColor()));

        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM").withLocale(Locale.FRANCE);
        ZonedDateTime start = ZonedDateTime.parse(startDateTime).withZoneSameInstant(ZoneId.of("Europe/Paris"));
        ZonedDateTime end = ZonedDateTime.parse(endDateTime).withZoneSameInstant(ZoneId.of("Europe/Paris"));

        embed.setAuthor(start.format(dayFormatter) + " — " + start.format(hourFormatter) + "-" + end.format(hourFormatter), univConfig.getDataUrl() + "/loginin?app=edt", univConfig.getClockEmoji());
        embed.setTimestamp(OffsetDateTime.from(start));

        embed.setDescription("\u200E");

        embed.addField("Groupe(s)", groups.stream().map(group -> "`" + group.getLabel() + "`").reduce((a, b) -> a + "\n" + b).orElse("Aucun"), true);
        embed.addField("Salle(s)", rooms.stream().map(room -> "`" + room.getLabel() + "`").reduce((a, b) -> a + "\n" + b).orElse("Aucune"), true);
        embed.addField("Enseignant(s)", teachers.stream().map(Teacher::getName).reduce((a, b) -> a + "\n" + b).orElse("Aucun"), false);

        return embed;
    }

    public Collection<? extends ActionRow> toActionRow() {
        List<ActionRow> rows = new ArrayList<>();
        if (course.getUrl() != null) {
            Button button = Button.link(course.getUrl(), "Voir le cours");
            rows.add(ActionRow.of(button));
        }
        if (teachers.size() > 0) {
            rows.add(ActionRow.of(teachers.stream().map(teacher -> Button.link("https://maxouxax.me/raymond/teacher/" + teacher.getEmail(), "Contacter " + teacher.getName()).withEmoji(Emoji.fromUnicode("\uD83D\uDCE7"))).toList()));
        }
        return rows;
    }

    public String getId() {
        return id;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public long getDay() {
        return day;
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getUrls() {
        return urls;
    }

    public Course getCourse() {
        return course;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Group> getGroups() {
        return groups;
    }

}
