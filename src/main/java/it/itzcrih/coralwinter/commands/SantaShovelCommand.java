package it.itzcrih.coralwinter.commands;

import it.itzcrih.coralwinter.CoralWinter;
import org.bukkit.ChatColor;
import it.itzcrih.coralwinter.utils.SantaShovel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

/**
 * This code is made by
 * @author itzCrih
 */

public class SantaShovelCommand implements CommandExecutor {
    SantaShovel santaShovel = new SantaShovel();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.enable_command")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.command_disabled")));
                return true;
            }
            if (player.hasPermission("coralwinter.command.santashovel")) {
                santaShovel.giveSantaShovel(player);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            }
        } else {
            sender.sendMessage("This command can be executed only as a player");
        }
        return true;
    }
}
