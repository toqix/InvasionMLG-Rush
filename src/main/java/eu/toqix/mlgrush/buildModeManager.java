package eu.toqix.mlgrush;

import eu.toqix.mlgrush.Utils.InvOpener;
import eu.toqix.mlgrush.Utils.Inventories;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class buildModeManager implements Listener {
    public HashMap<Player, Integer> playerBuilding = new HashMap<>();
    public int map;
    private int height = 10;
    private int rounds = 10;
    private int zcord;

    public void handler() {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : playerBuilding.keySet()) {
                    if (!player.isOnline()) {
                        playerBuilding.remove(player);
                        quitBuildMode(player, false, false);
                    }
                    if(playerBuilding.get(player) > 1) {
                        TextComponent actionbar1 = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7Use &c/leave&7"));
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar1);
                    }
                }
            }
        }.runTaskTimer(MLGRush.getInstance(), 0, 20);
    }

    public void deleteMap(Boolean total) {
        if(!total) {
            MLGRush.getGameManager().Maps.remove(map);
        }else {
            /*
            MLGRush.getGameManager().Maps.remove(map);
            for (int xa = -30;xa<30;xa++) {
                for (int ya = 80; ya<120; ya++) {
                    for (int za = zcord - 25; za<zcord+25; za++) {

                        Block block = Bukkit.getWorld("world").getBlockAt(xa, ya, za);
                        if(block.getType() != Material.AIR) {
                            Bukkit.getWorld("world").getBlockAt(xa,ya,za).setType(Material.AIR);
                        }

                    }
                }
            }
        */
        }

    }

    public void editMap(int mapnumber, Player player) {
        map = mapnumber;
        //zcord = (int) MLGRush.getGameManager().Maps.get(MLGRush.getGameManager().Maps.keySet().size() -1).get("p1z");


        HashMap map = MLGRush.getGameManager().Maps.get(mapnumber);
        if (map.containsKey("height")) {
            height = (int) map.get("height");
        }
        if (map.containsKey("rounds")) {
            rounds = (int) map.get("rounds");
        }

        player.setGameMode(GameMode.CREATIVE);
        player.setFlying(true);
        player.getInventory().clear();
        playerBuilding.put(player, 1);
        player.getInventory().setItem(0, StackCreator.createStack(Material.BLAZE_ROD, "&6Build Wand", Arrays.asList("&7Your Tool to edit the Map"), "", true));
        int realz = (int) map.get("p1z");
        MLGRush.resetBlocks(32, 20, realz - 26, realz + 26);
        player.teleport(new Location(player.getWorld(), 0, (Integer) map.get("p1y") + 5, (Integer) map.get("p1z") - 0.5));
        player.setFlying(true);
    }



    public void createMap(Player player) {
        playerBuilding.put(player, 1);

        zcord = (int) MLGRush.getGameManager().Maps.get(MLGRush.getGameManager().Maps.size() -1).get("p1z");
        zcord -= 60;
        player.setGameMode(GameMode.CREATIVE);
        player.getWorld().getBlockAt(0, 100, zcord).setType(Material.DIAMOND_BLOCK);
        player.teleport(new Location(player.getWorld(), 0, 110, zcord));
        player.setFlying(true);
        map = MLGRush.getGameManager().Maps.size();
        MLGRush.getGameManager().Maps.put(map, new HashMap());
        MLGRush.getGameManager().Maps.get(map).put("name", "Default Name");
        MLGRush.getGameManager().Maps.get(map).put("verfügbar", false);
        MLGRush.getGameManager().Maps.get(map).put("finished", false);
        MLGRush.getGameManager().Maps.get(map).put("p1z", zcord);
        MLGRush.getGameManager().Maps.get(map).put("p1x", 0);
        MLGRush.getGameManager().Maps.get(map).put("p1y", 110);
        MLGRush.getGameManager().Maps.get(map).put("p2z", zcord);
        MLGRush.getGameManager().Maps.get(map).put("p2x", 0);
        MLGRush.getGameManager().Maps.get(map).put("p2y", 110);
        player.getInventory().setItem(0, StackCreator.createStack(Material.BLAZE_ROD, "&6Build Wand", Arrays.asList("&7Your Tool to edit the Map"), "", true));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] &cBefore finishing or leaving please define the Spawns to prevent bugs"));

    }

    public void helpBuilder(Player builder, Player playerP) {

        MLGRush.getBuildManager().playerBuilding.put(playerP, 5);

        playerP.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You are going to help " + builder.getName()));
        if (playerP != null) {
            playerP.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Entered Build Mode"), "Helping building", 6, 60, 2);
        }

        playerP.teleport(builder.getLocation());
        playerP.setGameMode(GameMode.CREATIVE);
        playerP.setFlying(true);
        TextComponent actionbar1 = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7Use &c/leave&7"));
        playerP.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar1);
        TextComponent actionbar2 = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7Yay &a" + playerP.getName() + "&7 is going to help you"));
        builder.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar2);
    }

    public void editLobby(Player player) {
        InvOpener.closeDelay(player);
        if(player.isOp()) {
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.setFlying(true);
            playerBuilding.put(player, 4);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You are now editing the Lobby please be careful all changes are permanent"));
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Enterd Build Mode"), "Editing the Lobby", 6, 60, 2);
            TextComponent actionbar = new TextComponent(ChatColor.translateAlternateColorCodes('&', "To leave this mode use &c/leave"));
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
        }else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] &cYou have to be OP to perform this action"));
        }
    }

    public void quitBuildMode(Player player, Boolean enableMap, Boolean delete) {
        playerBuilding.remove(player);
        for(Map.Entry<Player, Integer> build : playerBuilding.entrySet()) {
            if(build.getValue() == 5) {
                playerBuilding.remove(build.getKey());
                build.getKey().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You have left Build Mode, you're ready to play now"));
                build.getKey().teleport(MLGRush.spawn);
                build.getKey().setGameMode(GameMode.SURVIVAL);
            }
        }

        player.getInventory().clear();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You have left Build Mode, you're ready to play now"));
        player.teleport(MLGRush.spawn);
        player.setGameMode(GameMode.SURVIVAL);
        if(!delete) {
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Leaving Build Mode"), ChatColor.translateAlternateColorCodes('&', "&7Your Map will be &asaved"), 5, 50, 0);
            MLGRush.getGameManager().Maps.get(map).put("verfügbar", true);
            if (enableMap) {
                MLGRush.getGameManager().Maps.get(map).put("finished", true);
            }
        }
    }

    public void setHeight(int h) {
        height = h;
        MLGRush.getGameManager().Maps.get(map).put("height", height);
    }

    public int getHeight() {
        return height;
    }

    public void setRounds(int r) {
        rounds = r;
        MLGRush.getGameManager().Maps.get(map).put("rounds", rounds);
    }

    public int getRounds() {
        return rounds;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack tool = event.getPlayer().getItemInHand();
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (playerBuilding.containsKey(player)) {
            if (tool.hasItemMeta()) {
                if (tool.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&6Build Wand"))) {
                    event.setCancelled(true);
                } else if (tool.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cTeam Red"))) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You set the Spawn of Team Red to: " + block.getX() + " " + (block.getY() + 1) + " " + block.getZ()));
                    if (MLGRush.getGameManager().Maps.containsKey(map)) {
                        MLGRush.getGameManager().Maps.get(map).put("p1x", block.getX());
                        MLGRush.getGameManager().Maps.get(map).put("p1y", block.getY() + 1);
                        MLGRush.getGameManager().Maps.get(map).put("p1z", block.getZ());
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] &4An error occured when saving the spawn"));
                    }
                } else if (tool.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&9Team Blue"))) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You set the Spawn of Team Blue to: " + block.getX() + " " + (block.getY() + 1) + " " + block.getZ()));
                    if (MLGRush.getGameManager().Maps.containsKey(map)) {
                        MLGRush.getGameManager().Maps.get(map).put("p2x", block.getX());
                        MLGRush.getGameManager().Maps.get(map).put("p2y", block.getY() + 1);
                        MLGRush.getGameManager().Maps.get(map).put("p2z", block.getZ());
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] &4An error occured when saving the spawn"));
                    }
                }

            }
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (playerBuilding.containsKey(event.getPlayer())) {
            if (playerBuilding.get(event.getPlayer()) == 1) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    ItemStack tool = event.getPlayer().getItemInHand();
                    if (tool.hasItemMeta()) {
                        if (tool.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&6Build Wand"))) {
                            InvOpener.openDelay(event.getPlayer(), Inventories.buildModeWand());
                        }
                    }
                }
            }
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.BEACON) {
                event.setCancelled(true);
            }
        }
    }
}
