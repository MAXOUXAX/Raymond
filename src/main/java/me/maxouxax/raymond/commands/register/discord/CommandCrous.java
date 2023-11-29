package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.multi4j.MultiHelper;
import me.maxouxax.multi4j.crous.CrousMenu;
import me.maxouxax.multi4j.crous.CrousRestaurant;
import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.UnivServerConfig;
import me.maxouxax.raymond.utils.CrousUtils;
import me.maxouxax.supervisor.interactions.commands.DiscordCommand;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

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

    @Override
    public int power() {
        return 0;
    }

}
