package it.itzcrih.coralwinter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import it.itzcrih.coralwinter.commands.CoralWinterCommand;
import it.itzcrih.coralwinter.config.ConfigLoader;
import it.itzcrih.coralwinter.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 * CoralWinter
 * <p>
 * A CoralMC Snowball fight plugin replica
 * made with love by
 *
 * @author itzCrih
 */

public final class CoralWinter extends JavaPlugin {
    private static CoralWinter instance;
    private static ConfigLoader config;
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        getLogger().info("Starting CoralWinter v" + getDescription().getVersion() + "...");

        initConfig();
        checkPluginYML();

        if (config.getConfig().getBoolean("snow_particles.enabled")) {
            getLogger().info("Snow particles are enabled in the configuration, searching ProtocolLib...");
            if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
                protocolManager = ProtocolLibrary.getProtocolManager();

                getLogger().info(ChatColor.GREEN + "[OK] ProtocolLib detected!");
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Hooked into ProtocolLib and running successfully!");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        makeItSnow();
                    }
                }.runTaskTimer(this, 0, 20);
            } else {
                getLogger().warning("[!] ProtocolLib not found!");
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "If you want snow particles to work you need to install it.");
            }
        }

        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "_________                     .__  __      __.__        __                ");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "\\_   ___ \\  ________________  |  |/  \\    /  \\__| _____/  |_  ___________ ");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "/    \\  \\/ /  _ \\_  __ \\__  \\ |  |\\   \\/\\/   /  |/    \\   __\\/ __ \\_  __ \\");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "\\     \\___(  <_> )  | \\// __ \\|  |_\\        /|  |   |  \\  | \\  ___/|  | \\/");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + " \\______  /\\____/|__|  (____  /____/\\__/\\  / |__|___|  /__|  \\___  >__|   ");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "        \\/                  \\/           \\/          \\/          \\/       ");

        getCommand("coralwinter").setExecutor(new CoralWinterCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        getLogger().info(ChatColor.GREEN + "[OK] CoralWinter has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling CoralWinter v" + getDescription().getVersion() + "...");
        instance = null;

        getLogger().info(ChatColor.GREEN + "[OK] CoralWinter has been disabled successfully!");
    }

    public static CoralWinter getInstance() {
        return instance;
    }

    public void initConfig() throws RuntimeException {
        instance = this;
        try {
            config = new ConfigLoader(this);
        } catch (Exception e) {
            throw new RuntimeException(config.getMessages().getString("error.config_loading"));
        }
    }

    public static ConfigLoader getConfigLoader() {
        return config;
    }

    private void checkPluginYML() {
        if (!this.getDescription().getAuthors().contains("itzCrih") || !this.getDescription().getName().contains("CoralWinter")) {
            for (int i = 0; i < 25; i++) {
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Why are you changing the");
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "plugin.yml? ( ͡° ͜ʖ ͡°)╭∩╮");
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "weirdo");
            }
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void makeItSnow() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLocation = player.getLocation();
            Random random = new Random();

            for (int i = 0; i < 50; i++) {
                double radius = 10;
                double angle = random.nextDouble() * Math.PI * 2;
                double offsetX = Math.cos(angle) * radius * random.nextDouble();
                double offsetZ = Math.sin(angle) * radius * random.nextDouble();

                PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                packet.getParticles().write(0, EnumWrappers.Particle.SNOW_SHOVEL);
                packet.getFloat().write(0, (float) (playerLocation.getX() + offsetX));
                packet.getFloat().write(1, (float) (playerLocation.getY() + 2 + random.nextDouble() * 2));
                packet.getFloat().write(2, (float) (playerLocation.getZ() + offsetZ));
                packet.getFloat().write(3, (float) 0);
                packet.getFloat().write(4, -0.05f);
                packet.getFloat().write(5, (float) 0);
                packet.getFloat().write(6, 0.1f);
                packet.getIntegers().write(0, 1);

                protocolManager.sendServerPacket(player, packet);
            }
        }
    }
}
