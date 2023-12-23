package it.itzcrih.coralwinter.commands;

import it.itzcrih.coralwinter.CoralWinter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * This code is made by
 * @author itzCrih
 */

public class SantaShovelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("coralwinter.command.santashovel")) {
                giveSantaShovel(player);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            }
        } else {
            sender.sendMessage("This command can be executed only as a player");
        }
        return true;
    }

    private void giveSantaShovel(Player player) {
        ItemStack santaShovel = createSantaShovel();

        player.getInventory().addItem(santaShovel);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.shovel_received")));
    }

    private ItemStack createSantaShovel() {
        ItemStack santaShovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        ItemMeta meta = santaShovel.getItemMeta();

        String displayName = CoralWinter.getConfigLoader().getConfig().getString("santashovel.display-name");
        List<String> lore = CoralWinter.getConfigLoader().getConfig().getStringList("santashovel.lore");

        meta.setDisplayName(displayName != null ? colorize(displayName) : "Santa's Shovel");
        if (lore != null && !lore.isEmpty()) {
            meta.setLore(colorize(lore));
        }

        santaShovel.setItemMeta(meta);
        return santaShovel;
    }

    private String colorize(String message) {
        return message.replace("&", "§");
    }

    private List<String> colorize(List<String> messages) {
        messages.replaceAll(this::colorize);
        return messages;
    }
}
