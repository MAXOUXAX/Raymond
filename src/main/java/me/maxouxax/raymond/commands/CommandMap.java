package me.maxouxax.raymond.commands;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.register.console.CommandConsoleSay;
import me.maxouxax.raymond.commands.register.discord.CommandDefault;
import me.maxouxax.raymond.commands.slashannotations.InteractionListener;
import me.maxouxax.raymond.commands.slashannotations.SimpleInteraction;
import me.maxouxax.raymond.database.DatabaseManager;
import me.maxouxax.raymond.database.Databases;
import me.maxouxax.raymond.database.sql.DatabaseAccess;
import me.maxouxax.raymond.utils.EmbedCrafter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Date;
import java.sql.*;
import java.util.*;

public final class CommandMap {

    private final Raymond raymond;

    private final Map<String, Long> powers = new HashMap<>();

    private final Map<String, SimpleCommand> discordCommands = new HashMap<>();
    private final Map<String, SimpleConsoleCommand> consoleCommands = new HashMap<>();
    private final Map<String, SimpleInteraction> interactions = new HashMap<>();
    private final String discordTag = ".";

    public CommandMap() {
        this.raymond = Raymond.getInstance();

        registerCommands(
                new CommandConsoleSay(this),
                new CommandDefault(this)
        );

        //registerInteraction(new SearchCommand(this));

        load();
    }

    private void load() {
        try {
            DatabaseAccess databaseAccess = (DatabaseAccess) DatabaseManager.getDatabaseAccess(Databases.MARIADB.getName());
            Connection connection = databaseAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");

            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                long power = resultSet.getLong("power");
                powers.put(id, power);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void savePower(String id, long power) throws SQLException {
        DatabaseAccess databaseAccess = (DatabaseAccess) DatabaseManager.getDatabaseAccess(Databases.MARIADB.getName());
        Connection connection = databaseAccess.getConnection();
        if (power > 0) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET power = ?, updated_at = ? WHERE id = ?");
            preparedStatement.setLong(1, power);
            preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
            preparedStatement.setString(3, id);

            final int updateCount = preparedStatement.executeUpdate();

            if (updateCount < 1) {
                PreparedStatement insertPreparedStatement = connection.prepareStatement("INSERT INTO users (id, power, updated_at) VALUES (?, ?, ?)");
                insertPreparedStatement.setString(1, id);
                insertPreparedStatement.setLong(2, power);
                insertPreparedStatement.setDate(3, new Date(System.currentTimeMillis()));
                insertPreparedStatement.execute();
            }
        } else {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            preparedStatement.setString(1, id);

            preparedStatement.execute();
        }
        connection.close();
    }

    public MessageEmbed getHelpEmbed(String command) {
        try {
            SimpleCommand command1 = discordCommands.get(command);
            EmbedCrafter embedCrafter = new EmbedCrafter();
            embedCrafter.setTitle("Aide » " + discordTag + command)
                    .setDescription(command1.getDescription())
                    .addField("Utilisation:", discordTag + command1.getHelp(), true)
                    .addField("Exemple:", discordTag + command1.getExemple(), true);
            return embedCrafter.build();
        } catch (Exception e) {
            raymond.getErrorHandler().handleException(e);
        }
        return new EmbedBuilder().build();
    }

    public void setUserPower(User user, long power) {
        if (power == 0) {
            powers.remove(user.getId());
        } else {
            powers.put(user.getId(), power);
        }
        try {
            savePower(user.getId(), power);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public long getPowerUser(Guild guild, User user) {
        if (guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) return 150;
        return powers.getOrDefault(user.getId(), 0L);
    }

    public String getDiscordTag() {
        return discordTag;
    }

    public Collection<SimpleCommand> getDiscordCommands() {
        return discordCommands.values();
    }

    public Collection<SimpleConsoleCommand> getConsoleCommands() {
        return consoleCommands.values();
    }

    public void registerCommands(Object... objects) {
        for (Object object : objects) {
            registerCommand(object);
        }
    }

    public void registerCommand(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                SimpleCommand simpleCommand = new SimpleCommand(command.name(), command.description(), command.help(), command.example(), object, method, command.power(), command.guildOnly());
                discordCommands.put(command.name(), simpleCommand);
            } else if (method.isAnnotationPresent(ConsoleCommand.class)) {
                ConsoleCommand command = method.getAnnotation(ConsoleCommand.class);
                method.setAccessible(true);
                SimpleConsoleCommand simpleConsoleCommand = new SimpleConsoleCommand(command.name(), command.description(), command.help(), object, method);
                consoleCommands.put(command.name(), simpleConsoleCommand);
            }
        }
    }

    public void registerInteraction(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(InteractionListener.class)) {
                InteractionListener interactionListener = method.getAnnotation(InteractionListener.class);
                method.setAccessible(true);
                SimpleInteraction simpleInteraction = new SimpleInteraction(interactionListener.id(), object, method);
                interactions.put(interactionListener.id(), simpleInteraction);
            }
        }
    }

    private Object[] getConsoleCommand(String command) {
        String[] commandSplit = command.split(" ");
        String[] args = new String[commandSplit.length - 1];
        for (int i = 1; i < commandSplit.length; i++) args[i - 1] = commandSplit[i];
        SimpleConsoleCommand simpleConsoleCommand = consoleCommands.get(commandSplit[0]);
        return new Object[]{simpleConsoleCommand, args};
    }

    public void consoleCommand(String command) {
        SimpleConsoleCommand simpleConsoleCommand = (SimpleConsoleCommand) getConsoleCommand(command)[0];

        try {
            executeConsoleCommand(simpleConsoleCommand, (String[]) getConsoleCommand(command)[1]);
        } catch (Exception e) {
            raymond.getLogger().error("La methode " + simpleConsoleCommand.getMethod().getName() + " n'est pas correctement initialisé. (" + e.getMessage() + ")");
            raymond.getErrorHandler().handleException(e);
        }
    }

    private void executeConsoleCommand(SimpleConsoleCommand simpleConsoleCommand, String[] args) throws Exception {
        Parameter[] parameters = simpleConsoleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == String[].class) objects[i] = args;
            else if (parameters[i].getType() == String.class) objects[i] = simpleConsoleCommand.getName();
            else if (parameters[i].getType() == JDA.class) objects[i] = raymond.getJda();
            else if (parameters[i].getType() == SimpleCommand.class) objects[i] = simpleConsoleCommand;
        }
        simpleConsoleCommand.getMethod().invoke(simpleConsoleCommand.getObject(), objects);
    }

    public void discordCommandUser(String command, SlashCommandInteractionEvent slashCommandEvent) {
        SimpleCommand simpleCommand = (SimpleCommand) getDiscordCommand(command)[0];

        if (simpleCommand.isGuildOnly() && !slashCommandEvent.isFromGuild() || simpleCommand.isGuildOnly() && slashCommandEvent.isFromGuild() && simpleCommand.getPower() > getPowerUser(slashCommandEvent.getGuild(), slashCommandEvent.getUser())) {
            slashCommandEvent.reply("Vous ne pouvez pas utiliser cette commande.").setEphemeral(true).queue();
            return;
        }

        try {
            executeDiscordCommand(simpleCommand, slashCommandEvent.getOptions(), slashCommandEvent);
        } catch (Exception e) {
            raymond.getLogger().error("La methode " + simpleCommand.getMethod().getName() + " n'est pas correctement initialisé. (" + e.getMessage() + ")");
            raymond.getErrorHandler().handleException(e);
        }
    }

    private Object[] getDiscordCommand(String command) {
        String[] commandSplit = command.split(" ");
        String[] args = new String[commandSplit.length - 1];
        for (int i = 1; i < commandSplit.length; i++) args[i - 1] = commandSplit[i];
        SimpleCommand simpleCommand = discordCommands.get(commandSplit[0]);
        return new Object[]{simpleCommand, args};
    }

    public SimpleCommand getDiscordSimpleCommand(String command) {
        return discordCommands.get(command);
    }

    private void executeDiscordCommand(SimpleCommand simpleCommand, List<OptionMapping> args, SlashCommandInteractionEvent slashCommandEvent) throws Exception {
        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == List[].class) objects[i] = args;
            else if (parameters[i].getType() == User.class) objects[i] = slashCommandEvent.getUser();
            else if (parameters[i].getType() == Member.class) objects[i] = slashCommandEvent.getMember();
            else if (parameters[i].getType() == TextChannel.class) objects[i] = slashCommandEvent.getTextChannel();
            else if (parameters[i].getType() == PrivateChannel.class)objects[i] = slashCommandEvent.getPrivateChannel();
            else if (parameters[i].getType() == Guild.class) objects[i] = slashCommandEvent.getGuild();
            else if (parameters[i].getType() == String.class) objects[i] = slashCommandEvent.getName();
            else if (parameters[i].getType() == SlashCommandInteractionEvent.class) objects[i] = slashCommandEvent;
            else if (parameters[i].getType() == JDA.class) objects[i] = raymond.getJda();
            else if (parameters[i].getType() == SimpleCommand.class) objects[i] = simpleCommand;
        }
        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
    }

    public void updateCommands() {
        List<CommandData> commands = new ArrayList<>();
        discordCommands.forEach((s, simpleCommand) -> {
            SlashCommandData commandData = Commands.slash(simpleCommand.getName(), simpleCommand.getDescription());
            if (simpleCommand.getOptionsData().length != 0) commandData = commandData.addOptions(simpleCommand.getOptionsData());
            if (simpleCommand.getSubcommandsData().length != 0)
                commandData = commandData.addSubcommands(simpleCommand.getSubcommandsData());
            if (simpleCommand.getSubcommandsGroups().length != 0)
                commandData = commandData.addSubcommandGroups(simpleCommand.getSubcommandsGroups());

            commands.add(commandData);
        });
        raymond.getJda().getGuilds().forEach(guild -> {
            guild.updateCommands().addCommands(commands).queue();
        });
        raymond.getJda().updateCommands().addCommands(commands).queue();
    }

    public void discordInteraction(String id, MessageContextInteractionEvent messageContextInteractionEvent) {
        try {
            SimpleInteraction simpleInteraction = interactions.get(id);
            Parameter[] parameters = simpleInteraction.getMethod().getParameters();
            Object[] objects = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].getType() == User.class) objects[i] = messageContextInteractionEvent.getUser();
                else if (parameters[i].getType() == TextChannel.class) objects[i] = messageContextInteractionEvent.getTextChannel();
                else if (parameters[i].getType() == PrivateChannel.class)objects[i] = messageContextInteractionEvent.getPrivateChannel();
                else if (parameters[i].getType() == Guild.class) objects[i] = messageContextInteractionEvent.getGuild();
                else if (parameters[i].getType() == MessageContextInteractionEvent.class) objects[i] = messageContextInteractionEvent;
                else if (parameters[i].getType() == JDA.class) objects[i] = raymond.getJda();
                else if (parameters[i].getType() == SimpleInteraction.class) objects[i] = simpleInteraction;
            }
            simpleInteraction.getMethod().invoke(simpleInteraction.getObject(), objects);
        } catch (Exception e) {
            raymond.getErrorHandler().handleException(e);
        }
    }

}