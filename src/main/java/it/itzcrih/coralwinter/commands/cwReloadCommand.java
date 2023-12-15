package it.itzcrih.coralwinter.commands;

import it.itzcrih.coralwinter.CoralWinter;
import it.itzcrih.coralwinter.config.ConfigLoader;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cwReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (!sender.hasPermission("coralwinter.command.reload")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.reloaded_successfully")));
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);

        ConfigLoader config = CoralWinter.getConfigLoader();
        config.reloadConfig();
        config.reloadMessages();
        return false;
    }
}
