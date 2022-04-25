package me.maxouxax.raymond.commands.register.console;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.RaymondServerConfig;
import me.maxouxax.supervisor.commands.ConsoleCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class CommandConsolePower implements ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        Raymond raymond = Raymond.getInstance();

        if (args.length == 3) {
            Guild guild = raymond.getJda().getGuildById(args[0]);
            if (guild == null) {
                System.out.println("Le serveur n'existe pas");
            } else {
                Member member = guild.getMemberById(args[1]);
                if (member == null) {
                    System.out.println("L'utilisateur n'existe pas ou n'est pas présent sur le serveur");
                } else {
                    int power = Integer.parseInt(args[2]);
                    if (power < 0) {
                        System.out.println("Le power doit être supérieur à 0");
                    } else {
                        RaymondServerConfig serverConfig = raymond.getServerConfigsManager().getServerConfig(guild.getId());
                        serverConfig.setUserPower(member.getUser(), power);
                        System.out.println("Le power de " + member.getUser().getName() + " a bien été défini à " + power);
                    }
                }
            }
        } else {
            System.out.println("Usage: " + help());
        }
    }

    @Override
    public String help() {
        return "power <server id> <user id> <power>";
    }

    @Override
    public String example() {
        return "power 98435712186447 376157426943765 100";
    }

    @Override
    public String name() {
        return "power";
    }

    @Override
    public String description() {
        return "Permet de modifier le power d'un utilisateur sur un serveur";
    }

}
