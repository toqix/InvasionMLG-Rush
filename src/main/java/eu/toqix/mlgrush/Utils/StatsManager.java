package eu.toqix.mlgrush.Utils;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class StatsManager {
    public HashMap<Player, HashMap<statsType, Integer>> stats = new HashMap<>();

    public void saveStats(Player player, statsType type, int value) {
        if (!stats.containsKey(player)) {
            stats.put(player, new HashMap<>());
        }
        stats.get(player).put(type, value);
    }

    public void addStats(Player player, statsType type, int value) {
        if (!stats.containsKey(player)) {
            stats.put(player, new HashMap<>());
        }
        if (!stats.get(player).containsKey(type)) {
            stats.get(player).put(type, 0);
        }
        stats.get(player).put(type, stats.get(player).get(type) + value);
    }

    public int getStats(Player player, statsType type) {
        if(stats.containsKey(player)) {
            return stats.get(player).getOrDefault(type, 0);
        }else {
            return 0;
        }
    }
}
