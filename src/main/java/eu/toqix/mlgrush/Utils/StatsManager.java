package eu.toqix.mlgrush.Utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class StatsManager {
    public HashMap<UUID, HashMap<statsType, Integer>> stats = new HashMap<>();

    public void saveStats(Player player, statsType type, int value) {
        UUID uuid = player.getUniqueId();
        if (!stats.containsKey(uuid)) {
            stats.put(uuid, new HashMap<>());
        }
        stats.get(uuid).put(type, value);
    }

    public void addStats(Player player, statsType type, int value) {
        UUID uuid = player.getUniqueId();
        if (!stats.containsKey(uuid)) {
            stats.put(uuid, new HashMap<>());
        }
        if (!stats.get(uuid).containsKey(type)) {
            stats.get(uuid).put(type, 0);
        }
        stats.get(uuid).put(type, stats.get(uuid).get(type) + value);
    }

    public int getStats(Player player, statsType type) {
        UUID uuid = player.getUniqueId();
        if(stats.containsKey(uuid)) {
            return stats.get(uuid).getOrDefault(type, 0);
        }else {
            return 0;
        }
    }
}
