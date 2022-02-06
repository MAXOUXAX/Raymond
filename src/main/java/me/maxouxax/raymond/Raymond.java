package me.maxouxax.raymond;

import me.maxouxax.raymond.commands.CommandMap;
import me.maxouxax.raymond.database.DatabaseManager;
import me.maxouxax.raymond.listeners.DiscordListener;
import me.maxouxax.raymond.serversconfig.ServerConfigsManager;
import me.maxouxax.raymond.utils.ErrorHandler;
import me.maxouxax.raymond.utils.GlobalConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Raymond implements Runnable{

    private static Raymond instance;
    private static JDA jda;
    private final CommandMap commandMap;
    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger;
    private final ErrorHandler errorHandler;
    private final GlobalConfigManager globalConfigManager;
    private final ServerConfigsManager serverConfigsManager;

    private ScheduledExecutorService scheduledExecutorService;

    private boolean running;
    private final String version;

    public Raymond() throws LoginException, IllegalArgumentException, NullPointerException, IOException, InterruptedException, SQLException {
        instance = this;
        this.logger = org.slf4j.LoggerFactory.getLogger(Raymond.class);
        this.errorHandler = new ErrorHandler();

        DatabaseManager.initDatabaseConnection(Paths.get("configs/database.yml"));
        DatabaseManager.initDatabaseConnection(Paths.get("configs/database_mongo.yml"));

        String string = new File(Raymond.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
        string = string.replaceAll("Raymond-", "")
                .replaceAll(".jar", "");
        this.version = string;

        this.globalConfigManager = new GlobalConfigManager();
        this.serverConfigsManager = new ServerConfigsManager(this);

        logger.info("--------------- STARTING ---------------");

        logger.info("> Generated new BOT instance");
        logger.info("> BOT thread started, loading libraries...");
        this.commandMap = new CommandMap();
        logger.info("> Libraries loaded! Loading JDA...");

        loadDiscord();
        commandMap.updateCommands();
        logger.info("> JDA loaded!");

        this.scheduledExecutorService = Executors.newScheduledThreadPool(16);

        logger.info("> The BOT is now good to go !");
        logger.info("--------------- STARTING ---------------");
    }

    private void loadDiscord() throws LoginException, InterruptedException {
        jda = JDABuilder.create(globalConfigManager.getStringValue("botToken"), GatewayIntent.GUILD_MESSAGES,
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
        jda.addEventListener(new DiscordListener(commandMap));
        jda.getPresence().setActivity(Activity.playing(globalConfigManager.getStringValue("gameName")));
        jda.awaitReady();
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public JDA getJda() {
        return jda;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            if (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                commandMap.consoleCommand(nextLine);
            }
        }

        jda.getPresence().setActivity(Activity.playing("Arrêt en cours..."));
        logger.info("--------------- STOPPING ---------------");
        logger.info("> Shutdowning...");
        scanner.close();
        logger.info("> Scanner closed!");
        jda.shutdown();
        logger.info("> JDA shutdowned!");
        DatabaseManager.closeDatabasesConnection();
        logger.info("> Closed database connection!");
        logger.info("--------------- STOPPING ---------------");
        logger.info("Arrêt du BOT réussi");
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            Raymond Raymond = new Raymond();
            new Thread(Raymond, "raymond").start();
        } catch (LoginException | IllegalArgumentException | NullPointerException | IOException | InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public String getVersion() {
        return version;
    }

    public GlobalConfigManager getGlobalConfigManager() {
        return globalConfigManager;
    }

    public ServerConfigsManager getServerConfigsManager() {
        return serverConfigsManager;
    }

    public static Raymond getInstance(){
        return instance;
    }

}
