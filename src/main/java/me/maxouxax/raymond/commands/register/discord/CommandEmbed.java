package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.Command;
import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.commands.slashannotations.Option;
import me.maxouxax.raymond.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandEmbed {

    private final Raymond raymond;
    private final CommandMap commandMap;

    public CommandEmbed(CommandMap commandMap){
        this.commandMap = commandMap;
        this.raymond = Raymond.getInstance();
    }

    @Option(name = "titre", description = "Titre de l'embed", isRequired = true, type = OptionType.STRING)
    @Option(name = "description", description = "Description de l'embed", isRequired = true, type = OptionType.STRING)
    @Option(name = "lien-de-limage", description = "Image de l'embed", isRequired = false, type = OptionType.STRING)
    @Command(name="embed", power = 100, help = ".embed <titre>-²<description>-²<image (url)>", example = ".embed Ceci est une annonce-²Juste pour vous dire que les bananes c'est assez bon mais que la raclette reste au dessus.-²https://lien-de-l-image.fr/image32.png")
    public void embed(User user, TextChannel textChannel, SlashCommandInteractionEvent slashCommandEvent) {
        String title = slashCommandEvent.getOption("titre").getAsString();
        String description = slashCommandEvent.getOption("description").getAsString();
        String image = "";
        if(slashCommandEvent.getOption("lien-de-limage") != null)image = slashCommandEvent.getOption("lien-de-limage").getAsString();
        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setTitle(title, "https://lyor.in/twitch")
                .setColor(15844367)
                .setDescription(description);
        if (!image.equals("")) {
            embedCrafter.setImageUrl(image);
        }
        slashCommandEvent.getHook().sendMessageEmbeds(embedCrafter.build()).queue();
    }

}
