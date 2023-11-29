package me.maxouxax.raymond.tasks;

import me.maxouxax.multi4j.MultiHelper;
import me.maxouxax.multi4j.crous.CrousFoodCategory;
import me.maxouxax.multi4j.crous.CrousMeal;
import me.maxouxax.multi4j.crous.CrousMenu;
import me.maxouxax.multi4j.crous.CrousRestaurant;
import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Calendar;
import java.util.Date;
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

            try {
                CrousRestaurant restaurantDetails = MultiHelper.getRestaurantDetails(raymond.getMultiClient(), restaurantName);
                Optional<CrousMenu> todaysMenu = restaurantDetails.getMenus().stream().filter(crousMenu -> {
                    Date today = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(today);

                    Date menuDate = crousMenu.getDate();
                    Calendar menuCalendar = Calendar.getInstance();
                    menuCalendar.setTime(menuDate);

                    return calendar.get(Calendar.DAY_OF_YEAR) == menuCalendar.get(Calendar.DAY_OF_YEAR);
                }).findFirst();
                if (todaysMenu.isEmpty()) {
                    crousMenuChannel.sendMessage("Aucun menu disponible pour aujourd'hui :(").queue();
                } else {
                    CrousMenu menu = todaysMenu.get();
                    EmbedCrafter embedCrafter = new EmbedCrafter(raymond);
                    embedCrafter.setTitle("Menu du " + menu.getDate().toString() + " | " + restaurantDetails.getName());
                    embedCrafter.setThumbnailUrl(restaurantDetails.getThumbnailUrl());

                    StringBuilder description = new StringBuilder();
                    for (CrousMeal meal : menu.getMeals()) {
                        // meal could either be morning, lunch or dinner
                        for (CrousFoodCategory foodCategory : meal.getFoodCategories()) {
                            // food category could either be "self" or other rooms inside the restaurant
                            description.append("## Menu du ").append(meal.getName()).append("\n");
                            description.append("### ").append(foodCategory.getName()).append("\n");
                            for (String dish : foodCategory.getDishes()) {
                                if(dish.trim().equalsIgnoreCase("")) continue;

                                description.append("- ").append(dish).append("\n");
                            }
                        }
                    }
                    embedCrafter.setDescription(description.toString());

                    crousMenuChannel.sendMessageEmbeds(embedCrafter.build()).queue();
                }
            } catch (Exception e) {
                raymond.getLogger().error("Failed to send CROUS menu for guild " + guild.getId() + " (" + guild.getName() + ")", e);
            }
        });
    }
}
