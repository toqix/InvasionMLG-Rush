package eu.toqix.mlgrush;

import eu.toqix.mlgrush.Utils.CoinManager;
import eu.toqix.mlgrush.Utils.StatsManager;
import eu.toqix.mlgrush.Utils.statsType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.BatchUpdateException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class gameRunner implements Listener {

    private MLGRush main = MLGRush.getInstance();

    private int mapx = 30;
    private int mapy = 10;
    private int realz = 0;
    private int mapz = 25;

    private int buffer = 5;
    gameRunner instance;

    private int player1score = 0;
    private int player2score = 0;

    private String pp1 = null;
    private String pp2 = null;
    private int p1Yaw = 0;
    private int p2Yaw = 0;

    public Map<String, Integer> playerBlocks = new HashMap<String, Integer>();
    public Map<String, Integer> playerPicke = new HashMap<String, Integer>();
    public Map<String, Integer> playerStick = new HashMap<String, Integer>();

    private Location p1p;
    private Location p2p;


    private int gameTime = 0;
    private Integer mapint;
    private double height = 10;
    private boolean gameHasStarted = false;
    private StatsManager statsManager = MLGRush.getStatsManager();
    private boolean playersOnMap = false;

    private boolean gamHasEnd = false;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;

    public void saveInstance(gameRunner ins) {
        instance = ins;
    }

    public void runGame(Player player1, Player player2, Location p1, Location p2, String name, String author, double games, Integer map, double height1) {

        height = height1;

        new BukkitRunnable() {

            @Override
            public void run() {

                if (!gamHasEnd && MLGRush.getGameManager().queue.containsKey(player1) && MLGRush.getGameManager().queue.containsKey(player2)) {
                    //Timer how long the Game is running
                    if (gameHasStarted) {
                        gameTime++;
                        seconds = gameTime;
                        minutes = seconds / 60;
                        hours = minutes / 60;
                        seconds = seconds - (minutes * 60) - (hours * 3600);
                        minutes = minutes - (hours * 60);

                    } else {
                        gameTime = 0;
                    }

                    //Search for the Players and set them

                    if (!player1.isOnline()) {
                        gamHasEnd = true;
                    } else if (!player2.isOnline()) {
                        gamHasEnd = true;
                    }


                    if (!gameHasStarted) {
                        mapint = map;
                        p1p = p1;
                        p2p = p2;
                        p1p.setZ(p1p.getZ() + 0.5);
                        p1p.setX(p1p.getX() + 0.5);
                        p2p.setZ(p2p.getZ() + 0.5);
                        p2p.setX(p2p.getX() + 0.5);
                        if (p1p.getX() > 0) {
                            p1Yaw = 90;
                        } else {
                            p1Yaw = 270;
                        }
                        if (p2p.getX() < 0) {
                            p2Yaw = 270;
                        } else {
                            p2Yaw = 90;
                        }
                        p1p.setYaw(p1Yaw);
                        p2p.setYaw(p2Yaw);
                        realz = (int) p1.getZ();
                        p1.setWorld(player1.getWorld());
                        p2.setWorld(player2.getWorld());
                        pp1 = player1.getName();
                        pp2 = player2.getName();
                        mapx = (int) Math.abs(p1.getX()) + 25;

                        gameHasStarted = true;
                    }


                    if (gameTime < 5) {

                        TextComponent timer = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7[&aSpiel startet in: " + (5 - gameTime) + "&7]"));

                        player1.spigot().sendMessage(ChatMessageType.ACTION_BAR, timer);
                        player2.spigot().sendMessage(ChatMessageType.ACTION_BAR, timer);
                        player1.setLevel(5 - gameTime);
                        player2.setLevel(5 - gameTime);
                    }
                    if (gameTime >= 2 && gameTime < 5) {
                        player1.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6" + pp1 + "&7 vs &6" + pp2), name + " by " + author, 0, 30, 0);
                        player2.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6" + pp1 + "&7 vs &6" + pp2), name + " by " + author, 0, 30, 0);
                        player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
                        player2.playSound(player2.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
                    }

                    //Teleport the Players to the Map after 5 seconds
                    if (!playersOnMap && gameTime > 5) {

                        //clear the map
                        MLGRush.resetBlocks(mapx + 1, mapy + 1, realz - mapz - 1, realz + mapz + 1);
                        player1.setDisplayName(ChatColor.RED + pp1 + ChatColor.RESET);
                        player2.setDisplayName(ChatColor.BLUE + pp2 + ChatColor.RESET);
                        player1.setPlayerListName(ChatColor.RED + pp1);
                        player2.setPlayerListName(ChatColor.BLUE + pp2);
                        player1.teleport(p1);
                        player2.teleport(p2);
                        player1.setLevel(0);
                        player2.setLevel(0);
                        giveItems(player1);
                        giveItems(player2);
                        player1.setGameMode(GameMode.SURVIVAL);
                        player2.setGameMode(GameMode.SURVIVAL);
                        player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2);
                        player2.playSound(player2.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2);
                        playersOnMap = true;
                    }


                    if (playersOnMap) {
                        String pl1color;
                        String pl2color;
                        if (player1score > player2score) {
                            pl1color = "&l&2";
                            pl2color = "&l&4";
                        } else if (player2score > player1score) {
                            pl1color = "&l&4";
                            pl2color = "&l&2";
                        } else {
                            pl1color = "&l&7";
                            pl2color = "&l&7";
                        }
                        TextComponent stats = new TextComponent(ChatColor.translateAlternateColorCodes('&', pl1color + player1score + "&7 -- &c" + pp1 + "&7  --" + hours + ":" + minutes + ":" + seconds + "--  &1" + pp2 + "&7 -- " + pl2color + player2score));
                        player1.spigot().sendMessage(ChatMessageType.ACTION_BAR, stats);
                        player2.spigot().sendMessage(ChatMessageType.ACTION_BAR, stats);
                    }
                    if (player1score >= games) {
                        player1.sendTitle(ChatColor.GOLD + pp1 + ChatColor.GRAY + " hat Gewonnen", "Invasion Devs, @toqix", 0, 60, 0);
                        player2.sendTitle(ChatColor.GOLD + pp1 + ChatColor.GRAY + " hat Gewonnen", "Invasion Devs, @toqix", 0, 60, 0);
                        CoinManager.calculateCoins(player1, player1score-player2score, player2, gameTime);
                        statsManager.addStats(player1, statsType.WINS, 1);
                        endGame(player1, player2);
                    } else if (player2score >= games) {
                        player1.sendTitle(ChatColor.GOLD + pp2 + ChatColor.GRAY + " hat Gewonnen", "Invasion Devs, @toqix", 0, 60, 0);
                        player2.sendTitle(ChatColor.GOLD + pp2 + ChatColor.GRAY + " hat Gewonnen", "Invasion Devs, @toqix", 0, 60, 0);
                        CoinManager.calculateCoins(player2, player2score-player1score, player1, gameTime);
                        statsManager.addStats(player2, statsType.WINS, 1);
                        endGame(player2, player1);
                    }
                } else {

                    player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush§7] §cthe Game has end!"));
                    player2.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush§7] §cthe Game has end!"));
                    MLGRush.getGameManager().Maps.get(map).put("verfügbar", true);
                    if (!gamHasEnd) {
                        endGame(player1, player2);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(main, 0, 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                //let the Players respawn after falling down
                die(p1, player1, p1Yaw);
                die(p2, player2, p2Yaw);
                if (gamHasEnd) {
                    cancel();
                }


            }
        }.runTaskTimer(main, 0, 5);
    }


    private void die(Location spawn, Player player, int rotation) {
        Location pos = player.getLocation();

        if (pos.getY() < 95) {
            respawn(spawn, player, rotation);
        }
    }

    private void respawn(Location spawn, Player player, int rotation) {
        giveItems(player);
        spawn.setWorld(player.getWorld());
        spawn.setYaw(rotation);
        player.setFallDistance(0);
        player.teleport(spawn);
        statsManager.addStats(player, statsType.DEATHS, 1);
    }

    private void giveItems(Player player) {

        player.getInventory().clear();
        String name = player.getUniqueId().toString();
        //create the blocks
        ItemStack blöck = new ItemStack(Material.SANDSTONE, 64);
        ItemMeta blöcke_meta = blöck.getItemMeta();
        blöcke_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5Blöcke"));
        blöcke_meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&7Mit diesen Item kannst du dich Bauen")));
        blöck.setItemMeta(blöcke_meta);
        //create the knock back stick
        ItemStack stic = new ItemStack(Material.STICK);
        ItemMeta stick_meta = stic.getItemMeta();

        assert stick_meta != null;
        stick_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4KnockBack-Stick"));
        stick_meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7Knockback 1"), ChatColor.translateAlternateColorCodes('&', "&7Dieser Stock lässt dich den Gegner weit weck schlagen")));
        stick_meta.getItemFlags().add(ItemFlag.HIDE_ENCHANTS);
        stick_meta.getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);
        stic.setItemMeta(stick_meta);
        stic.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        //create the Pickaxt!
        ItemStack pick = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta picke_meta = pick.getItemMeta();
        assert picke_meta != null;
        picke_meta.getItemFlags().add(ItemFlag.HIDE_ENCHANTS);
        picke_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4Picke"));
        picke_meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7Effizienz 3"), ChatColor.translateAlternateColorCodes('&', "&7Mit diesem Werkzeug kannst du Blöcke zerstören")));

        pick.setItemMeta(picke_meta);
        pick.addEnchantment(Enchantment.DIG_SPEED, 2);
        pick.addEnchantment(Enchantment.DURABILITY, 3);

        int stick = 0;
        int picke = 1;
        int blocks = 2;
        if (main.playerStick.containsKey(name)) {
            stick = main.playerStick.get(name);
        }
        if (main.playerPicke.containsKey(name)) {
            picke = main.playerPicke.get(name);
        }
        if (main.playerBlocks.containsKey(name)) {
            blocks = main.playerBlocks.get(name);
        }


        player.getInventory().setItem(stick, stic);
        player.getInventory().setItem(picke, pick);
        player.getInventory().setItem(blocks, blöck);
    }

    private void endGame(Player player1, Player player2) {
        //not two Players : One player has left
        //if the game is still active teleport the left Players back to the lobby and end the game
        if (gameHasStarted) {

            Location s = MLGRush.spawn;
            s.setYaw(180);
            player1.setGameMode(GameMode.SURVIVAL);
            player1.teleport(s);
            player2.setGameMode(GameMode.SURVIVAL);
            player2.teleport(s);
            player1.getInventory().clear();
            player2.getInventory().clear();
            player1.playSound(player1.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);

            pp1 = null;
            pp2 = null;
            gameHasStarted = false;
            playersOnMap = false;
            player2score = 0;
            player1score = 0;
            gamHasEnd = true;
            HandlerList.unregisterAll(instance);
            MLGRush.getGameManager().leaveQueue(player1);
            MLGRush.getGameManager().leaveQueue(player2);
            statsManager.addStats(player1, statsType.ROUNDS, 1);
            statsManager.addStats(player2, statsType.ROUNDS, 1);
            statsManager.addStats(player1, statsType.PLAYTIME, gameTime);
            statsManager.addStats(player2, statsType.PLAYTIME, gameTime);
        }
    }

    private void bedDestroyed(Block block, Player player) {
        if (block.getType() == Material.RED_BED) {
            int bedx = block.getX();
            if (player.getName().equals(pp1)) {
                if (p1p.getX() > 0 && bedx > 0) {
                    player.sendMessage(ChatColor.RED + "Du kannst dein eigenes Bett nicht abbauen");
                } else if (p1p.getX() < 0 && bedx < 0) {
                    player.sendMessage(ChatColor.RED + "Du kannst dein eigenes Bett nicht abbauen");
                } else {
                    bedRespawn(1);
                }
            } else if (player.getName().equals(pp2)) {
                if (p2p.getX() > 0 && bedx > 0) {
                    player.sendMessage(ChatColor.RED + "Du kannst dein eigenes Bett nicht abbauen");
                } else if (p2p.getX() < 0 && bedx < 0) {
                    player.sendMessage(ChatColor.RED + "Du kannst dein eigenes Bett nicht abbauen");
                } else {
                    bedRespawn(2);

                }
            }
        }
    }

    private void bedRespawn(int winner) {
        Player player1 = Bukkit.getPlayer(pp1);
        Player player2 = Bukkit.getPlayer(pp2);
        respawn(p1p, player1, p1Yaw);
        respawn(p2p, player2, p2Yaw);
        if (winner == 1) {
            player1score++;
            statsManager.addStats(player1, statsType.BEDS, 1);
            player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush§7] §f" + pp1 + "&e hat das Bett von &f" + pp2 + " &eabgebaut"));
            player2.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush§7] §f" + pp1 + "&e hat das Bett von &f" + pp2 + " &eabgebaut"));
        } else {
            player2score++;
            statsManager.addStats(player2, statsType.BEDS, 1);
            player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush§7] §f" + pp2 + "&e hat das Bett von &f" + pp1 + " &eabgebaut"));
            player2.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush§7] §f" + pp2 + "&e hat das Bett von &f" + pp1 + " &eabgebaut"));
        }
        MLGRush.resetBlocks(mapx + 1, (int) (mapy + height + 1), realz - mapz - 1, realz + mapz + 1);
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (gameTime <= 5) {
            event.setCancelled(true);
        }
        Block block = event.getBlock();
        Player player = event.getPlayer();


        if (block.getType() != Material.SANDSTONE) {
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();


            if (x < mapx && x > -mapx) {
                if (y < mapy + 100 && y > -mapy + 100) {
                    if (z < (realz + mapz) && z > (realz - mapz)) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (player.getName().equals(pp1) || player.getName().equals(pp2)) {
            bedDestroyed(block, player);

        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (gameTime <= 5) {
            event.setCancelled(true);
        }
        Block block = event.getBlock();

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        HashMap theMap = MLGRush.getGameManager().Maps.get(mapint);
        double px = (double) theMap.get("p1x");
        double py = (double) theMap.get("p1y");
        double pz = (double) theMap.get("p1z");
        double px2 = (double) theMap.get("p2x");
        double py2 = (double) theMap.get("p2y");
        double pz2 = (double) theMap.get("p2z");

        if (x == px && y == py && z == pz) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDu darfst hier keinen Block plazieren!"));
            event.setCancelled(true);
        } else if (x == px && y == py + 1 && z == pz) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDu darfst hier keinen Block plazieren!"));
            event.setCancelled(true);
        }
        if ((x) == px2 && y == py2 && z == pz2) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDu darfst hier keinen Block plazieren!"));
            event.setCancelled(true);
        } else if ((x) == px2 && y == py2 + 1 && z == pz2) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDu darfst hier keinen Block plazieren!"));
            event.setCancelled(true);
        }

        if (block.getType() != Material.SANDSTONE) {
            int bx = block.getX();
            int by = block.getY();
            int bz = block.getZ();


            if (bx < mapx && x > -mapx) {
                if (by < mapy + 100 && by > -mapy + 100) {
                    if (bz < (realz + mapz) && bz > (realz - mapz)) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        //Map größe
        if (y < -(mapy + 100) || y > (height + 100)) {
            if (y > height + 100 + buffer) {

            } else {
                if (x > mapx + buffer || x < -mapx - buffer) {

                } else {
                    if (z > (realz + mapz) + buffer || z < (realz - mapz) - buffer) {

                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        } else if (x < -mapx || x > mapx) {
            if (y > height + 100 + buffer) {

            } else {
                if (x > mapx + buffer || x < -mapx - buffer) {

                } else {
                    if (z > (realz + mapz) + buffer || z < (realz - mapz) - buffer) {

                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        } else if (z < (realz - mapz) || z > (realz + mapz)) {
            if (y > height + 100 + buffer) {

            } else {
                if (x > mapx + buffer || x < -mapx - buffer) {

                } else {
                    if (z > (realz + mapz) + buffer || z < (realz - mapz) - buffer) {

                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }

    }


}

/*
//Map größe
        if(y < -(mapy + 100) || y > (height + 100)) {
            if(y > height + 100 + buffer) {

            }else {
                event.setCancelled(true);
            }
        }else if(x<-mapx || x>mapx) {
            if(x > mapx + buffer || x < -mapx - buffer ) {

            }else {
                event.setCancelled(true);
            }

        }else if(z< (realz-mapz)  || z> (realz + mapz)) {
            if(z > (realz + mapz) + buffer || z < (realz-mapz) - buffer ) {

            }else {
                event.setCancelled(true);
            }
        }

 */
