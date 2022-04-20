package me.maxouxax.raymond.schedule;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONObject;

import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class UnivClass {

    private String id;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private Date day;
    private int duration;
    private List<String> urls;
    private Course course;
    private List<Teacher> teachers;
    private List<Room> rooms;
    private List<Group> groups;

    public UnivClass(JSONObject event) {
        this.id = event.getString("id");
        this.startDateTime = ZonedDateTime.parse(event.getString("startDateTime")).withZoneSameInstant(ZoneId.of("Europe/Paris"));
        this.endDateTime = ZonedDateTime.parse(event.getString("endDateTime")).withZoneSameInstant(ZoneId.of("Europe/Paris"));
        this.day = new Date(event.getLong("day"));
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
        embed.setAuthor(startDateTime.format(dayFormatter) + " — " + startDateTime.format(hourFormatter) + "-" + endDateTime.format(hourFormatter), "https://google.com", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Icons8_flat_clock.svg/1200px-Icons8_flat_clock.svg.png");

        embed.setDescription("\u200E");

        embed.addField("Groupe(s)", groups.stream().map(Group::getLabel).reduce((a, b) -> a + ", " + b).orElse("Aucun"), true);
        embed.addField("Salle(s)", rooms.stream().map(Room::getLabel).reduce((a, b) -> a + ", " + b).orElse("Aucune"), true);
        embed.addField("Enseignant(s)", teachers.stream().map(Teacher::getName).reduce((a, b) -> a + ", " + b).orElse("Aucun"), false);

        return embed;
    }

    public Collection<? extends ActionRow> toActionRow() {
        List<ActionRow> rows = new ArrayList<>();
        if (course.getUrl() != null) {
            Button button = Button.link(course.getUrl(), "Voir le cours");
            rows.add(ActionRow.of(button));
        }
        if (teachers.size() > 0) {
            rows.add(ActionRow.of(teachers.stream().map(teacher -> Button.link("https://api.maxouxax.me/redirect?l=mailto:" + teacher.getEmail(), "\uD83D\uDCE7 Contacter " + teacher.getName())).toList()));
        }
        return rows;
    }

    public String getId() {
        return id;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public Date getDay() {
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
