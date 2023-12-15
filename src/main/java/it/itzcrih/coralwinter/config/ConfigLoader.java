package it.itzcrih.coralwinter.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private FileConfiguration messages;

    public ConfigLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        reloadConfig();
        reloadMessages();
    }

    public void reloadConfig() {
        File configFile = new File(plugin.getDataFolder(), "settings.yml");
        if (!configFile.exists()) {
            plugin.saveResource("settings.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        File configFile = new File(plugin.getDataFolder(), "settings.yml");
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadMessages() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void saveMessages() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessages() {
        return messages;
    }
}