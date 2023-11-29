package me.maxouxax.raymond;

import me.maxouxax.multi4j.MultiClient;
import me.maxouxax.multi4j.exceptions.MultiLoginException;
import me.maxouxax.raymond.commands.interactions.EventCreateMessageInteraction;
import me.maxouxax.raymond.commands.interactions.SelfRolesInteraction;
import me.maxouxax.raymond.commands.register.console.CommandConsoleHelp;
import me.maxouxax.raymond.commands.register.console.CommandConsolePower;
import me.maxouxax.raymond.commands.register.console.CommandConsoleSay;
import me.maxouxax.raymond.commands.register.discord.*;
import me.maxouxax.raymond.config.RaymondConfig;
import me.maxouxax.raymond.config.RaymondServerConfigsManager;
import me.maxouxax.raymond.helpers.ScheduledJobsHelper;
import me.maxouxax.raymond.listeners.DiscordListener;
import me.maxouxax.raymond.tasks.TaskSendCrousMenu;
import me.maxouxax.supervisor.interactions.commands.ConsoleCommand;
import me.maxouxax.supervisor.interactions.commands.DiscordCommand;
import me.maxouxax.supervisor.interactions.message.DiscordMessageInteraction;
import me.maxouxax.supervisor.interactions.modals.DiscordModalInteraction;
import me.maxouxax.supervisor.supervised.Supervised;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class Raymond extends Supervised {

    private static Raymond instance;
    private Logger logger;
    private String version;
    private MultiClient multiClient;
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
        Instant start = Instant.now();

        this.serverConfigsManager = new RaymondServerConfigsManager();
        this.logger = LoggerFactory.getLogger(Raymond.class);
        super.onEnable();

        saveDefaultConfig();
        loadConfig(RaymondConfig.class);

        this.version = this.getDescription().getVersion();
        this.multiClient = new MultiClient.Builder()
                .withMultiConfig(getConfig().getMultiConfig())
                .build();

        try {
            this.multiClient.connect();
            logger.info("Successfully connected to Multi4J");
        } catch (MultiLoginException e) {
            logger.error("An error occurred while connecting to Multi4J", e);
        }

        try {
            jda = JDABuilder.create(getConfig().getDiscordToken(), EnumSet.allOf(GatewayIntent.class))
                    .build();
            jda.addEventListener(new DiscordListener());
            jda.getPresence().setActivity(Activity.playing(getConfig().getGameName()));
            jda.awaitReady();
            super.bindListeners();
        } catch (IllegalArgumentException | NullPointerException | InterruptedException e) {
            logger.error("An error occurred while connecting to Discord", e);
        }

        List<ConsoleCommand> consoleCommands = Arrays.asList(
                new CommandConsoleHelp(),
                new CommandConsolePower(),
                new CommandConsoleSay()
        );
        consoleCommands.forEach(consoleCommand -> supervisor.getInteractionManager().registerConsoleCommand(consoleCommand));

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
                new CommandVersion(this),
                new CommandEvent(),
                new CommandCrous()
        );
        discordCommands.forEach(discordCommand -> supervisor.getInteractionManager().registerCommand(this, discordCommand));

        supervisor.getInteractionManager().updateCommands(this);

        List<DiscordMessageInteraction> discordMessageInteractions = List.of(
                new SelfRolesInteraction()
        );

        List<DiscordModalInteraction> discordModalInteractions = List.of(
                new EventCreateMessageInteraction()
        );

        discordMessageInteractions.forEach(discordMessageInteraction -> supervisor.getInteractionManager().registerMessageInteraction(this, discordMessageInteraction));
        discordModalInteractions.forEach(discordModalInteraction -> supervisor.getInteractionManager().registerModalInteraction(this, discordModalInteraction));

        TaskSendCrousMenu taskSendCrousMenu = new TaskSendCrousMenu(this);
        ScheduledJobsHelper.scheduleAt(10, 0, taskSendCrousMenu);

        Instant end = Instant.now();
        logger.info("Raymond is ready! (took {}s)", (end.getEpochSecond() - start.getEpochSecond()));
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onDisable() {
        jda.getPresence().setActivity(Activity.playing("Arrêt en cours..."));
        jda.shutdown();
        try {
            ScheduledJobsHelper.stop();
        } catch (InterruptedException e) {
            logger.error("An error occurred while stopping scheduled jobs", e);
        }
    }

    @Override
    public RaymondConfig getConfig() {
        return (RaymondConfig) super.getConfig();
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public RaymondServerConfigsManager getServerConfigsManager() {
        return (RaymondServerConfigsManager) this.serverConfigsManager;
    }

    public MultiClient getMultiClient() {
        return multiClient;
    }

}
