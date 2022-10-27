package me.maxouxax.raymond.commands.register.discord;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.RaymondServerConfig;
import me.maxouxax.supervisor.Supervisor;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandSendRules implements DiscordCommand {

    private final Raymond raymond;

    public CommandSendRules(Raymond raymond) {
        this.raymond = raymond;
    }

    @Override
    public String name() {
        return "sendrules";
    }

    @Override
    public String description() {
        return "Permet d'envoyer les règles dans le salon destiné";
    }

    @Override
    public void onRootCommand(MessageChannelUnion textChannel, Member member, SlashCommandInteractionEvent messageContextInteractionEvent) {
        if(!textChannel.getType().isGuild()) return;
        GuildMessageChannel guildChannel = textChannel.asGuildMessageChannel();

        messageContextInteractionEvent.reply("Envoi des règles en cours...").setEphemeral(true).queue();
        RaymondServerConfig raymondServerConfig = raymond.getServerConfigsManager().getServerConfig(guildChannel.getGuild().getId());

        EmbedCrafter embedCrafter = new EmbedCrafter(raymond)
                .setImageUrl(raymondServerConfig.getRulesBanner() + "?size=1000")
                .setColor(2895667)
                .noFooter();
        EmbedCrafter embedCrafterRules = new EmbedCrafter(raymond)
                .setTitle("Règles")
                .setColor(15105570)
                .setThumbnailUrl(raymondServerConfig.getRulesThumbnail())
                .setDescription(":small_orange_diamond: 1. **Traitez tout le monde avec respect**. " +
                        "Aucun harcèlement, sexisme, racisme ou discours de haine ne sera toléré. " +
                        "**Cette règle s'applique aux pseudonymes ainsi qu'aux statuts personnalisés !**\n\n" +

                        ":small_orange_diamond: 2. **Pas de spam ni d'autopromotion** (invitations de serveurs" +
                        ", publicités, etc.) sans l'autorisation d'un modérateur du serveur, y compris via les messages " +
                        "privés envoyés aux autres membres.\n\n" +

                        ":small_orange_diamond: 3. **Pas de contenu violent, obscène ou NSFW**, qu'il s'agisse " +
                        "de texte, d'images ou de liens mettant en scène de la nudité, du sexe, de l'hyperviolence " +
                        "ou un quelconque contenu dérangeant, et ce, **y compris dans les photos de profil**.\n\n" +

                        ":small_orange_diamond: 4. **Pas de majuscules abusives ou de spam/flood**\n\n" +

                        ":small_orange_diamond: 5. **Pas de pseudos incorrects ou remplis d'émoticônes ou de " +
                        "caractères spéciaux** empêchant de vous mentionner\n\n" +

                        ":small_orange_diamond: 6. Si tu remarques quelque chose de contraire aux règles ou qui " +
                        "te met dans un sentiment d'insécurité, informe-en les modérateurs. Nous voulons que ce " +
                        "serveur soit accueillant pour tout le monde !")
                .noFooter();
        EmbedCrafter embedCrafterModeration = new EmbedCrafter(raymond)
                .setTitle("Modération")
                .setThumbnailUrl(raymondServerConfig.getRulesModerationThumbnail())
                .setColor(3066993)
                .setDescription("En rejoignant ce serveur Discord, vous acceptez que notre équipe de modération puisse vous sanctionner à tout moment si vous enfreignez une des règles précédemment citées")
                .noFooter();
        EmbedCrafter embedCrafterWarning = new EmbedCrafter(raymond)
                .setTitle("Attention !")
                .setColor(15158332)
                .setThumbnailUrl(raymondServerConfig.getRulesAttentionThumbnail())
                .setDescription("Si vous ne respectez pas une des règles précédemment citées, vous recevrez un avertissement.\n\nAu bout de 3 avertissements, vous serez banni définitivement du serveur par notre équipe de modération.\n\n**DE PLUS**, si vous commettez une sanction très grave, l'équipe de modération se réserve le droit de vous bannir sans avertissement.")
                .setFooter("Dernière mise à jour des règles");
        TextChannel rulesTextChannel = guildChannel.getGuild().getTextChannelById(raymondServerConfig.getRulesTextChannelId());
        if (rulesTextChannel == null) {
            Supervisor.getInstance().getErrorHandler().handleException(new Exception("textChannel == null (the textchannel id or the guildid (or both) may not have been set in the config file)"));
        } else {
            rulesTextChannel.sendMessageEmbeds(embedCrafter.build(), embedCrafterRules.build(), embedCrafterModeration.build(), embedCrafterWarning.build()).queue(message -> {
                messageContextInteractionEvent.getHook().editOriginal("Envoi des règles terminé !").queue();
            });
        }
    }

    @Override
    public int power() {
        return 150;
    }

}
