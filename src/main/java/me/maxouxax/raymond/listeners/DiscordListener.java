package me.maxouxax.raymond.listeners;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DiscordListener implements EventListener {

    private final Raymond raymond;

    public DiscordListener() {
        this.raymond = Raymond.getInstance();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof MessageReceivedEvent) onMessage((MessageReceivedEvent) event);
        if (event instanceof MessageReactionAddEvent) onReactionAdd((MessageReactionAddEvent) event);
        if (event instanceof MessageReactionRemoveEvent) onReactionRemove((MessageReactionRemoveEvent) event);
    }

    private void onReactionAdd(MessageReactionAddEvent event) {
    }

    private void onReactionRemove(MessageReactionRemoveEvent event) {
    }

    private void onMessage(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            onDM(event);
        }
    }

    private void onDM(MessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) return;
        EmbedCrafter embedCrafter = new EmbedCrafter(raymond);
        embedCrafter.setColor(Color.RED.getRGB());
        embedCrafter.setTitle("Private message received of " + event.getAuthor().getName());
        embedCrafter.setThumbnailUrl(raymond.getConfig().getEmbed().getCancelIcon());
        embedCrafter.setDescription("Cette action est **IMPOSSIBLE**");
        event.getChannel().sendMessageEmbeds(embedCrafter.build()).queue();
    }

}
