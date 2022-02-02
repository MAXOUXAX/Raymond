package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.Command;
import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.utils.EmbedCrafter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class CommandDefault {

    private final Raymond raymond;
    private final CommandMap commandMap;

    public CommandDefault(CommandMap commandMap){
        this.commandMap = commandMap;
        this.raymond = Raymond.getInstance();
    }

    @Command(name="stop",type= Command.ExecutorType.CONSOLE)
    private void stop(){
        raymond.setRunning(false);
    }

    @Command(name="power",power=150, description = "Permet de définir le power d'un utilisateur", example = "power 150 @MAXOUXAX", help = "power <power> <@user>")
    private void power(User user, MessageChannel channel, Message message, String[] args){
        MessageEmbed helperEmbed = commandMap.getHelpEmbed("power");
        if(args.length == 0 || message.getMentionedUsers().size() == 0){
            channel.sendMessageEmbeds(helperEmbed).queue();
        }
        int power = 0;
        try{
            power = Integer.parseInt(args[0]);
        }catch(NumberFormatException nfe){
            channel.sendMessageEmbeds(helperEmbed).queue();
        }

        User target = message.getMentionedUsers().get(0);
        commandMap.setUserPower(target, power);
        channel.sendMessage("Le power de "+target.getAsMention()+" est maintenant de "+power).queue();
    }

    @Command(name="game",power=100,description = "Permet de modifier le jeu du BOT.", help = "game <jeu>", example = "game planter des tomates")
    private void game(TextChannel textChannel, JDA jda, String[] args){
        MessageEmbed helperEmbed = commandMap.getHelpEmbed("game");
        if(args.length == 0){
            textChannel.sendMessageEmbeds(helperEmbed).queue();
        }else {
            StringBuilder builder = new StringBuilder();
            for (String str : args) {
                if (builder.length() > 0) builder.append(" ");
                builder.append(str);
            }

            jda.getPresence().setActivity(Activity.playing(builder.toString()));
        }
    }

    @Command(name="delete",power=50,description = "Permet de nettoyer un nombre x de message du salon", example = "delete 50", help = "delete <nombre de message>")
    private void delete(TextChannel textChannel, JDA jda, String[] args){
        if (getInt(args[0]) <= 100) {
            List<Message> msgs;
            MessageHistory history = new MessageHistory(textChannel);
            msgs = history.retrievePast(getInt(args[0])).complete();
            textChannel.deleteMessages(msgs).queue();
            textChannel.sendMessage("Suppression de " + args[0] + " messages terminée !").queue();
        }
    }

    private int getInt(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (Exception e) {
            return 0;
        }

    }

    @Command(name = "info",description = "Permet d'obtenir des informations sur un membre",type = Command.ExecutorType.USER, help = "info <@user>", example = "info @MAXOUXAX#2233")
    private void info(User user, Guild guild, TextChannel textChannel, String[] args, Message message) {
        User infoUser;
        if(message.getMentionedUsers().size() > 0){
            infoUser = message.getMentionedUsers().get(0);
        }else{
            infoUser = user;
        }

        String name = infoUser.getName();
        String tag = infoUser.getName() + "#" + infoUser.getDiscriminator();
        String guildJoinDate = "-/-";
        String status = "-/-";
        String avatar = infoUser.getAvatarUrl();
        String discordJoinDate = infoUser.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String id = infoUser.getId();
        String roles = "-/-";
        String game = "-/-";
        if (guild.isMember(infoUser)) {
            Member infoMember = guild.getMember(infoUser);
            if (infoMember != null) {
                guildJoinDate = infoMember.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
                status = infoMember.getOnlineStatus().getKey();
                game = infoMember.getActivities().get(0).getName();
                Iterator<Role> it = infoMember.getRoles().iterator();
                StringBuilder stringBuilder = new StringBuilder();
                while(it.hasNext()){
                    Role role = it.next();
                    stringBuilder.append(role.getAsMention());
                    if (it.hasNext())stringBuilder.append(", ");
                }
                roles = stringBuilder.toString();
            }
        }

        EmbedCrafter em = new EmbedCrafter().setColor(Color.GREEN);
        em.setDescription(":spy: **Informations sur " + infoUser.getName() + ":**")
                .addField("Nom", name, true)
                .addField("Tag", tag, true)
                .addField("ID", id, true)
                .addField("Statut", status, true)
                .addField("Joue à", game, true)
                .addField("Rôles", roles, true)
                .addField("A rejoint le serveur le", guildJoinDate, true)
                .addField("A rejoint Discord le", discordJoinDate, true)
                .addField("URL de l'avatar", avatar, true)
                .setThumbnailUrl(avatar);
        textChannel.sendMessageEmbeds(em.build()).queue();
    }

    @Command(name = "ping", description = "Permet de récupérer le ping du bot", type = Command.ExecutorType.USER, example = "ping", help = "ping")
    private void ping(TextChannel textChannel, User user, Guild guild){
        long ping = guild.getJDA().getGatewayPing();
        EmbedCrafter builder = new EmbedCrafter()
                .setTitle("Discord API ping", raymond.getConfigurationManager().getStringValue("websiteUrl"))
                .setThumbnailUrl(user.getAvatarUrl()+"?size=256")
                .addField(new MessageEmbed.Field("Ping", ping+"ms", true));
        if(ping > 300) {
            builder.setColor(Color.RED);
            builder.setDescription("Mauvais ping");
        }else{
            builder.setColor(Color.GREEN);
            builder.setDescription("Bon ping");
        }
        textChannel.sendMessageEmbeds(builder.build()).queue();
    }

}