package eu.toqix.mlgrush;

import eu.toqix.mlgrush.Utils.MessageCreator;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class BuildTrainer implements Listener {
    public List<Player> playersInBuild;
    public List<Player> playersInMLG;
    public List<Player> playersInJump;

    private HashMap<Player, Integer> blocksPlaced = new HashMap<>();
    private HashMap<Player, Integer> timeBridged = new HashMap<>();
    private HashMap<Player, Integer> jumpFails = new HashMap<>();
    private HashMap<Player, Integer> timeJumped = new HashMap<>();
    private HashMap<Player, Integer> timeWeb = new HashMap<>();


    private Location start = new Location(Bukkit.getWorld("world"), -15.5, 104, 189.5);
    private Location jumpStart = new Location(Bukkit.getWorld("world"), -4.5, 106, 168.5);

    public void pressure() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            start.setWorld(player.getWorld());
            if (x < -12 && x > -18 && z > 183 && z < 189) {
                if (playersInBuild.contains(player)) {
                    leave(player);
                } else if (!playersInBuild.contains(player)) {
                    join(player);
                }
            } else if (x < -15 && x > -17 && z > 229 && z < 233 && y > 103 && y < 105) {
                win(player);
            } else if (x < -4 && x > -5 && z > 167.5 && z < 169.5 && y > 105 && y < 107) {
                joinJumpAndRun(player);
            } else if (x < -26 && x > -28 && z > 178 && z < 180 && y > 103 && y < 105) {
                runMlg(player, 169, new ItemStack(Material.COBWEB), "Laufen");
            }

        }
    }

    public void joinJumpAndRun(Player player) {
        if (!playersInJump.contains(player)) {
            if (!MLGRush.getBuildManager().playerBuilding.containsKey(player)) {
                if (MLGRush.getGameManager().queue.containsKey(player)) {
                    if (MLGRush.getGameManager().queue.get(player) == 1) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] Sorry you can't join the Jump and Run shortly before game or in game"));
                    } else {
                        if (playersInBuild.contains(player)) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] Sorry you can't join the Jump and Run whilst in the Trainer"));
                        } else {
                            player.setGameMode(GameMode.SURVIVAL);
                            player.teleport(start);
                            if (!playersInJump.contains(player)) {
                                playersInJump.add(player);
                            }
                            player.sendTitle(ChatColor.GOLD + "Jump And Run", "joined", 0, 50, 0);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] You joined the Jump And Run by Dia_block_mcg use &e/leave&7 to leave"));
                            player.getInventory().clear();
                            player.getInventory().setItem(0, StackCreator.createStack(Material.BARRIER, "&fReset", Arrays.asList("&7Teleports you back"), "", false));

                        }
                    }
                } else {
                    if (playersInBuild.contains(player)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] Sorry you can't join the Jump and Run whilst in the Trainer"));
                    } else {
                        if (!playersInJump.contains(player)) {
                            playersInJump.add(player);
                        }
                        player.sendTitle(ChatColor.GOLD + "Jump And Run", "joined", 0, 50, 0);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] You joined the Jump And Run by Dia_block_mcg use &e/leave&7 to leave"));
                        player.getInventory().clear();
                        player.getInventory().setItem(0, StackCreator.createStack(Material.BARRIER, "&4Reset", Arrays.asList("&7Teleports you back"), "", false));
                        player.getInventory().setItem(8, StackCreator.createStack(Material.RED_DYE, "&cLeave", Arrays.asList("&7Leaves the Game"), "", false));
                    }
                }
            }
        }
    }

    public void leaveJumpAndRun(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] You left the Jump And Run"));
        playersInJump.remove(player);
        timeJumped.remove(player);

        player.setFallDistance(0);
        player.teleport(MLGRush.spawn);
        player.getInventory().clear();
    }

    public void join(Player player) {

        if (!MLGRush.getBuildManager().playerBuilding.containsKey(player)) {
            if (MLGRush.getGameManager().queue.containsKey(player)) {
                if (MLGRush.getGameManager().queue.get(player) == 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush&7] Sorry you can't join the trainer shortly before game or in game"));
                } else {
                    if (playersInJump.contains(player)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] Sorry you can't join the Trainer whilst in the Jump And Run"));
                    } else {
                        // MLGRush.resetBlocks(70, 60, 160, 400);
                        player.setGameMode(GameMode.SURVIVAL);
                        player.teleport(start);
                        if (!playersInBuild.contains(player)) {
                            playersInBuild.add(player);
                        }
                        giveItems(player);
                        player.sendTitle(ChatColor.GOLD + "Training", "joined", 0, 50, 0);
                    }
                }
            } else {
                if (playersInJump.contains(player)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] Sorry you can't join the Trainer whilst in the Jump And Run"));
                } else {


                    if (!playersInBuild.contains(player)) {
                        playersInBuild.add(player);
                    }
                    giveItems(player);
                    player.sendTitle(ChatColor.GOLD + "Training", "joined", 0, 50, 0);
                }
            }
        }

    }

    public void win(Player player) {
        if (playersInBuild.contains(player)) {

            Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> player.teleport(start), 35);
            int time = timeBridged.get(player);
            timeBridged.put(player, 0);
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Ziel erreicht"), ChatColor.translateAlternateColorCodes('&', "&7in &l" + time + "&7 sekunden"), 0, 40, 0);
            blocksPlaced.put(player, 0);

        }
    }

    public void leave(Player player) {
        if (playersInBuild.contains(player)) {
            // MLGRush.resetBlocks(70, 60, 160, 400);
            player.sendTitle(ChatColor.GOLD + "Training", "left", 0, 50, 0);
            player.getInventory().clear();


        }
        playersInBuild.remove(player);
        blocksPlaced.remove(player);
    }

    public void jumpDie(Player player) {
        if (jumpFails.containsKey(player)) {
            jumpFails.put(player, jumpFails.get(player) + 1);
        } else {
            jumpFails.put(player, 1);
        }
        player.setFallDistance(0);
        player.teleport(jumpStart);
        timeJumped.put(player, 0);
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 2);
        player.getInventory().setItem(0, StackCreator.createStack(Material.BARRIER, "&4Reset", Arrays.asList("&7Teleports you back"), "", false));
        player.getInventory().setItem(8, StackCreator.createStack(Material.RED_DYE, "&cLeave", Arrays.asList("&7Leaves the Game"), "", false));
    }

    public void runMlg(Player player, int height, ItemStack item, String art) {
        if (!playersInMLG.contains(player)) {
            player.getWorld().getBlockAt(new Location(player.getWorld(), -32, height, 179)).setType(Material.DIAMOND_BLOCK);
            Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> {
                player.teleport(new Location(player.getWorld(), -31.5, height + 1, 179.5));
                player.getInventory().setItem(4, item);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                playersInMLG.add(player);
            }, 20);
            Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> {
                if (playersInMLG.contains(player)) {

                    mlgFailed(player);
                }
            }, 200);
            Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> player.getWorld().getBlockAt(new Location(player.getWorld(), -32, height, 179)).setType(Material.AIR), 100);
            player.sendTitle(MessageCreator.translate("&6Cobweb MLG"), MessageCreator.translate("&7" + art), 0, 50, 0);


        }
    }
    public void startMLG(Player player, String mode) {
        Random random = new Random();
        switch (mode) {
            case "web":
                player.sendMessage(MessageCreator.translate("&7[&bTrainer&7] You are going to perform a &6Cobweb MLG"));
                break;
            case "sweb":
                player.sendMessage(MessageCreator.translate("&7[&bTrainer&7] You are going to perform a &6Cobweb MLG &7 without heights"));
                break;
            case "leiter":
                player.sendMessage(MessageCreator.translate("&7[&bTrainer&7] You are going to perform a &6Leiter MLG"));
                runMlg(player, random.nextInt(20) + 20, new ItemStack(Material.LADDER, 5), "Leiter");
                break;
            case "sleiter":
                player.sendMessage(MessageCreator.translate("&7[&bTrainer&7] &4 Not done Yet"));
        }
    }

    public void mlgFailed(Player player) {
        if (playersInMLG.contains(player)) {
            player.sendTitle(MessageCreator.translate("&cMission Failed"), MessageCreator.translate("&7We'll get it next Time"), 0, 50, 0);
            player.getInventory().clear();
            player.teleport(MLGRush.spawn);
            playersInMLG.remove(player);
        }
    }

    public void runTrainer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (playersInBuild.size() > 0) {
                    for (Player player : playersInBuild) {
                        if (!player.isOnline()) {
                            playersInBuild.remove(player);
                        }
                        if (player.getLocation().getY() < 100) {
                            //MLGRush.resetBlocks(70, 60, 160, 400);
                            player.setFallDistance(0);
                            player.teleport(start);
                            giveItems(player);
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 2);
                            blocksPlaced.put(player, 0);

                        }
                        if (blocksPlaced.containsKey(player)) {
                            int blocks = blocksPlaced.get(player);
                            int time = 0;
                            if (timeBridged.containsKey(player)) {
                                time = timeBridged.get(player) / 2;
                            }

                            TextComponent actionbar = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6Blocks &7bridged: &a&l" + blocks + " &6Time: &7&l" + time));
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
                            if (blocks > 0) {
                                if (timeBridged.containsKey(player)) {
                                    timeBridged.put(player, timeBridged.get(player) + 1);
                                } else {
                                    timeBridged.put(player, 1);
                                }
                            } else {
                                timeBridged.put(player, 0);
                            }
                        } else {
                            TextComponent actionbar = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6Blocks &7bridget: &a&l00 Sekunden"));
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
                        }


                    }
                }
                if (playersInJump.size() > 0) {

                    for (Player player : playersInJump) {
                        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                           // leaveJumpAndRun(player);
                            player.kickPlayer(MessageCreator.kickCreator("&4Nein Du drecks Cheater", "&7Schämst du dich nicht dass du im Jump and Run in den Gamemode gehst?", true));

                        }

                        double py = player.getLocation().getY();
                        if (timeJumped.containsKey(player)) {
                            timeJumped.put(player, timeJumped.get(player) + 1);
                        } else {
                            timeJumped.put(player, 1);
                        }
                        int hours = 0;
                        int mins = 0;
                        int secs = 0;

                        secs = timeJumped.get(player) / 2;
                        mins = secs / 60;
                        hours = mins / 60;
                        secs = secs - (mins * 60) - (hours * 3600);
                        TextComponent actionbar = new TextComponent(ChatColor.GRAY + "Fails: " + 0 + ChatColor.GRAY + "   Time: " + hours + ":" + mins + ":" + secs);
                        if (jumpFails.containsKey(player)) {
                            actionbar = new TextComponent(ChatColor.GRAY + "Fails: " + ChatColor.RED + jumpFails.get(player) + ChatColor.GRAY + "   Time: " + hours + ":" + mins + ":" + secs);
                        }

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
                        if (py < 106) {
                            jumpDie(player);
                        }

                    }
                }
                if (playersInMLG.size() > 0) {
                    for (Player player : playersInMLG) {
                        double py = player.getLocation().getY();

                        if (timeWeb.containsKey(player)) {
                            timeJumped.put(player, timeWeb.get(player) + 1);
                        } else {
                            timeWeb.put(player, 1);
                        }
                        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                            // player.kickPlayer(MessageCreator.kickCreator("&4Nein Du drecks Cheater", "&7Schämst du dich nicht dass du im Jump and Run in den Gamemode gehst?", true));
                        }
                        if (py < 109) {
                            mlgFailed(player);
                        }

                        // player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);


                    }
                }
            }
        }.runTaskTimer(MLGRush.getInstance(), 0, 10);
    }

    public void giveItems(Player player) {
        //create the blocks
        ItemStack blöck = new ItemStack(Material.SANDSTONE, 64);
        ItemMeta blöcke_meta = blöck.getItemMeta();
        blöcke_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5Blöcke"));
        blöcke_meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7Mit diesen Item kannst du dich Bauen")));
        blöck.setItemMeta(blöcke_meta);
        for (int s = 0; s < 9; s++) {
            player.getInventory().setItem(s, blöck);
        }

    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        World world = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            world = player.getWorld();
        }
        if (world == null) {
            world = Bukkit.getWorld("world");
        }
        if (event.getNewCurrent() > event.getOldCurrent()) {
            if (world.getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() == Material.EMERALD_BLOCK) {
                pressure();
            }
        }


    }


    @EventHandler
    void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (playersInBuild.contains(player)) {
            if (event.getBlock().getZ() < 189) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] Please don't bridge out of the Bridge zone!"));
            } else {
                if (blocksPlaced.containsKey(player)) {
                    int blocks = blocksPlaced.get(player);
                    blocksPlaced.put(player, blocks + 1);
                } else {
                    blocksPlaced.put(player, 1);
                }
                Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> {
                    block.setType(Material.RED_SANDSTONE);
                    Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> {
                        block.setType(Material.REDSTONE_BLOCK);
                        Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> block.setType(Material.AIR), 20);
                    }, 20);
                }, 20);
            }
        }
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (playersInJump.contains(player)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ItemStack tool = event.getPlayer().getItemInHand();
                if (tool.hasItemMeta()) {

                    if (ChatColor.stripColor(tool.getItemMeta().getDisplayName()).equals("Reset")) {
                        jumpDie(player);
                    } else if (ChatColor.stripColor(tool.getItemMeta().getDisplayName()).equals("Leave")) {
                        leaveJumpAndRun(player);
                    }
                }
            }
        }
    }
}
