package me.maxouxax.raymond.utils;

import me.maxouxax.multi4j.crous.CrousFoodCategory;
import me.maxouxax.multi4j.crous.CrousMeal;
import me.maxouxax.multi4j.crous.CrousMenu;
import me.maxouxax.multi4j.crous.CrousRestaurant;
import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.utils.EmbedCrafter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class CrousUtils {

    private static final Raymond raymond = Raymond.getInstance();

    public static EmbedCrafter buildEmbedFromMenu(CrousRestaurant restaurant, CrousMenu menu) {
        EmbedCrafter embedCrafter = new EmbedCrafter(raymond);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.FRANCE);

        LocalDate menuDate = menu.getDate().toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();

        embedCrafter.setTitle("Menu du " + format.format(menuDate) + " | " + restaurant.getName());
        embedCrafter.setThumbnailUrl(restaurant.getThumbnailUrl());

        StringBuilder description = new StringBuilder();
        for (CrousMeal meal : menu.getMeals()) {
            description.append("## ⏲️ Menu du ").append(meal.getName()).append("\n");
            for (CrousFoodCategory foodCategory : meal.getFoodCategories()) {
                description.append("### ➡️ ").append(foodCategory.getName()).append("\n");
                for (String dish : foodCategory.getDishes()) {
                    // Skip empty dishes
                    if (dish.trim().equalsIgnoreCase("")) continue;

                    description.append("- ").append(dish).append("\n");
                }
            }
        }
        embedCrafter.setDescription(description.toString());
        return embedCrafter;
    }

    public static Optional<CrousMenu> findTodaysMenu(CrousRestaurant restaurant) {
        return restaurant.getMenus().stream().filter(crousMenu -> {
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            Date menuDate = crousMenu.getDate();
            Calendar menuCalendar = Calendar.getInstance();
            menuCalendar.setTime(menuDate);

            return calendar.get(Calendar.DAY_OF_YEAR) == menuCalendar.get(Calendar.DAY_OF_YEAR);
        }).findFirst();
    }

}
