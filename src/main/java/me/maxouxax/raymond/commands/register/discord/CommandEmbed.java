package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.commands.slashannotations.Option;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandEmbed implements DiscordCommand {

    @Override
    @Option(name = "titre", description = "Titre de l'embed", isRequired = true, type = OptionType.STRING)
    @Option(name = "description", description = "Description de l'embed", isRequired = true, type = OptionType.STRING)
    @Option(name = "lien-de-limage", description = "Image de l'embed", isRequired = false, type = OptionType.STRING)
    public void onCommand(MessageChannelUnion textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        String title = messageContextInteractionEvent.getOption("titre").getAsString();
        String description = messageContextInteractionEvent.getOption("description").getAsString();
        String image = "";
        if (messageContextInteractionEvent.getOption("lien-de-limage") != null)
            image = messageContextInteractionEvent.getOption("lien-de-limage").getAsString();
        EmbedCrafter embedCrafter = new EmbedCrafter(Raymond.getInstance());
        embedCrafter.setTitle(title, "https://lyor.in/twitch")
                .setColor(15844367)
                .setDescription(description);
        if (!image.equals("")) {
            embedCrafter.setImageUrl(image);
        }
        messageContextInteractionEvent.replyEmbeds(embedCrafter.build()).queue();
    }

    @Override
    public int power() {
        return 100;
    }

    @Override
    public String name() {
        return "embed";
    }

    @Override
    public String description() {
        return "Permet d'envoyer un Embed avec un titre, une description et une image";
    }
}
