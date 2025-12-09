package it.itzcrih.coralwinter.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://github.com/itzCrih/CoralWinter">CoralWinter</a>
 * This code is made by
 * @author itzCrih
 */

public class CoralWinterTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> list = new ArrayList<>();

        if (!(sender instanceof Player)) return list;
        Player player = (Player) sender;

        if (args.length == 1) {

            if (player.hasPermission("coralwinter.command.reload"))
                list.add("reload");

            if (player.hasPermission("coralwinter.command.santashovel"))
                list.add("santashovel");

            if (player.hasPermission("coralwinter.command.snow"))
                list.add("snow");

            if (player.hasPermission("coralwinter.command.addsnow"))
                list.add("addsnow");

            if (player.hasPermission("coralwinter.command.version"))
                list.add("version");

            return list;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("snow")) {
            if (player.hasPermission("coralwinter.command.snow")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    list.add(p.getName());
                }
            }
        }

        return list;
    }
}
