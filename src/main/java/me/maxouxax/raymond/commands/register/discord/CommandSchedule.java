package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.schedule.UnivClass;
import me.maxouxax.raymond.schedule.UnivHelper;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.commands.slashannotations.Option;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

import java.awt.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class CommandSchedule implements DiscordCommand {

    @Override
    @Option(name = "date", description = "Date (ex. 12/01/2022) dont vous souhaitez consulter l'emploi du temps", isRequired = true, type = OptionType.STRING)
    public void onCommand(MessageChannelUnion textChannel, Member member, SlashCommandInteractionEvent slashCommandInteractionEvent) {
        String dateInput = slashCommandInteractionEvent.getOption("date").getAsString();
        String[] split = dateInput.split("/");
        if (split.length != 3) {
            slashCommandInteractionEvent.reply("Format de date invalide, il doit être au format DD/MM/YYYY").setEphemeral(true).queue();
        } else {
            int day = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            int year = Integer.parseInt(split[2]);
            if (split[2].length() == 2) year = Integer.parseInt("20" + split[2]);
            if (day < 1 || day > 31 || month < 1 || month > 12 || year < 2020 || year > 2099) {
                slashCommandInteractionEvent.reply("Format de date invalide, il doit être au format DD/MM/YYYY").setEphemeral(true).queue();
            } else {
                slashCommandInteractionEvent.deferReply().queue();
                GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month - 1, day);
                ZonedDateTime date = gregorianCalendar.toZonedDateTime();
                gregorianCalendar.add(Calendar.HOUR, 23);
                ZonedDateTime midnight = gregorianCalendar.toZonedDateTime();
                ArrayList<UnivClass> univSchedule = UnivHelper.getUnivSchedule(date, midnight);
                if (univSchedule.size() == 0) {
                    EmbedCrafter embedCrafter = new EmbedCrafter(Raymond.getInstance());
                    embedCrafter.setColor(Color.RED);
                    embedCrafter.setTitle("Aucun cours :x:");
                    embedCrafter.setDescription("Aucun cours n'est prévu à cette date");
                    slashCommandInteractionEvent.getHook().sendMessageEmbeds(embedCrafter.build()).queue();
                } else {
                    slashCommandInteractionEvent.getHook().sendMessage("Récupération des cours...").queue();

                    List<MessageCreateAction> messageActions = univSchedule.stream().map(univClass -> {
                        MessageCreateAction messageCreateAction = slashCommandInteractionEvent.getChannel()
                                .sendMessageEmbeds(univClass.toEmbed().build());

                        univClass.toActionRow().forEach(itemComponents -> messageCreateAction.addActionRow(itemComponents.getComponents()));
                        return messageCreateAction;
                    }).toList();

                    RestAction.allOf(messageActions).queue(messages -> {
                        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM").withLocale(Locale.FRANCE);
                        slashCommandInteractionEvent.getHook().editOriginal("Voici les cours de la journée du " + dayFormatter.format(date) + " :tada: ").queue();
                    });
                }
            }
        }
    }

    @Override
    public int power() {
        return 0;
    }

    @Override
    public String name() {
        return "emploidutemps";
    }

    @Override
    public String description() {
        return "Permet de récupérer l'emploi du temps d'une journée";
    }

}
