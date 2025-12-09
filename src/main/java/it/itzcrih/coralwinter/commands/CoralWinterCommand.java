package it.itzcrih.coralwinter.commands;

import it.itzcrih.coralwinter.CoralWinter;
import it.itzcrih.coralwinter.config.ConfigLoader;
import it.itzcrih.coralwinter.utils.ChatUtils;
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
 * <a href="https://github.com/itzCrih/CoralWinter">CoralWinter</a>
 * This code is made by
 * @author itzCrih
 */

public class CoralWinterCommand implements CommandExecutor {

    private final SantaShovel santaShovel = new SantaShovel();
    private final Map<UUID, Long> snowfallCooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtils.colorize("&cThis command can only be executed by players"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (player.hasPermission("coralwinter.command.help")) {
                sendHelpMessages(player);
            } else {
                player.sendMessage(ChatUtils.colorize("&7Running &bCoralWinter &7v"
                        + CoralWinter.getInstance().getDescription().getVersion() + " &7developed by &bitzCrih&7."));
                player.sendMessage(ChatUtils.colorize("&n&b https://github.com/itzCrih/CoralWinter"));
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

            case "version":
                handleVersionCommand(player);
                break;

            default:
                player.sendMessage(ChatUtils.colorize("&cUnknown subcommand."));
                break;
        }

        return true;
    }

    private void handleVersionCommand(Player player) {
        if (!player.hasPermission("coralwinter.command.version")) {
            player.sendMessage(ChatUtils.colorize("&cYou have no permission to view sensitive info."));
            return;
        }

        player.sendMessage(ChatUtils.colorize(
                "&b&l=== CoralWinter Diagnostics ===\n" +
                        "&7Plugin version: &f" + CoralWinter.getInstance().getDescription().getVersion() + "\n" +
                        "&7Server version: &f" + Bukkit.getVersion() + "\n" +
                        "&7Online players: &f" + Bukkit.getOnlinePlayers().size() + "\n" +
                        "&7Config status: &f" + (CoralWinter.getConfigLoader() != null ? "Loaded" : "ERROR") + "\n" +
                        "&7Snowfall cooldown tracked: &f" + snowfallCooldowns.size()
        ));
    }

    private void handleReloadCommand(Player player) {
        if (!player.hasPermission("coralwinter.command.reload")) {
            player.sendMessage(ChatUtils.colorize(
                    CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            return;
        }

        ConfigLoader config = CoralWinter.getConfigLoader();
        config.reloadConfig();
        config.reloadMessages();

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        player.sendMessage(ChatUtils.colorize(
                CoralWinter.getConfigLoader().getMessages().getString("commands.reloaded_successfully")));
    }

    private void handleSantaShovelCommand(Player player) {
        if (!CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.enable_command")) {
            player.sendMessage(ChatUtils.colorize(
                    CoralWinter.getConfigLoader().getMessages().getString("errors.command_disabled")));
            return;
        }

        if (!player.hasPermission("coralwinter.command.santashovel")) {
            player.sendMessage(ChatUtils.colorize(
                    CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            return;
        }

        santaShovel.giveSantaShovel(player);
        player.sendMessage(ChatUtils.colorize(
                CoralWinter.getConfigLoader().getMessages().getString("commands.shovel_received")));
    }

    private void handleAddSnowCommand(Player player) {
        if (!player.hasPermission("coralwinter.command.addsnow")) {
            player.sendMessage(ChatUtils.colorize(
                    CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            return;
        }

        World world = player.getWorld();
        player.sendMessage(ChatUtils.colorize(
                CoralWinter.getConfigLoader().getMessages().getString("commands.adding_snow")));

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
                            Bukkit.getScheduler().runTask(CoralWinter.getInstance(),
                                    () -> world.getBlockAt(worldX, worldY, worldZ).setType(Material.SNOW));
                        }
                    }
                }
                Bukkit.getScheduler().runTask(CoralWinter.getInstance(), () ->
                        player.sendMessage(ChatUtils.colorize(
                                CoralWinter.getConfigLoader().getMessages().getString("commands.successfully_added_snow")))
                );
            }
        }.runTaskAsynchronously(CoralWinter.getInstance());
    }

    private void handleSnowfallCommand(Player player, String[] args) {
        Player targetPlayer = player;

        if (args.length > 1) {
            targetPlayer = player.getServer().getPlayer(args[1]);
            if (targetPlayer == null) {
                player.sendMessage(ChatUtils.colorize(
                        CoralWinter.getConfigLoader().getMessages().getString("errors.player_not_found")
                                .replace("%target%", args[1])));
                return;
            }
        }

        if (!player.hasPermission("coralwinter.command.snow")) {
            player.sendMessage(ChatUtils.colorize(
                    CoralWinter.getConfigLoader().getMessages().getString("errors.no_perm")));
            return;
        }

        UUID playerUUID = player.getUniqueId();
        if (snowfallCooldowns.containsKey(playerUUID)) {
            long timeLeft = (snowfallCooldowns.get(playerUUID) - System.currentTimeMillis()) / 1000;
            if (timeLeft > 0) {
                player.sendMessage(ChatUtils.colorize(
                        CoralWinter.getConfigLoader().getMessages().getString("commands.cooldown")
                                .replace("%time_remaining%", String.valueOf(timeLeft))));
                return;
            }
        }

        snowfallCooldowns.put(playerUUID, System.currentTimeMillis()
                + CoralWinter.getConfigLoader().getConfig().getInt("snow_command.cooldown") * 1000L);

        player.sendMessage(ChatUtils.colorize(
                CoralWinter.getConfigLoader().getMessages().getString("commands.snow_start")
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
                    targetPlayer.sendMessage(ChatUtils.colorize(
                            CoralWinter.getConfigLoader().getMessages().getString("commands.snow_end")
                                    .replace("%target%", targetPlayer.getName())));
                    cancel();
                    return;
                }

                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.SNOW_SHOVEL, 0);
                targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.SNOWBALL_BREAK, 0);

                timeLeft -= 10;
            }
        }.runTaskTimer(CoralWinter.getInstance(), 0, 10);
    }

    private void sendHelpMessages(Player player) {
        player.sendMessage(ChatUtils.colorize(
                "&b&l====[ Coral&f&lWinter &7v" + CoralWinter.getInstance().getDescription().getVersion() + " ]====\n" +
                        "&3/coralwinter reload &7- Reload configs\n" +
                        "&3/coralwinter santashovel &7- Get shovel\n" +
                        "&3/coralwinter snow <player> &7- Snow particles\n" +
                        "&3/coralwinter addsnow &7- Add snow in the lobby\n" +
                        "&3/coralwinter version &7- Plugin diagnostics\n" +
                        "&b&l==============================="
        ));
    }
}