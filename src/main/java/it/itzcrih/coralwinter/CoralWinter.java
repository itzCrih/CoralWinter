package it.itzcrih.coralwinter;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoralWinter extends JavaPlugin {
    private static CoralWinter instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("--------------------------------------");
        getLogger().info("Enabling CoralWinter v" + getDescription().getVersion() + "...");
        getLogger().info("CoralMC plugin replica made by itzCrih");
        getLogger().info("--------------------------------------");

        getLogger().info(ChatColor.GREEN + "CoralWinter has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CoralWinter has been disabled successfully!");
    }

    public static CoralWinter getInstance() {
        return instance;
    }
}
