package me.maxouxax.raymond.commands.interactions;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.UnivServerConfig;
import me.maxouxax.supervisor.interactions.modals.DiscordModalInteraction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.ScheduledEventAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventCreateMessageInteraction implements DiscordModalInteraction {

    private final DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("EEEE dd MMMM", Locale.FRANCE);
    private final DateTimeFormatter eventHourFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.FRANCE);
    private ZoneId paris = ZoneId.of("Europe/Paris");
    private final Raymond raymond;

    public EventCreateMessageInteraction() {
        this.raymond = Raymond.getInstance();
    }

    @Override
    public String name() {
        return "event-create";
    }

    @Override
    public String description() {
        return "Créez un événement";
    }

    @Override
    public String id() {
        return "event-create";
    }

    @Override
    public void onModalSubmit(ModalInteractionEvent event) {
        event.deferReply().queue();

        String name = event.getValue("event-name").getAsString();
        String location = event.getValue("event-location").getAsString();
        String description = event.getValue("event-description").getAsString();
        String startDateString = event.getValue("event-date-start").getAsString();
        String endDateString = event.getValue("event-date-end").getAsString();

        UnivServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(event.getGuild().getId()).getUnivServerConfig();
        String forumChannelId = serverConfig.getDiscordForumChannelId();

        ForumChannel forumChannel;

        if (forumChannelId == null || (forumChannel = event.getGuild().getForumChannelById(forumChannelId)) == null) {
            event.getHook().sendMessage("Le forum n'a pas été configuré, utilisez /event set-channel pour le configurer").setEphemeral(true).queue();
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dateFormat.set2DigitYearStart(Calendar.getInstance().getTime());

            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);
            OffsetDateTime start = OffsetDateTime.ofInstant(startDate.toInstant(), paris);
            OffsetDateTime end = OffsetDateTime.ofInstant(endDate.toInstant(), paris);
            boolean sameDay = start.getDayOfMonth() == end.getDayOfMonth() && start.getMonth() == end.getMonth() && start.getYear() == end.getYear();

            ScheduledEventAction action = event.getGuild().createScheduledEvent(name, location, start, end).setDescription(description);
            ScheduledEvent scheduledEvent = action.complete();

            String eventLink = "https://discord.com/events/" + event.getGuild().getId() + "/" + scheduledEvent.getId();
            String sameDayString = eventDateFormat.format(start) + " de " + eventHourFormat.format(start) + " à " + eventHourFormat.format(end);
            String startString = eventDateFormat.format(start) + " à " + eventHourFormat.format(start);
            String endString = eventDateFormat.format(end) + " à " + eventHourFormat.format(end);

            Role role = event.getGuild().getRoleById(serverConfig.getDiscordForumRoleId());

            String startTimestamp = "<t:" + start.toEpochSecond() + ":R>";
            String[] lines = description.split("\n");
            StringBuilder formattedDescription = new StringBuilder("> 📝 " + lines[0] + "\n");

            for (int i = 1; i < lines.length; i++) {
                formattedDescription.append("> ").append(lines[i]).append("\n");
            }

            MessageCreateBuilder message = new MessageCreateBuilder().addContent("# 🗓️ " + startTimestamp + "\n").addContent("## 📍 " + scheduledEvent.getLocation() + "\n").addContent(formattedDescription.toString()).addContent("\n\n").addContent("--").addContent("\n\n");
            if (sameDay) {
                message.addContent("🗓️ **Date**\n").addContent("> " + sameDayString + "\n");
            } else {
                message.addContent("🗓️ **Début**\n").addContent("> " + startString + "\n").addContent("🗓️ **Fin**\n").addContent("> " + endString + "\n");
            }
            message.addContent("\n\n").addContent("> 🔔 Événement Discord\n").addContent("> " + eventLink + "\n");
            if (role != null) message.addContent("> " + role.getAsMention());

            message.setSuppressedNotifications(true);

            forumChannel.createForumPost(name, message.build()).queue(forumPost -> {
                event.getHook().sendMessage("Salon de forum et événement créés!\nPost: " + forumPost.getThreadChannel().getJumpUrl()).queue();
            });
        } catch (ParseException e) {
            event.getHook().sendMessage("Une erreur est survenue lors de la création de l'événement").setEphemeral(true).queue();
        }
    }

}
