package me.maxouxax.raymond;

import me.maxouxax.raymond.commands.register.console.CommandConsoleHelp;
import me.maxouxax.raymond.commands.register.console.CommandConsolePower;
import me.maxouxax.raymond.commands.register.console.CommandConsoleSay;
import me.maxouxax.raymond.commands.register.discord.*;
import me.maxouxax.raymond.config.RaymondConfig;
import me.maxouxax.raymond.config.RaymondServerConfigsManager;
import me.maxouxax.raymond.listeners.DiscordListener;
import me.maxouxax.raymond.schedule.UnivConnector;
import me.maxouxax.supervisor.commands.ConsoleCommand;
import me.maxouxax.supervisor.commands.DiscordCommand;
import me.maxouxax.supervisor.supervised.Supervised;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.EnumSet;
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
            jda = JDABuilder.create(getConfig().getDiscordToken(), EnumSet.allOf(GatewayIntent.class))
                    .build();
            jda.addEventListener(new DiscordListener());
            jda.getPresence().setActivity(Activity.playing(getConfig().getGameName()));
            jda.awaitReady();
            super.bindListeners();
        } catch (IllegalArgumentException | NullPointerException | InterruptedException e) {
            e.printStackTrace();
        }

        List<ConsoleCommand> consoleCommands = Arrays.asList(
                new CommandConsoleHelp(),
                new CommandConsolePower(),
                new CommandConsoleSay()
        );
        consoleCommands.forEach(consoleCommand -> supervisor.getCommandManager().registerConsoleCommand(consoleCommand));

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
