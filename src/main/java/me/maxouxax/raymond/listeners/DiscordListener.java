package me.maxouxax.raymond.listeners;

import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class DiscordListener implements EventListener {

    private final CommandMap commandMap;
    private final me.maxouxax.raymond.Raymond raymond;

    public DiscordListener(CommandMap commandMap){
        this.commandMap = commandMap;
        this.raymond = me.maxouxax.raymond.Raymond.getInstance();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof MessageReceivedEvent) onMessage((MessageReceivedEvent)event);
        if(event instanceof MessageReactionAddEvent) onReactionAdd((MessageReactionAddEvent)event);
        if(event instanceof MessageReactionRemoveEvent) onReactionRemove((MessageReactionRemoveEvent)event);
    }

    private void onReactionAdd(MessageReactionAddEvent event) {
    }

    private void onReactionRemove(MessageReactionRemoveEvent event) {
    }


    private void onMessage(MessageReceivedEvent event){
        if(event.isFromType(ChannelType.PRIVATE)){
            onDM(event);
            return;
        }
        if(event.getMessage().getAuthor().isBot()
                || event.getAuthor().equals(event.getJDA().getSelfUser())
                || event.getChannelType() == ChannelType.PRIVATE)return;

        String message = event.getMessage().getContentDisplay();
        if (message.startsWith(commandMap.getDiscordTag())) {
            message = message.replaceFirst(commandMap.getDiscordTag(), "");
            if (commandMap.discordCommandUser(event.getAuthor(), message, event.getMessage())) {
                event.getChannel().sendTyping().queue();
                event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
            }
        }
    }

    private void onDM(MessageReceivedEvent event){
        if(event.getAuthor().equals(event.getJDA().getSelfUser())) return;
        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setColor(Color.RED.getRGB());
        embedCrafter.setTitle("Private message received of " + event.getAuthor().getName());
        embedCrafter.setThumbnailUrl(raymond.getConfigurationManager().getStringValue("cancelIcon"));
        embedCrafter.setDescription("Cette action est **IMPOSSIBLE**");
        event.getChannel().sendMessageEmbeds(embedCrafter.build()).queue();
    }

}
