package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.commands.Command;
import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandEmbed {

    private final me.maxouxax.raymond.Raymond raymond;
    private final CommandMap commandMap;

    public CommandEmbed(CommandMap commandMap){
        this.commandMap = commandMap;
        this.raymond = me.maxouxax.raymond.Raymond.getInstance();
    }

    @Command(name="embed",type = Command.ExecutorType.ALL,power = 100,help = ".embed <titre>-²<description>-²<image (url)>",example = ".embed Ceci est une annonce-²Juste pour vous dire que les bananes c'est assez bon mais que la raclette reste au dessus.-²https://lien-de-l-image.fr/image32.png")
    public void embed(User user, TextChannel textChannel, String[] args) {
        try {
            MessageEmbed em = commandMap.getHelpEmbed("embed");
            if (args.length == 0) {
                textChannel.sendMessageEmbeds(em).queue();
            } else {
                StringBuilder str = new StringBuilder();

                for (String arg : args) {
                    str.append(arg).append(" ");
                }

                if (!str.toString().contains("-²")) {
                    textChannel.sendMessageEmbeds(em).queue();
                    return;
                }

                String[] argsReal = str.toString().split("-²");

                String title = argsReal[0];

                String description = "Aucune description n'a été fournie !";
                if (argsReal.length >= 2) {
                    description = argsReal[1];
                }

                String imageURL = null;
                if (argsReal.length == 3) {
                    imageURL = argsReal[2];
                }

                EmbedCrafter embedCrafter = new EmbedCrafter()
                        .setTitle(title, "https://sti2d.best/")
                        .setColor(15844367)
                        .setDescription(description);
                if (imageURL != null) {
                    embedCrafter.setImageUrl(imageURL);
                }
                textChannel.sendMessageEmbeds(embedCrafter.build()).queue();
            }
        }catch (Exception e){
            raymond.getErrorHandler().handleException(e);
        }
    }

}
