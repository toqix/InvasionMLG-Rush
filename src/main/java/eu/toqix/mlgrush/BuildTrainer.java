package eu.toqix.mlgrush;

import eu.toqix.mlgrush.Utils.InvOpener;
import eu.toqix.mlgrush.Utils.Inventories;
import eu.toqix.mlgrush.Utils.MessageCreator;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class BuildTrainer implements Listener {
    public List<Player> playersInBuild = new ArrayList();
    public HashMap<Player, Boolean> playersInMLG = new HashMap<>();
    private HashMap<Player, String> mlgMode = new HashMap<>();
    private HashMap<Player, Integer> mlgWins = new HashMap<>();
    private HashMap<Player, Integer> mlgFails = new HashMap<>();
    public List<Player> playersInJump = new ArrayList();
    public List<Player> playersTraining = new ArrayList<>();

    private Location mlgSpawn = new Location(Bukkit.getWorld("world"), -24.5, 104, 178.5, 180, 0);
    private Location jumpSpawn = new Location(Bukkit.getWorld("world"), -3.5, 105, 164.5, 270, 0);

    private HashMap<Player, Integer> blocksPlaced = new HashMap<>();
    private HashMap<Player, Integer> timeBridged = new HashMap<>();
    private HashMap<Player, Integer> jumpFails = new HashMap<>();
    private HashMap<Player, Integer> timeJumped = new HashMap<>();
    private HashMap<Player, Integer> timeWeb = new HashMap<>();
    private HashMap<Player, Integer> timeMlg = new HashMap<>();


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
                joinMlg(player);
            }

        }
    }

    public void joinMlg(Player player) {
        if (!playersInMLG.containsKey(player)) {
            if (!MLGRush.getBuildManager().playerBuilding.containsKey(player)) {
                if (MLGRush.getGameManager().queue.containsKey(player)) {
                    if (MLGRush.getGameManager().queue.get(player) == 1) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] Sorry you can't join the Jump and Run shortly before game or in game"));
                    } else {
                        if (playersInBuild.contains(player)) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] Sorry you can't join the MLG-Trainer whilst in another Trainer"));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] You joined the MLG-Trainer by toqix"));
                            InvOpener.openDelay(player, Inventories.mlgTrainer());
                        }
                    }
                } else {
                    if (playersInBuild.contains(player)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] Sorry you can't join the MLG-Trainer whilst in another Trainer"));
                    } else {
                        InvOpener.openDelay(player, Inventories.mlgTrainer());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bTrainer&7] You joined the MLG-Trainer by toqix"));
                    }
                }
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
                            if(!playersTraining.contains(player)) {
                                playersTraining.add(player);
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
                        if(!playersTraining.contains(player)) {
                            playersTraining.add(player);
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
        playersTraining.remove(player);
        timeJumped.remove(player);

        player.setFallDistance(0);
        player.teleport(jumpSpawn);
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
                        if(!playersTraining.contains(player)) {
                            playersTraining.add(player);
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
                    if(!playersTraining.contains(player)) {
                        playersTraining.add(player);
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
        playersTraining.remove(player);
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
        if (playersInMLG.containsKey(player)) {
            player.getWorld().getBlockAt(new Location(player.getWorld(), -93, height, 182)).setType(Material.DIAMOND_BLOCK);

            player.teleport(new Location(player.getWorld(), -92.5, height + 1, 182.5, 90, 90));
            player.getInventory().setItem(4, item);


            Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> player.getWorld().getBlockAt(new Location(player.getWorld(), -93, height, 182)).setType(Material.AIR), 50);
            if (art != "Endless Mode") {
                player.sendTitle(MessageCreator.translate("&6Cobweb MLG"), MessageCreator.translate("&7" + art), 0, 50, 0);
            }
        }
    }

    public void startMLG(Player player, String mode) {
        Random random = new Random();
        switch (mode) {
            case "web":
                player.sendMessage(MessageCreator.translate("&7[&bTrainer&7] You are going to perform a &6Cobweb MLG"));
                mlgMode.put(player, "web");
                playersInMLG.put(player, false);
                if(!playersTraining.contains(player)) {
                    playersTraining.add(player);
                }
                runMlg(player, random.nextInt(45) + 135, new ItemStack(Material.COBWEB, 1), "CobWeb");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                break;
            case "sweb":
                player.sendMessage(MessageCreator.translate("&7[&bTrainer&7] You joined the Endless Cobweb Mode"));
                mlgMode.put(player, "web");
                playersInMLG.put(player, true);
                if(!playersTraining.contains(player)) {
                    playersTraining.add(player);
                }
                runMlg(player, random.nextInt(45) + 135, new ItemStack(Material.COBWEB, 1), "CobWeb");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                break;
            case "leiter":
                player.sendMessage(MessageCreator.translate("&7[&bTrainer&7] You are going to perform a &6Leiter MLG"));
                mlgMode.put(player, "ladder");
                playersInMLG.put(player, false);
                if(!playersTraining.contains(player)) {
                    playersTraining.add(player);
                }
                runMlg(player, random.nextInt(20) + 120, new ItemStack(Material.LADDER, 5), "Leiter");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                break;
            case "sleiter":
                mlgMode.put(player, "ladder");
                playersInMLG.put(player, true);
                if(!playersTraining.contains(player)) {
                    playersTraining.add(player);
                }
                runMlg(player, random.nextInt(20) + 120, new ItemStack(Material.LADDER, 1), "CobWeb");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                player.sendMessage(MessageCreator.translate("&7[&bTrainer&7] You joined the Endless Leiter Mode"));
        }
    }

    public void mlgFailed(Player player) {
        Random random = new Random();
        if (playersInMLG.containsKey(player)) {
            if (!playersInMLG.get(player)) {
                player.sendTitle(MessageCreator.translate("&cMLG Failed"), MessageCreator.translate("&7Du bist ein schlechter Minequafter"), 0, 50, 0);
                player.getInventory().clear();
                player.teleport(mlgSpawn);
                playersInMLG.remove(player);
                playersTraining.remove(player);
            } else {
                player.sendTitle(MessageCreator.translate("&cMLG Failed"), MessageCreator.translate("&7Du bist ein schlechter Minequafter"), 0, 50, 0);
                player.getInventory().clear();
                ItemStack item;
                if (mlgFails.containsKey(player)) {
                    mlgFails.put(player, mlgFails.get(player) + 1);
                } else {
                    mlgFails.put(player, 1);
                }
                if (mlgMode.get(player) == "web") {
                    item = new ItemStack(Material.COBWEB, 1);
                    runMlg(player, random.nextInt(45) + 135, item, "Endless Mode");
                } else if (mlgMode.get(player) == "ladder") {
                    item = new ItemStack(Material.LADDER, 5);
                    runMlg(player, random.nextInt(20) + 120, item, "Endless Mode");
                }

            }
        }
    }

    public void leaveMlg(Player player) {
        player.sendTitle(MessageCreator.translate("&cDu hast Verlassen"), MessageCreator.translate("&7Du bist ein schlechter Minequafter"), 0, 50, 0);
        player.sendMessage(MessageCreator.translate("&7[&bTrainer&7] You left the &6MLG-Trainer"));
        player.getInventory().clear();
        player.teleport(mlgSpawn);
        playersInMLG.remove(player);
        playersTraining.remove(player);
    }

    public void mlgWon(Player player) {
        Random random = new Random();
        if (playersInMLG.containsKey(player)) {
            if (!playersInMLG.get(player)) {
                player.getInventory().clear();
                player.teleport(mlgSpawn);
                playersInMLG.remove(player);
                playersTraining.remove(player);
                player.sendTitle(MessageCreator.translate("&6You Won"), MessageCreator.translate("&7Du bist ein guter Minequafter"), 0, 50, 0);
            } else {
                player.sendTitle(MessageCreator.translate("&6MLG Won"), MessageCreator.translate("&7Du bist ein guter Minequafter"), 0, 50, 0);
                player.getInventory().clear();
                ItemStack item;
                if (mlgWins.containsKey(player)) {
                    mlgWins.put(player, mlgWins.get(player) + 1);
                } else {
                    mlgWins.put(player, 1);
                }

                if (mlgMode.get(player) == "web") {
                    item = new ItemStack(Material.COBWEB, 1);
                    runMlg(player, random.nextInt(45) + 135, item, "Endless Mode");
                } else if (mlgMode.get(player) == "ladder") {
                    item = new ItemStack(Material.LADDER, 5);
                    runMlg(player, random.nextInt(20) + 120, item, "Endless Mode");
                }
            }
        }
    }

    public void runTrainer() {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : playersInBuild) {
                    if (!player.isOnline()) {
                        playersInBuild.remove(player);
                        playersTraining.remove(player);
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

                for (Player player : playersInJump) {
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                        // leaveJumpAndRun(player);
                        player.kickPlayer(MessageCreator.kickCreator("&4Nein Du drecks Cheater", "&7Schämst du dich nicht dass du im Jump and Run in den Gamemode gehst?", true));
                    }
                    if (!player.isOnline()) {
                        playersInJump.remove(player);
                        playersTraining.remove(player);
                    }

                    double py = player.getLocation().getY();
                    if (timeJumped.containsKey(player)) {
                        timeJumped.put(player, timeJumped.get(player) + 1);
                    } else {
                        timeJumped.put(player, 1);
                    }
                    int hours;
                    int mins;
                    int secs;

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

                for (Map.Entry<Player, Boolean> map : playersInMLG.entrySet()) {
                    Player player = map.getKey();
                    if (!player.isOnline()) {
                        playersInMLG.remove(player);
                        playersTraining.remove(player);
                    }

                    double py = player.getLocation().getY();

                    if (timeWeb.containsKey(player)) {
                        timeJumped.put(player, timeWeb.get(player) + 1);
                    } else {
                        timeWeb.put(player, 1);
                    }
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                        // player.kickPlayer(MessageCreator.kickCreator("&4Nein Du drecks Cheater", "&7Schämst du dich nicht dass du im Jump and Run in den Gamemode gehst?", true));
                        player.setGameMode(GameMode.SURVIVAL);
                    }
                    if (py < 120) {
                        timeMlg.put(player, 1);
                    } else {
                        timeMlg.put(player, 0);
                    }
                    if (timeMlg.get(player) == 1 && player.isOnGround()) {
                        Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> mlgWon(player), 0);
                    }
                    if (map.getValue()) {
                        TextComponent actionbar = new TextComponent("");
                        if (mlgFails.containsKey(player) && mlgWins.containsKey(player)) {
                            actionbar = new TextComponent(MessageCreator.translate("&7Fails &c" + mlgFails.get(player) + "  &7Wins: &6" + mlgWins.get(player)));
                        } else if (mlgWins.containsKey(player)) {
                            actionbar = new TextComponent(MessageCreator.translate("&7Fails &c" + 0 + "  &7Wins: &6" + mlgWins.get(player)));
                        } else if (mlgFails.containsKey(player)) {
                            actionbar = new TextComponent(MessageCreator.translate("&7Fails &c" + mlgFails.get(player) + "  &7Wins: &6" + 0));
                        }

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
                    }
                    if (py < 105) {
                        mlgFailed(player);
                    }
                    // player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
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
        } else if (playersInMLG.containsKey(player)) {
            Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> {
                player.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).setType(Material.AIR);
            }, 20);
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
                        player.getInventory().setItem(0, StackCreator.createStack(Material.BARRIER, "&4Reset", Arrays.asList("&7Teleports you back"), "", false));
                    } else if (ChatColor.stripColor(tool.getItemMeta().getDisplayName()).equals("Leave")) {
                        leaveJumpAndRun(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (playersInMLG.containsKey(player)) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    if (player.getLocation().getY() < 120) {
                        mlgFailed(player);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
