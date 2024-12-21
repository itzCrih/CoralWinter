package it.itzcrih.coralwinter.commands;

import it.itzcrih.coralwinter.CoralWinter;
import it.itzcrih.coralwinter.config.ConfigLoader;
import it.itzcrih.coralwinter.utils.SantaShovel;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

            case "snow":
                handleSnowfallCommand(player, args);
                break;

            case "addsnow":
                handleAddSnowCommand(player);
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

    private void handleAddSnowCommand(Player player) {
        if (!player.hasPermission("coralwinter.command.addsnow")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            return;
        }

        World world = player.getWorld();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.adding_snow")));

        new BukkitRunnable() {
            @Override
            public void run() {
                int radius = 50;

                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        int worldX = player.getLocation().getBlockX() + x;
                        int worldZ = player.getLocation().getBlockZ() + z;
                        int worldY = world.getHighestBlockYAt(worldX, worldZ);

                        Material blockAbove = world.getBlockAt(worldX, worldY, worldZ).getType();
                        Material blockBelow = world.getBlockAt(worldX, worldY - 1, worldZ).getType();

                        if (blockAbove == Material.AIR && blockBelow.isSolid() && blockBelow != Material.LEAVES) {
                            Bukkit.getScheduler().runTask(CoralWinter.getInstance(), () -> world.getBlockAt(worldX, worldY, worldZ).setType(Material.SNOW));
                        }
                    }
                }
                Bukkit.getScheduler().runTask(CoralWinter.getInstance(), () -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.successfully_added_snow"))));
            }
        }.runTaskAsynchronously(CoralWinter.getInstance());
    }

    private final Map<UUID, Long> snowfallCooldowns = new HashMap<>();

    private void handleSnowfallCommand(Player player, String[] args) {
        Player targetPlayer = player;

        if (args.length > 1) {
            targetPlayer = player.getServer().getPlayer(args[1]);

            if (targetPlayer == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.player_not_found").replace("%target%", args[1])));
                return;
            }
        }

        if (!player.hasPermission("coralwinter.command.snow")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm"))));
            return;
        }

        UUID playerUUID = player.getUniqueId();
        if (snowfallCooldowns.containsKey(playerUUID)) {
            long timeLeft = (snowfallCooldowns.get(playerUUID) - System.currentTimeMillis()) / 1000;
            if (timeLeft > 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.cooldown")
                        .replaceAll("%time_remaining%", String.valueOf(timeLeft))));
                return;
            }
        }

        snowfallCooldowns.put(playerUUID, System.currentTimeMillis() + CoralWinter.getConfigLoader().getConfig().getInt("snow_command.cooldown") * 1000L);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.snow_start")
                .replace("%target%", targetPlayer.getName())));
        startSnowfallEffect(targetPlayer);
    }

    private void startSnowfallEffect(Player targetPlayer) {
        int duration = CoralWinter.getConfigLoader().getConfig().getInt("snow_command.seconds");

        new BukkitRunnable() {
            int timeLeft = duration * 20;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.snow_end")
                            .replace("%target%", targetPlayer.getName())));
                    cancel();
                    return;
                }

                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.SNOW_SHOVEL, 0);
                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.SNOW_SHOVEL, 0);
                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 2, 1), Effect.SNOW_SHOVEL, 0);
                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(1, 2, 0), Effect.SNOW_SHOVEL, 0);
                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.SNOWBALL_BREAK, 0);
                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.SNOWBALL_BREAK, 0);
                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 2, 1), Effect.SNOWBALL_BREAK, 0);
                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(1, 2, 0), Effect.SNOWBALL_BREAK, 0);

                targetPlayer.playSound(targetPlayer.getLocation(), Sound.NOTE_PLING, 1.0f, 1.2f);

                timeLeft -= 10;
            }
        }.runTaskTimer(CoralWinter.getInstance(), 0, 10);
    }

    private void sendHelpMessages(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lCoral&f&lWinter &7v" + CoralWinter.getInstance().getDescription().getVersion()));
        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/coralwinter reload &7- Reload all configurations"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/coralwinter santashovel &7- Get your own shovel"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/coralwinter snow <player> &7- Generate snow particles near a player"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/coralwinter addsnow &7- Add snow in the lobby"));
        player.sendMessage("");
    }
}
