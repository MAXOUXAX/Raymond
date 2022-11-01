package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.interactions.annotations.Option;
import me.maxouxax.supervisor.interactions.commands.DiscordCommand;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class CommandInfo implements DiscordCommand {

    @Override
    public String name() {
        return "info";
    }

    @Override
    public String description() {
        return "Permet d'obtenir des informations sur un membre";
    }

    @Override
    @Option(name = "utilisateur", description = "Utilisateur duquel les informations seront récupérées", type = OptionType.USER, required = true)
    public void onRootCommand(MessageChannelUnion textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        Member memberTo = messageContextInteractionEvent.getOption("utilisateur").getAsMember();
        String name = memberTo.getEffectiveName();
        String tag = memberTo.getUser().getName() + "#" + memberTo.getUser().getDiscriminator();
        String guildJoinDate = memberTo.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String discordJoinDate = memberTo.getUser().getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String id = memberTo.getUser().getId();
        String status = memberTo.getOnlineStatus().getKey();
        String roles = "";
        String game;
        String avatar = memberTo.getUser().getAvatarUrl();

        try {
            game = memberTo.getActivities().get(0).getName();
        } catch (Exception e) {
            game = "-/-";
        }

        for (Role r : memberTo.getRoles()) {
            roles += r.getName() + ", ";
        }
        if (roles.length() > 0)
            roles = roles.substring(0, roles.length() - 2);
        else
            roles = "Aucun rôle.";

        if (avatar == null) {
            avatar = "Pas d'avatar";
        }

        EmbedCrafter embedCrafter = new EmbedCrafter(Raymond.getInstance()).setColor(Color.GREEN);
        embedCrafter.setDescription(":spy:   **Informations sur " + memberTo.getUser().getName() + ":**")
                .addField("Nom", name, true)
                .addField("Tag", tag, true)
                .addField("ID", id, true)
                .addField("Statut", status, true)
                .addField("Joue à", game, true)
                .addField("Rôles", roles, true)
                .addField("A rejoint le serveur le", guildJoinDate, true)
                .addField("A rejoint discord le", discordJoinDate, true)
                .addField("URL de l'avatar", avatar, true);
        if (!avatar.equals("Pas d'avatar")) {
            embedCrafter.setThumbnailUrl(avatar);
        }

        messageContextInteractionEvent.replyEmbeds(embedCrafter.build()).queue();
    }

    @Override
    public int power() {
        return 50;
    }

}
