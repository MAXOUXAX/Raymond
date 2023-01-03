package me.maxouxax.raymond.commands.interactions;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.UnivServerConfig;
import me.maxouxax.supervisor.interactions.modals.DiscordModalInteraction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.ScheduledEventAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class EventCreateMessageInteraction implements DiscordModalInteraction {

    private SimpleDateFormat eventDateFormat = new SimpleDateFormat("EEEE dd MMMM à HH:mm", Locale.FRANCE);
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
        String name = event.getValue("event-name").getAsString();
        String location = event.getValue("event-location").getAsString();
        String description = event.getValue("event-description").getAsString();
        String startDateString = event.getValue("event-date-start").getAsString();
        String endDateString = event.getValue("event-date-end").getAsString();

        UnivServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(event.getGuild().getId()).getUnivServerConfig();
        String forumChannelId = serverConfig.getDiscordForumChannelId();

        ForumChannel forumChannel;

        if (forumChannelId == null || (forumChannel = event.getGuild().getForumChannelById(forumChannelId)) == null) {
            event.reply("Le forum n'a pas été configuré, utilisez /event set-channel pour le configurer").setEphemeral(true).queue();
            return;
        }

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);
            OffsetDateTime start = OffsetDateTime.ofInstant(startDate.toInstant(), paris);
            OffsetDateTime end = OffsetDateTime.ofInstant(endDate.toInstant(), paris);

            ScheduledEventAction action = event.getGuild().createScheduledEvent(name, location, start, end).setDescription(description);
            ScheduledEvent scheduledEvent = action.complete();

            String eventLink = "https://discord.com/events/" + event.getGuild().getId() + "/" + scheduledEvent.getId();

            forumChannel.createForumPost(name, new MessageCreateBuilder()
                    .addContent("# " + eventDateFormat.format(startDate) + " - " + eventDateFormat.format(endDate) + "\n")
                    .addContent("## " + scheduledEvent.getLocation() + "\n")
                    .addContent("\n")
                    .addContent(description + "\n")
                    .addContent("\n")
                    .addContent("Événement Discord: " + eventLink)
                    .build()
            ).queue(forumPost -> {
                ThreadChannel thread = forumPost.getThreadChannel();
                Role role = thread.getGuild().getRoleById(serverConfig.getDiscordForumRoleId());
                thread.sendMessage("Nouvel événement ! " + (role != null ? role.getAsMention() : "")).queue();
            });

            event.reply("L'événement a bien été créé !").queue();
        } catch (ParseException e) {
            event.reply("Une erreur est survenue lors de la création de l'événement").setEphemeral(true).queue();
        }
    }

}
