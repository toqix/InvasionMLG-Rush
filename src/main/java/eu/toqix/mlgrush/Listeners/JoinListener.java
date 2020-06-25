package eu.toqix.mlgrush.Listeners;

import eu.toqix.mlgrush.MLGRush;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        Location spawn = MLGRush.spawn;
        spawn.setWorld(event.getPlayer().getWorld());

        spawn.setYaw(180);
        player.teleport(spawn);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();

        player.sendMessage(ChatColor.RED + "Wilkommen zurück " + ChatColor.GOLD + player.getName());
        player.sendTitle(ChatColor.YELLOW +"Willkommen " + ChatColor.RED + player.getName(), ChatColor.GOLD + "auf dem Besten Minequaft Server", 10, 50, 10);
        if(player.getDisplayName().equals("toqix")) {
            event.setJoinMessage("Achtung Achtung der beste Minequafter " + player.getName() + " ist da!!!");
        }else {
            event.setJoinMessage(ChatColor.GRAY + "Der " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY +  " schaut vorbei");
        }
    }
}
