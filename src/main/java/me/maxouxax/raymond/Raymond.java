package me.maxouxax.raymond;

import me.maxouxax.raymond.commands.register.console.CommandConsoleHelp;
import me.maxouxax.raymond.commands.register.console.CommandConsolePower;
import me.maxouxax.raymond.commands.register.console.CommandConsoleSay;
import me.maxouxax.raymond.commands.register.discord.*;
import me.maxouxax.raymond.config.RaymondConfig;
import me.maxouxax.raymond.config.RaymondServerConfigsManager;
import me.maxouxax.raymond.listeners.DiscordListener;
import me.maxouxax.raymond.schedule.UnivConnector;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.supervised.Supervised;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class Raymond extends Supervised {

    private static Raymond instance;
    private String version;
    private UnivConnector univConnector;
    private ScheduledExecutorService scheduledExecutorService;

    public static Raymond getInstance() {
        return instance;
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
        instance = this;
        this.serverConfigsManager = new RaymondServerConfigsManager();
        super.onEnable();

        saveDefaultConfig();
        loadConfig(RaymondConfig.class);

        this.version = this.getDescription().getVersion();
        this.univConnector = new UnivConnector();

        try {
            this.univConnector.connect();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }

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
                new CommandSchedule(),
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
        jda.shutdown();
    }

    @Override
    public RaymondConfig getConfig() {
        return (RaymondConfig) super.getConfig();
    }

    @Override
    public RaymondServerConfigsManager getServerConfigsManager() {
        return (RaymondServerConfigsManager) this.serverConfigsManager;
    }

    public UnivConnector getUnivConnector() {
        return univConnector;
    }

}
