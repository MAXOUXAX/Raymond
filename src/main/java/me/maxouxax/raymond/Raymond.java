package me.maxouxax.raymond;

import me.maxouxax.raymond.commands.register.console.CommandConsoleHelp;
import me.maxouxax.raymond.commands.register.console.CommandConsolePower;
import me.maxouxax.raymond.commands.register.console.CommandConsoleSay;
import me.maxouxax.raymond.commands.register.discord.*;
import me.maxouxax.raymond.config.RaymondConfig;
import me.maxouxax.raymond.config.RaymondServerConfigsManager;
import me.maxouxax.raymond.listeners.DiscordListener;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.supervised.Supervised;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class Raymond extends Supervised {

    private static Raymond instance;

    private ScheduledExecutorService scheduledExecutorService;

    private final String version;

    public Raymond() {
        instance = this;
        this.version = this.getDescription().getVersion();
        this.serverConfigsManager = new RaymondServerConfigsManager();
    }

    public String getVersion() {
        return version;
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();

        loadConfig(RaymondConfig.class);

        try {
            jda = JDABuilder.create(getConfig().getDiscordToken(), GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.DIRECT_MESSAGE_TYPING,
                            GatewayIntent.DIRECT_MESSAGES,
                            GatewayIntent.GUILD_BANS,
                            GatewayIntent.GUILD_EMOJIS,
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_INVITES,
                            GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_MESSAGE_TYPING,
                            GatewayIntent.GUILD_PRESENCES,
                            GatewayIntent.GUILD_VOICE_STATES)
                    .build();
            jda.addEventListener(new DiscordListener());
            jda.getPresence().setActivity(Activity.playing(getConfig().getGameName()));
            jda.awaitReady();
            super.bindListeners();
        } catch (LoginException | IllegalArgumentException | NullPointerException | InterruptedException e) {
            e.printStackTrace();
        }

        supervisor.getCommandManager().registerConsoleCommand(new CommandConsoleHelp());
        supervisor.getCommandManager().registerConsoleCommand(new CommandConsoleSay());
        supervisor.getCommandManager().registerConsoleCommand(new CommandConsolePower());

        List<DiscordCommand> discordCommands = Arrays.asList(
                new CommandArchive(),
                new CommandDelete(),
                new CommandEmbed(),
                new CommandGame(),
                new CommandInfo(),
                new CommandPing(this),
                new CommandPower(),
                new CommandSendRules(this),
                new CommandUnarchive(),
                new CommandVersion(this)
        );
        discordCommands.forEach(discordCommand -> supervisor.getCommandManager().registerCommand(this, discordCommand));

        supervisor.getCommandManager().updateCommands(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onDisable() {
        jda.getPresence().setActivity(Activity.playing("Arrêt en cours..."));
        supervisor.getLogger().info("--------------- STOPPING ---------------");
        supervisor.getLogger().info("> Shutdowning...");
        jda.shutdown();
        supervisor.getLogger().info("> JDA shutdowned!");
        supervisor.getLogger().info("--------------- STOPPING ---------------");
        supervisor.getLogger().info("Arrêt du BOT réussi");
    }

    public static Raymond getInstance() {
        return instance;
    }

    @Override
    public RaymondServerConfigsManager getServerConfigsManager() {
        return (RaymondServerConfigsManager) this.serverConfigsManager;
    }

}
