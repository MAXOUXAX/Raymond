package me.maxouxax.raymond.commands.interactions;

public class SearchCommand {

    /*private final CommandMap commandMap;

    public SearchCommand(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @InteractionListener(id = "music-search-choose")
    private void choose(ButtonClickEvent buttonClickEvent){
        int index = Integer.parseInt(buttonClickEvent.getButton().getLabel());
        MessageEmbed messageEmbed = buttonClickEvent.getMessage().getEmbeds().get(0);
        String url = messageEmbed.getFields().get(index-1).getValue();
        buttonClickEvent.deferEdit().queue();
        MusicCommand.getManager().loadTrack(buttonClickEvent, url, buttonClickEvent.getUser());
    }

    @InteractionListener(id = "music-search-cancel")
    private void cancel(ButtonClickEvent buttonClickEvent){
        buttonClickEvent.getMessage().delete().queue();
        buttonClickEvent.reply("Recherche annulée !").setEphemeral(true).queue();
    }
    */

}
