package eu.toqix.mlgrush.Utils;

import eu.toqix.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CoinManager {
    public static void giveCoins(Player player, int coins) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ecoadmin give " + player.getName() + " " + coins);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        player.sendMessage(MessageCreator.prefix("Coin-System", "You received &6" + coins + " &7coins!"));
    }

    public static void calculateCoins(Player winner, int wins, Player looser, int time) {
        int winnerCoins = 10*wins;
        int looserCoins = 10;

        if(wins<2) {
            looserCoins += 30;
        }

        if (time > 300) {
            winnerCoins = winnerCoins * 2;
            looserCoins = looserCoins * 2;
        } else if (time > 900) {
            looserCoins = looserCoins * 3;
            winnerCoins = winnerCoins * 4;
        }

        giveCoins(winner, winnerCoins);
        giveCoins(looser, looserCoins);
    }
}
