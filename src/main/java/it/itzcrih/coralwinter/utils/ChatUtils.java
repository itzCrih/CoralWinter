package it.itzcrih.coralwinter.utils;

import org.bukkit.ChatColor;

import java.util.List;

/**
 * <a href="https://github.com/itzCrih/CoralWinter">CoralWinter</a>
 * This code is made by
 * @author itzCrih
 */

public class ChatUtils {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorize(List<String> messages) {
        messages.replaceAll(ChatUtils::colorize);
        return messages;
    }
}
