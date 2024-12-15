package it.itzcrih.coralwinter.commands;

import it.itzcrih.coralwinter.CoralWinter;
import it.itzcrih.coralwinter.config.ConfigLoader;
import it.itzcrih.coralwinter.utils.SantaShovel;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This code is made by
 * @author itzCrih
 */

public class CoralWinterCommand implements CommandExecutor {

    private final SantaShovel santaShovel = new SantaShovel();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (player.hasPermission("coralwinter.command.help")) {
                sendHelpMessages(player);
            } else {
                player.sendMessage(ChatColor.GRAY + "Running " + ChatColor.AQUA + "CoralWinter " + ChatColor.GRAY + "v" + CoralWinter.getInstance().getDescription().getVersion() + " by itzCrih");
                player.sendMessage(ChatColor.AQUA + "" + ChatColor.UNDERLINE + "https://polymart.org/resource/coralwinter.5194 https://github.com/itzCrih/CoralWinter");
            }
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReloadCommand(player);
                break;

            case "santashovel":
                handleSantaShovelCommand(player);
                break;

            default:
                player.sendMessage(ChatColor.RED + "Unknown command. Use /coralwinter for help.");
                break;
        }

        return true;
    }

    private void handleReloadCommand(Player player) {
        if (!player.hasPermission("coralwinter.command.reload")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            return;
        }

        ConfigLoader config = CoralWinter.getConfigLoader();
        config.reloadConfig();
        config.reloadMessages();

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.reloaded_successfully")));
    }

    private void handleSantaShovelCommand(Player player) {
        if (!CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.enable_command")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.command_disabled")));
            return;
        }

        if (!player.hasPermission("coralwinter.command.santashovel")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            return;
        }

        santaShovel.giveSantaShovel(player);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.shovel_received")));
    }

    private void sendHelpMessages(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lCoral&f&lWinter &7v" + CoralWinter.getInstance().getDescription().getVersion()));
        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/coralwinter reload &7- Reload all configurations"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/coralwinter santashovel &7- Get your own shovel"));
        player.sendMessage("");
    }
}
