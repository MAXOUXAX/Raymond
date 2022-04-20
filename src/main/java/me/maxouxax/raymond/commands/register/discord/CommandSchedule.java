package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.schedule.UnivClass;
import me.maxouxax.raymond.schedule.UnivHelper;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.commands.slashannotations.Option;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CommandSchedule implements DiscordCommand {

    @Override
    @Option(name = "jour", description = "Jour de la semaine", isRequired = true, type = OptionType.STRING)
    public void onCommand(TextChannel textChannel, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        String jour = slashCommandInteractionEvent.getOption("jour").getAsString();
        String[] split = jour.split("/");
        if (split.length != 3) {
            slashCommandInteractionEvent.reply("Format de date invalide, il doit être au format DD/MM/YYYY").setEphemeral(true).queue();
        } else {
            int day = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            int year = Integer.parseInt(split[2]);
            if (day < 1 || day > 31 || month < 1 || month > 12 || year < 2020 || year > 2024) {
                slashCommandInteractionEvent.reply("Date invalide, elle doit être au format DD/MM/YYYY").queue();
            } else {
                slashCommandInteractionEvent.deferReply().queue();
                GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month - 1, day);
                Date date = gregorianCalendar.getTime();
                gregorianCalendar.add(Calendar.HOUR, 23);
                Date midnight = gregorianCalendar.getTime();
                ArrayList<UnivClass> univSchedule = UnivHelper.getUnivSchedule(date, midnight);
                if (univSchedule.size() == 0) {
                    EmbedCrafter embedCrafter = new EmbedCrafter(Raymond.getInstance());
                    embedCrafter.setColor(Color.RED);
                    embedCrafter.setTitle("Aucun cours :x:");
                    embedCrafter.setDescription("Aucun cours n'est prévu à cette date");
                    slashCommandInteractionEvent.getHook().sendMessageEmbeds(embedCrafter.build()).queue();
                } else {
                    slashCommandInteractionEvent.getHook().sendMessage("Récupération des cours...").queue();

                    List<MessageAction> messageActions = univSchedule.stream().map(univClass -> slashCommandInteractionEvent.getTextChannel()
                            .sendMessageEmbeds(univClass.toEmbed().build())
                            .setActionRows(univClass.toActionRow()))
                            .toList();

                    RestAction.allOf(messageActions).queue(messages -> {
                        slashCommandInteractionEvent.getHook().deleteOriginal().queue();
                    });
                }

            }
        }
    }

    @Override
    public int power() {
        return 10;
    }

    @Override
    public String name() {
        return "edt";
    }

    @Override
    public String description() {
        return "Permet de récupérer l'emploi du temps d'une journée";
    }

}
