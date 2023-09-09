package me.maxouxax.raymond.schedule;

import me.maxouxax.multi4j.schedule.Teacher;
import me.maxouxax.multi4j.schedule.UnivClass;
import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class DiscordUnivUtils {

    public static EmbedCrafter toEmbed(UnivClass univClass) {
        EmbedCrafter embed = new EmbedCrafter(Raymond.getInstance());
        embed.setTitle(univClass.getCourse().getLabel());
        embed.setColor(Color.decode(univClass.getCourse().getColor()));

        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM").withLocale(Locale.FRANCE);
        ZonedDateTime start = ZonedDateTime.parse(univClass.getStartDateTime()).withZoneSameInstant(ZoneId.of("Europe/Paris"));
        ZonedDateTime end = ZonedDateTime.parse(univClass.getEndDateTime()).withZoneSameInstant(ZoneId.of("Europe/Paris"));

        embed.setAuthor(start.format(dayFormatter) + " — " + start.format(hourFormatter) + "-" + end.format(hourFormatter), Raymond.getInstance().getMultiClient().getMultiConfig().getDataUrl() + "/loginin?app=edt", null);
        embed.setTimestamp(OffsetDateTime.from(start));

        embed.setDescription("\u200E");

        embed.addField("Groupe(s)", univClass.getGroups().stream().map(group -> "`" + group.getLabel() + "`").reduce((a, b) -> a + "\n" + b).orElse("Aucun"), true);
        embed.addField("Salle(s)", univClass.getRooms().stream().map(room -> "`" + room.getLabel() + "`").reduce((a, b) -> a + "\n" + b).orElse("Aucune"), true);
        embed.addField("Enseignant(s)", univClass.getTeachers().stream().map(Teacher::getName).reduce((a, b) -> a + "\n" + b).orElse("Aucun"), false);

        return embed;
    }

    public static Collection<? extends ActionRow> toActionRow(UnivClass univClass) {
        List<ActionRow> rows = new ArrayList<>();
        if (univClass.getCourse().getUrl() != null) {
            Button button = Button.link(univClass.getCourse().getUrl(), "Voir le cours");
            rows.add(ActionRow.of(button));
        }
        if (!univClass.getTeachers().isEmpty()) {
            rows.add(ActionRow.of(univClass.getTeachers().stream().map(teacher -> Button.link("https://maxouxax.me/raymond/teacher/" + teacher.getEmail(), "Contacter " + teacher.getName()).withEmoji(Emoji.fromUnicode("\uD83D\uDCE7"))).toList()));
        }
        return rows;
    }

}
