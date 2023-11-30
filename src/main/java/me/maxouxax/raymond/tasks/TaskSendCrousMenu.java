package me.maxouxax.raymond.tasks;

import me.maxouxax.multi4j.MultiHelper;
import me.maxouxax.multi4j.crous.CrousMenu;
import me.maxouxax.multi4j.crous.CrousRestaurant;
import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.utils.CrousUtils;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.Optional;

public class TaskSendCrousMenu implements Runnable {

    private final Raymond raymond;
    private final ArrayList<String> randomMessages = new ArrayList<>();

    public TaskSendCrousMenu(Raymond raymond) {
        this.raymond = raymond;
        this.randomMessages.add("On en dit quoi du menu d'aujourd'hui au %s ?");
        this.randomMessages.add("Ça m'a l'air pas mal le menu d'aujourd'hui au %s");
        this.randomMessages.add("J'ai faim, on mange au %s ?");
        this.randomMessages.add("Le menu du %s a l'air pas mal aujourd'hui");
        this.randomMessages.add("Le menu du %s est disponible !");
        this.randomMessages.add("Je vais me régaler au %s aujourd'hui");
        this.randomMessages.add("Je vais remplir mon ventre au %s aujourd'hui");
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
                    EmbedCrafter embed = CrousUtils.buildEmbedFromMenu(restaurantDetails, menu);

                    crousMenuChannel.sendMessage(crousRole.getAsMention()).addEmbeds(embed.build()).queue(message -> {
                        message.createThreadChannel("Discussion autour du menu").queue(threadChannel -> {
                            String randomMessage = randomMessages.get((int) (Math.random() * randomMessages.size()));
                            threadChannel.sendMessage(String.format(randomMessage, restaurantDetails.getName())).queue();
                        });
                    });
                }
            } catch (Exception e) {
                raymond.getLogger().error("Failed to send CROUS menu for guild " + guild.getId() + " (" + guild.getName() + ")", e);
            }
        });
    }
}
