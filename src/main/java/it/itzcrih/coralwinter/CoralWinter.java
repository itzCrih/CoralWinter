package it.itzcrih.coralwinter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import it.itzcrih.coralwinter.commands.CoralWinterCommand;
import it.itzcrih.coralwinter.commands.SantaShovelCommand;
import it.itzcrih.coralwinter.commands.cwReloadCommand;
import it.itzcrih.coralwinter.config.ConfigLoader;
import it.itzcrih.coralwinter.listeners.BlockProtectionListener;
import it.itzcrih.coralwinter.listeners.PlayerListener;
import it.itzcrih.coralwinter.listeners.SnowBreakListener;
import it.itzcrih.coralwinter.listeners.SnowballDamageListener;
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
        initConfig();
        checkPluginYML();

        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
            getLogger().info(ChatColor.GREEN + "[!] ProtocolLib detected!");
            getServer().getConsoleSender().sendMessage("Snow particles will work since you installed ProtocolLib on your server!");

            new BukkitRunnable() {
                @Override
                public void run() {
                    makeItSnow();
                }
            }.runTaskTimer(this, 0, 20);
        } else {
            getLogger().warning("[!] ProtocolLib not found!");
            getServer().getConsoleSender().sendMessage("If you want to make snow particles work you need to install it.");
        }

        getLogger().info("------------------------------");
        getLogger().info("CoralWinter v" + getDescription().getVersion());
        getLogger().info("CoralMC plugin replica made by itzCrih");
        getLogger().info("------------------------------");

        getCommand("coralwinter").setExecutor(new CoralWinterCommand());
        getCommand("santashovel").setExecutor(new SantaShovelCommand());
        getCommand("cwreload").setExecutor(new cwReloadCommand());

        Bukkit.getPluginManager().registerEvents(new SnowBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new SnowballDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockProtectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        getLogger().info(ChatColor.GREEN + "CoralWinter has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        instance = null;
        getLogger().info("CoralWinter has been disabled successfully!");
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
