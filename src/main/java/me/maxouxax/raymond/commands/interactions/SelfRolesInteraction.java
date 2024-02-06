package me.maxouxax.raymond.commands.interactions;

import me.maxouxax.supervisor.interactions.message.DiscordMessageInteraction;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.components.Component;

public class SelfRolesInteraction implements DiscordMessageInteraction {

    @Override
    public String id() {
        return "self-roles";
    }

    @Override
    public String name() {
        return "self-roles";
    }

    @Override
    public String description() {
        return "Gérez vos rôles";
    }

    @Override
    public void onInteraction(GenericComponentInteractionCreateEvent event) {
        if (event.getInteraction().getComponent().getType() == Component.Type.BUTTON) {
            event.deferReply().queue();
            event.getHook().editOriginal("Vous avez cliqué sur le bouton " + event.getInteraction().getComponent().getId()).queue();
        }
    }

}
