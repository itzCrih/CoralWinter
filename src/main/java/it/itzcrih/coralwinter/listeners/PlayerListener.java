package it.itzcrih.coralwinter.listeners;

import it.itzcrih.coralwinter.CoralWinter;
import it.itzcrih.coralwinter.utils.SantaShovel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This code is made by
 * @author itzCrih
 */

public class PlayerListener implements Listener {
    SantaShovel santaShovel = new SantaShovel();
    private final boolean giveShovelOnJoin = CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.give_on_join");

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (giveShovelOnJoin) {
            santaShovel.giveSantaShovel(player);
        }

        // Questo codice mi invia un messaggio quando usate il plugin. Mi dispiacerebbe se lo togliessi!
        if (player.getName().equals("itz_Crih") || player.getName().equals("FrChillato")) {
            player.sendMessage((""));
            player.sendMessage(("§7Questo server sta usando §bCoralWinter§7!"));
            player.sendMessage("");
            player.sendMessage("§3" + CoralWinter.getInstance().getDescription().getName() + "§7§o v" + CoralWinter.getInstance().getDescription().getVersion());
            player.sendMessage("§7Autore: §3" + CoralWinter.getInstance().getDescription().getAuthors());
            player.sendMessage((""));
        }
    }
}
