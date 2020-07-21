package eu.toqix.mlgrush.Utils;

import eu.toqix.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoinManager {
    public static void giveCoins(Player player, int coins) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ecoadmin give " + player.getName() + " " + coins );
        player.sendMessage(MessageCreator.prefix("Coin-System", "You received &6" + coins + " &7coins!"));
    }
}
