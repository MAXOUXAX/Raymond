package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.RaymondServerConfig;
import me.maxouxax.raymond.config.UnivServerConfig;
import me.maxouxax.supervisor.interactions.annotations.Option;
import me.maxouxax.supervisor.interactions.annotations.Subcommand;
import me.maxouxax.supervisor.interactions.annotations.SubcommandGroup;
import me.maxouxax.supervisor.interactions.commands.DiscordCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;

public class CommandEvent implements DiscordCommand {

    private final Raymond raymond;

    public CommandEvent() {
        this.raymond = Raymond.getInstance();
    }

    @Override
    public void onRootCommand(MessageChannelUnion messageChannelUnion, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        slashCommandInteractionEvent.reply("Vous devez utiliser une sous-commande").setEphemeral(true).queue();
    }

    @SubcommandGroup(name = "manage", description = "Gérez les événements à venir")
    @Subcommand(name = "create", description = "Créez un événement")
    public void onEventCreate(MessageChannelUnion messageChannelUnion, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        Modal.Builder builder = Modal.create("event-create", "Création d'un événement")
                .addActionRow(TextInput.create("event-name", "Nom de l'événement", TextInputStyle.SHORT).build())
                .addActionRow(TextInput.create("event-description", "Description de l'événement", TextInputStyle.PARAGRAPH).build())
                .addActionRow(TextInput.create("event-location", "Lieu où se déroulera l'événement", TextInputStyle.SHORT).build())
                .addActionRow(TextInput.create("event-date-start", "Date de début de l'événement", TextInputStyle.SHORT).build())
                .addActionRow(TextInput.create("event-date-end", "Date de fin de l'événement", TextInputStyle.SHORT).build());

        slashCommandInteractionEvent.replyModal(builder.build()).queue();
    }

    @SubcommandGroup(name = "manage", description = "Gérez les événements à venir")
    @Subcommand(name = "delete", description = "Supprimez un événement")
    @Option(name = "event-name", description = "Nom de l'événement", type = OptionType.STRING, required = true)
    public void onEventDelete(MessageChannelUnion messageChannelUnion, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        String eventName = slashCommandInteractionEvent.getOption("event-name").getAsString();
        List<ScheduledEvent> events = slashCommandInteractionEvent.getGuild().getScheduledEventsByName(eventName, true);
        UnivServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(slashCommandInteractionEvent.getGuild().getId()).getUnivServerConfig();
        ForumChannel forum = slashCommandInteractionEvent.getGuild().getForumChannelById(serverConfig.getDiscordForumChannelId());
        List<ThreadChannel> threadChannels = forum.getThreadChannels().stream().filter(thread -> thread.getName().equals(eventName)).toList();

        if(events.isEmpty() && threadChannels.isEmpty()){
            slashCommandInteractionEvent.reply("Aucun événement trouvé avec ce nom").setEphemeral(true).queue();
            return;
        }

        events.forEach(scheduledEvent -> scheduledEvent.delete().queue());
        threadChannels.forEach(threadChannel -> threadChannel.delete().queue());

        slashCommandInteractionEvent.reply("Evénement supprimé ! (" + events.size() + " événement(s) et " + threadChannels.size() + " thread(s) supprimé(s))").queue();
    }

    @SubcommandGroup(name = "config", description = "Configurez les paramètres des événements")
    @Subcommand(name = "set-channel", description = "Définissez le salon forum des événements")
    @Option(name = "channel", description = "Le salon forum des événements", type = OptionType.CHANNEL, required = true)
    public void onEventSetChannel(MessageChannelUnion messageChannelUnion, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        GuildChannelUnion channel = slashCommandInteractionEvent.getOption("channel").getAsChannel();
        if (channel.getType() != ChannelType.FORUM) {
            slashCommandInteractionEvent.reply("Le salon doit être un salon forum").setEphemeral(true).queue();
            return;
        }

        RaymondServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(slashCommandInteractionEvent.getGuild().getId());
        serverConfig.getUnivServerConfig().setDiscordForumChannelId(channel.getId());
        serverConfig.saveConfig();

        slashCommandInteractionEvent.reply("Le salon forum des événements a été défini").queue();
    }

    @SubcommandGroup(name = "config", description = "Configurez les paramètres des événements")
    @Subcommand(name = "set-role", description = "Définissez le rôle à notifier lors de la création d'un événement")
    @Option(name = "role", description = "Le rôle à notifier lors de la création d'un événement", type = OptionType.ROLE, required = true)
    public void onEventSetRole(MessageChannelUnion messageChannelUnion, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        Role role = slashCommandInteractionEvent.getOption("role").getAsRole();
        RaymondServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(slashCommandInteractionEvent.getGuild().getId());
        serverConfig.getUnivServerConfig().setDiscordForumRoleId(role.getId());
        serverConfig.saveConfig();

        slashCommandInteractionEvent.reply("Le rôle à notifier lors de la création d'un événement a été défini").queue();
    }

    @Override
    public int power() {
        return 100;
    }

    @Override
    public String name() {
        return "event";
    }

    @Override
    public String description() {
        return "Créez et gérez les événements à venir";
    }
}
