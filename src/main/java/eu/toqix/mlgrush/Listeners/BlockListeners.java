package eu.toqix.mlgrush.Listeners;

import eu.toqix.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SandstoneType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListeners implements Listener {


    @EventHandler
    public void onBlockDestroy(BlockBreakEvent event) {

        Boolean inGame = false;
        if(MLGRush.getGameManager().queue.containsKey(event.getPlayer())) {
            if(MLGRush.getGameManager().queue.get(event.getPlayer()) == 1) {
                inGame = true;
            }
        }
        if(!MLGRush.getBuildManager().playerBuilding.containsKey(event.getPlayer()) && !inGame) {
            event.setCancelled(true);
        }

    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Boolean inGame = false;
        if(MLGRush.getGameManager().queue.containsKey(event.getPlayer())) {
            if(MLGRush.getGameManager().queue.get(event.getPlayer()) == 1) {
                inGame = true;
            }
        }

        if(!MLGRush.getBuildManager().playerBuilding.containsKey(event.getPlayer()) && !inGame && !MLGRush.getTrainer().playersTraining.contains(event.getPlayer())) {
                if(!MLGRush.getTrainer().playersInMLG.containsKey(event.getPlayer())) {
                    event.setCancelled(true);
                }else {
                    if (event.getBlock().getType() != Material.COBWEB || event.getBlock().getType() != Material.LADDER) {
                        event.setCancelled(true);
                    }
                }
        }
       /* Block block = event.getBlock();

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        int px = (int) MLGRush.p1.getX();
        int py = (int) MLGRush.p1.getY();
        int pz = (int) MLGRush.p1.getZ();
        int px2 = (int) MLGRush.p2.getX();
        int py2 = (int) MLGRush.p2.getY();
        int pz2 = (int) MLGRush.p2.getZ();

        if(x==13 && y==py && z==pz) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDu darfst hier keinen Block plazieren!"));
            event.setCancelled(true);
        }else if(x==13 && y==py+1 &&z==pz) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDu darfst hier keinen Block plazieren!"));
            event.setCancelled(true);
        }
        if(x==-14 && y==py2 && z==pz2) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDu darfst hier keinen Block plazieren!"));
            event.setCancelled(true);
        }else if(x==-14 && y==py2+1 &&z==pz2) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDu darfst hier keinen Block plazieren!"));
            event.setCancelled(true);
        }
        if(y > 110) {
            event.setCancelled(true);
        }

        //Map größe
        if(y < -(mapy + 100) || y > (mapy + 100)) {
            event.setCancelled(true);
        }else if(x<-mapx || x>mapx) {
            event.setCancelled(true);
        }else if(z<-mapz || z>mapz) {
            event.setCancelled(true);
        }*/

    }
    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.BEACON) {
                event.setCancelled(true);
            }else if(event.getClickedBlock().getType() == Material.RED_BED) {
                event.setCancelled(true);
            }
        }
    }
}



