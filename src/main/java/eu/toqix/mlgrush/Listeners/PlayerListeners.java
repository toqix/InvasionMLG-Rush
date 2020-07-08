package eu.toqix.mlgrush.Listeners;

import eu.toqix.mlgrush.MLGRush;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerListeners implements Listener {
    @EventHandler
    public void onHungerLevelDrop(FoodLevelChangeEvent event) {
        if (event.getFoodLevel() < 20) {
            event.setFoodLevel(20);
        }

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.MAGIC) {
            event.setDamage(0);
        }
        if(event.getEntity()instanceof Player) {
            Player player = (Player) event.getEntity();

            if (MLGRush.getGameManager().queue.containsKey(player)) {
                if (MLGRush.getGameManager().queue.get(player) == 0) {
                    event.setCancelled(true);
                }
            }
            if (MLGRush.getBuildManager().playerBuilding.containsKey(player) || MLGRush.getTrainer().playersTraining.contains(player)) {
                event.setCancelled(true);
            }
        }
    }


}