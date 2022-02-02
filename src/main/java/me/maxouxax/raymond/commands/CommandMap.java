package me.maxouxax.raymond.commands;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.commands.register.discord.CommandDefault;
import me.maxouxax.raymond.commands.register.discord.CommandEmbed;
import me.maxouxax.raymond.commands.register.discord.CommandVersion;
import me.maxouxax.raymond.commands.register.discord.HelpCommand;
import me.maxouxax.raymond.database.DatabaseManager;
import me.maxouxax.raymond.utils.EmbedCrafter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class CommandMap {

    private final Raymond raymond;

    private final Map<String, Integer> powers = new HashMap<>();

    private final Map<String, SimpleCommand> discordCommands = new HashMap<>();
    private final String discordTag = ".";

    public CommandMap() {
        this.raymond = Raymond.getInstance();
        registerCommands(new CommandDefault(this),
                new CommandEmbed(this),
                new CommandVersion(this),
                new HelpCommand(this)
                );
        loadPower();
    }

    private void loadPower()
    {
        try {
            Connection connection = DatabaseManager.getDatabaseAccess().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");

            final ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String id = resultSet.getString("id");
                int power = resultSet.getInt("power");
                powers.put(id, power);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void savePower(String id, int power) throws SQLException {
        Connection connection = DatabaseManager.getDatabaseAccess().getConnection();
        if(power > 0) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET power = ?, updated_at = ? WHERE id = ?");
            preparedStatement.setInt(1, power);
            preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
            preparedStatement.setString(3, id);

            final int updateCount = preparedStatement.executeUpdate();

            if (updateCount < 1) {
                PreparedStatement insertPreparedStatement = connection.prepareStatement("INSERT INTO users (id, power, updated_at) VALUES (?, ?, ?)");
                insertPreparedStatement.setString(1, id);
                insertPreparedStatement.setInt(2, power);
                insertPreparedStatement.setDate(3, new Date(System.currentTimeMillis()));
                insertPreparedStatement.execute();
            }
        }else{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            preparedStatement.setString(1, id);

            preparedStatement.execute();
        }
        connection.close();
    }

    public MessageEmbed getHelpEmbed(String commandName) {
        try {
            SimpleCommand command = discordCommands.get(commandName);
            EmbedCrafter embedCrafter = new EmbedCrafter()
                .setTitle("Aide » "+discordTag + commandName)
                .setDescription(command.getDescription())
                .addField(new MessageEmbed.Field("Utilisation:", discordTag+command.getHelp(), true))
                .addField(new MessageEmbed.Field("Exemple:", discordTag+command.getExample(), true));
            return embedCrafter.build();
        }catch (Exception e){
            raymond.getErrorHandler().handleException(e);
        }
        return new EmbedBuilder().build();
    }

    public void setUserPower(User user, int power)
    {
        if(power == 0){
            powers.remove(user.getId());
        }else{
            powers.put(user.getId(), power);
        }
        try {
            savePower(user.getId(), power);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public int getPowerUser(Guild guild, User user)
    {
        if(guild.getMember(user).hasPermission(Permission.ADMINISTRATOR)) return 10000;
        return powers.getOrDefault(user.getIdLong(), 0);
    }

    public String getDiscordTag() {
        return discordTag;
    }

    public Collection<SimpleCommand> getDiscordCommands(){
        return discordCommands.values();
    }

    public void registerCommands(Object...objects){
        for(Object object : objects){
            registerCommand(object);
        }
    }

    public void registerCommand(Object object){
        for(Method method : object.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(Command.class)){
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                SimpleCommand simpleCommand = new SimpleCommand(command.name(), command.description(), command.help(), command.example(), command.type(), object, method, command.power());
                discordCommands.put(command.name(), simpleCommand);
            }
        }
    }

    public void discordCommandConsole(String command){
        Object[] object = getDiscordCommand(command);
        if(object[0] == null || ((SimpleCommand)object[0]).getExecutorType() == Command.ExecutorType.USER){
            raymond.getLogger().warn("Commande inconnue.");
            return;
        }
        try{
            executeDiscordCommand(((SimpleCommand)object[0]), command, (String[])object[1], null);
        }catch(Exception e){
            raymond.getLogger().error("La commande "+command+" ne s'est pas exécutée à cause d'un problème sur la méthode "+((SimpleCommand)object[0]).getMethod().getName()+" qui ne s'est pas correctement exécutée.");
            raymond.getErrorHandler().handleException(e);
        }
    }

    public boolean discordCommandUser(User user, String command, Message message){
        Object[] object = getDiscordCommand(command);
        if(object[0] == null || ((SimpleCommand)object[0]).getExecutorType() == Command.ExecutorType.CONSOLE) return false;

        if(((SimpleCommand) object[0]).getPower() > getPowerUser(message.getGuild(), message.getAuthor())) return false;

        try{
            executeDiscordCommand(((SimpleCommand)object[0]), command,(String[])object[1], message);
        }catch(Exception e){
            raymond.getLogger().error("La methode "+((SimpleCommand)object[0]).getMethod().getName()+" n'est pas correctement initialisé. ("+e.getMessage()+")");
            raymond.getErrorHandler().handleException(e);
        }
        return true;
    }

    private Object[] getDiscordCommand(String command){
        String[] commandSplit = command.split(" ");
        String[] args = new String[commandSplit.length-1];
        System.arraycopy(commandSplit, 1, args, 0, commandSplit.length - 1);
        SimpleCommand simpleCommand = discordCommands.get(commandSplit[0]);
        return new Object[]{simpleCommand, args};
    }

    public SimpleCommand getDiscordSimpleCommand(String command){
        return discordCommands.get(command);
    }

    private void executeDiscordCommand(SimpleCommand simpleCommand, String command, String[] args, Message message) throws Exception{
        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for(int i = 0; i < parameters.length; i++){
            if(parameters[i].getType() == String[].class) objects[i] = args;
            else if(parameters[i].getType() == User.class) objects[i] = message == null ? null : message.getAuthor();
            else if(parameters[i].getType() == TextChannel.class) objects[i] = message == null ? null : message.getTextChannel();
            else if(parameters[i].getType() == PrivateChannel.class) objects[i] = message == null ? null : message.getPrivateChannel();
            else if(parameters[i].getType() == Guild.class) objects[i] = message == null ? null : message.getGuild();
            else if(parameters[i].getType() == String.class) objects[i] = command;
            else if(parameters[i].getType() == Message.class) objects[i] = message;
            else if(parameters[i].getType() == JDA.class) objects[i] = raymond.getJda();
            else if(parameters[i].getType() == MessageChannel.class) objects[i] = message == null ? null : message.getChannel();
            else if(parameters[i].getType() == SimpleCommand.class) objects[i] = simpleCommand;
        }
        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
    }

}