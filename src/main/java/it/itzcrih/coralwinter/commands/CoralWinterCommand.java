package it.itzcrih.coralwinter.commands;

import it.itzcrih.coralwinter.CoralWinter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This code is made by
 * @author itzCrih
 */

public class CoralWinterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("coralwinter.command.help")) {
                sendHelpMessages(player);
            } else {
                player.sendMessage(ChatColor.GRAY + "Running " + ChatColor.AQUA + "CoralWinter " + ChatColor.GRAY + "v" + CoralWinter.getInstance().getDescription().getVersion() + " by itzCrih");
                player.sendMessage(ChatColor.AQUA + "" + ChatColor.UNDERLINE + "https://polymart.org/resource/coralwinter.5194");
            }
        } else {
            sender.sendMessage("This command can be executed only as a player");
        }
        return true;
    }

    private void sendHelpMessages(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lCoral&f&lWinter &7v" + CoralWinter.getInstance().getDescription().getVersion()));
        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/cwreload &7- Reload all configurations"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/santashovel &7- Get your own shovel"));
        player.sendMessage("");
    }
}
