package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.multi4j.MultiHelper;
import me.maxouxax.multi4j.crous.CrousMenu;
import me.maxouxax.multi4j.crous.CrousRestaurant;
import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.RaymondServerConfig;
import me.maxouxax.raymond.config.UnivServerConfig;
import me.maxouxax.raymond.utils.CrousUtils;
import me.maxouxax.supervisor.interactions.annotations.Option;
import me.maxouxax.supervisor.interactions.annotations.Subcommand;
import me.maxouxax.supervisor.interactions.annotations.SubcommandGroup;
import me.maxouxax.supervisor.interactions.commands.DiscordCommand;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Optional;

public class CommandCrous implements DiscordCommand {

    private final Raymond raymond;

    public CommandCrous() {
        this.raymond = Raymond.getInstance();
    }

    @Override
    public String name() {
        return "crous";
    }

    @Override
    public String description() {
        return "Récupère le menu du CROUS";
    }

    @Override
    public void onRootCommand(MessageChannelUnion messageChannelUnion, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        UnivServerConfig serverConfig =
                raymond.getServerConfigsManager().getServerConfig(member.getGuild().getId()).getUnivServerConfig();
        String restaurantName = serverConfig.getCrousRestaurantName();
        if (restaurantName == null) {
            slashCommandInteractionEvent.reply("Aucun restaurant CROUS n'est configuré pour ce serveur.").queue();
            return;
        }

        try {
            CrousRestaurant restaurantDetails = MultiHelper.getRestaurantDetails(raymond.getMultiClient(), restaurantName);
            Optional<CrousMenu> todaysMenu = CrousUtils.findTodaysMenu(restaurantDetails);
            if (todaysMenu.isEmpty()) {
                slashCommandInteractionEvent.reply("Aucun menu disponible pour aujourd'hui :(").queue();
            } else {
                CrousMenu menu = todaysMenu.get();
                EmbedCrafter embedCrafter = CrousUtils.buildEmbedFromCrous(restaurantDetails, menu);
                slashCommandInteractionEvent.replyEmbeds(embedCrafter.build()).queue();
            }
        } catch (IOException | URISyntaxException | InterruptedException | ParseException e) {
            raymond.getLogger().error("Failed to get CROUS menu for guild " + member.getGuild().getId() + " (" + member.getGuild().getName() + ")", e);
        }
    }

    @SubcommandGroup(name = "config", description = "Configurez les paramètres des restaurants CROUS")
    @Subcommand(name = "set-channel", description = "Définissez le salon dans lequel envoyer le menu du CROUS")
    @Option(name = "channel", description = "Le salon dans lequel envoyer le menu du CROUS", type = OptionType.CHANNEL, required = true)
    public void onCROUSSetChannel(MessageChannelUnion messageChannelUnion, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        GuildChannelUnion channel = slashCommandInteractionEvent.getOption("channel").getAsChannel();
        if (channel.getType() != ChannelType.TEXT) {
            slashCommandInteractionEvent.reply("Le salon doit être un salon texte").queue();
            return;
        }

        RaymondServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(slashCommandInteractionEvent.getGuild().getId());
        serverConfig.getUnivServerConfig().setDiscordCrousMenuChannelId(channel.getId());
        serverConfig.saveConfig();

        slashCommandInteractionEvent.reply("Le salon dans lequel envoyer le menu du CROUS a été défini").queue();
    }

    @SubcommandGroup(name = "config", description = "Configurez les paramètres des restaurants CROUS")
    @Subcommand(name = "set-role", description = "Définissez le rôle à notifier lors de la publication du menu du CROUS")
    @Option(name = "role", description = "Le rôle à notifier lors de la publication du menu du CROUS", type = OptionType.ROLE, required = true)
    public void onCROUSSetRole(MessageChannelUnion messageChannelUnion, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        Role role = slashCommandInteractionEvent.getOption("role").getAsRole();
        RaymondServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(slashCommandInteractionEvent.getGuild().getId());
        serverConfig.getUnivServerConfig().setDiscordCrousMenuRoleId(role.getId());
        serverConfig.saveConfig();

        slashCommandInteractionEvent.reply("Le rôle à notifier lors de la publication du menu du CROUS a été défini").queue();
    }

    @SubcommandGroup(name = "config", description = "Configurez les paramètres des restaurants CROUS")
    @Subcommand(name = "set-restaurant-name", description = "Définissez le nom du restaurant CROUS")
    @Option(name = "restaurant-name", description = "Le nom du restaurant CROUS", type = OptionType.STRING, required = true)
    public void onCROUSSetRestaurant(MessageChannelUnion messageChannelUnion, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        String restaurantName = slashCommandInteractionEvent.getOption("restaurant-name").getAsString();
        RaymondServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(slashCommandInteractionEvent.getGuild().getId());
        serverConfig.getUnivServerConfig().setCrousRestaurantName(restaurantName);
        serverConfig.saveConfig();

        slashCommandInteractionEvent.reply("Le nom du restaurant CROUS a été défini").queue();
    }

    @Override
    public int power() {
        return 0;
    }

}
