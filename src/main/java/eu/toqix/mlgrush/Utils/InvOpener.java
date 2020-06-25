package eu.toqix.mlgrush.Utils;

import eu.toqix.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InvOpener {
    public static void openDelay(Player player, Inventory inv) {
        Bukkit.getServer().getScheduler().runTaskLater(MLGRush.getInstance(), () -> player.openInventory(inv), 2L);
    }
    public static void openDelay(HumanEntity player, Inventory inv) {
        Bukkit.getServer().getScheduler().runTaskLater(MLGRush.getInstance(), () -> player.openInventory(inv), 2L);
    }
    public static void closeDelay(Player player) {
        Bukkit.getServer().getScheduler().runTaskLater(MLGRush.getInstance(), player::closeInventory, 2L);
    }
    public static void closeDelay(HumanEntity player) {
        Bukkit.getServer().getScheduler().runTaskLater(MLGRush.getInstance(), player::closeInventory, 2L);
    }
}