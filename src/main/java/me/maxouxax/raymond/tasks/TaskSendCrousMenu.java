package me.maxouxax.raymond.tasks;

import me.maxouxax.multi4j.MultiHelper;
import me.maxouxax.multi4j.crous.CrousMenu;
import me.maxouxax.multi4j.crous.CrousRestaurant;
import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.utils.CrousUtils;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Optional;

public class TaskSendCrousMenu implements Runnable {
    private final Raymond raymond;

    public TaskSendCrousMenu(Raymond raymond) {
        this.raymond = raymond;
    }

    @Override
    public void run() {
        raymond.getJda().getGuilds().forEach(guild -> {
            String restaurantName = raymond.getServerConfigsManager().getServerConfig(guild.getId()).getUnivServerConfig().getCrousRestaurantName();
            if (restaurantName == null) return;

            TextChannel crousMenuChannel = guild.getTextChannelById(raymond.getServerConfigsManager().getServerConfig(guild.getId()).getUnivServerConfig().getDiscordCrousMenuChannelId());
            if (crousMenuChannel == null) return;

            Role crousRole =
                    guild.getRoleById(raymond.getServerConfigsManager().getServerConfig(guild.getId()).getUnivServerConfig().getDiscordCrousMenuRoleId());
            if (crousRole == null) return;

            try {
                CrousRestaurant restaurantDetails = MultiHelper.getRestaurantDetails(raymond.getMultiClient(), restaurantName);
                Optional<CrousMenu> todaysMenu = CrousUtils.findTodaysMenu(restaurantDetails);
                if (todaysMenu.isEmpty()) {
                    crousMenuChannel.sendMessage("Aucun menu disponible pour aujourd'hui :(").queue();
                } else {
                    CrousMenu menu = todaysMenu.get();
                    EmbedCrafter embed = CrousUtils.buildEmbedFromCrous(restaurantDetails, menu);

                    crousMenuChannel.sendMessage(crousRole.getAsMention()).addEmbeds(embed.build()).queue();
                }
            } catch (Exception e) {
                raymond.getLogger().error("Failed to send CROUS menu for guild " + guild.getId() + " (" + guild.getName() + ")", e);
            }
        });
    }
}
