package eu.toqix.mlgrush;

import eu.toqix.mlgrush.Utils.InvOpener;
import eu.toqix.mlgrush.Utils.Inventories;
import eu.toqix.mlgrush.Utils.MessageCreator;
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
    public HashMap<Player, buildMode> playerBuilding = new HashMap<>();
    public int map;
    private double height = 10;
    private double rounds = 10;
    private double zcord;

    public void handler() {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : playerBuilding.keySet()) {
                    if (!player.isOnline()) {
                        playerBuilding.remove(player);
                        quitBuildMode(player, false, false);
                    }
                    if (playerBuilding.get(player) != buildMode.MAIN) {
                        TextComponent actionbar1 = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7Use &c/leave&7"));
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar1);
                    }
                }
            }
        }.runTaskTimer(MLGRush.getInstance(), 0, 40);
    }

    public void deleteMap(boolean total) {
        if (!total) {
            //MLGRush.getGameManager().Maps.remove(map);
        } else {
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

    public void editMapInventory(Player player, String args) {
        if (!playerBuilding.containsKey(player)) {
            InvOpener.closeDelay(player);
            int Map = Integer.parseInt(args);
            String author = "unkown";
            if (MLGRush.getGameManager().Maps.get(Map).containsKey("author")) {
                author = (String) MLGRush.getGameManager().Maps.get(Map).get("author");
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You are going to edit " + MLGRush.getGameManager().Maps.get(Integer.parseInt(args)).get("name") + " by " + author));

            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Enterd Build Mode"), "Editing: " + MLGRush.getGameManager().Maps.get(Map).get("name") + "  by " + author, 6, 60, 2);

            if (MLGRush.getGameManager().Maps.get(Map).containsKey("verfügbar")) {
                MLGRush.getGameManager().Maps.get(Map).put("verfügbar", false);
                MLGRush.getGameManager().Maps.get(Map).put("finished", false);
                editMap(Map, player);
            }
        } else {
            player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&cYou can't join the Build Mode twice"));
        }
    }

    public void inventoryHandler(String args, Player player) {
        boolean builderOnline = false;
        Player builder = null;
        for (Map.Entry<Player, buildMode> playerbuildModeEntry : playerBuilding.entrySet()) {
            if (playerbuildModeEntry.getValue() == buildMode.MAIN) {
                builderOnline = true;
                builder = playerbuildModeEntry.getKey();
                break;
            }
        }

        switch (args) {
            case "edit":
                if (!builderOnline) {
                    if (player.isOp()) {
                        InvOpener.openDelay(player, Inventories.editAMap());
                    } else {
                        InvOpener.closeDelay(player);
                        player.sendMessage(MessageCreator.translate("&7[&bMLG-Rush-Build&7] &cSorry you don't have permissions to edit this map if you think this is a mistake please contact us on Discord"));
                    }
                } else {
                    InvOpener.closeDelay(player);
                    player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&cSorry but there is already a builder online, but you can help him"));
                }
                break;
            case "back":
                InvOpener.openDelay(player, Inventories.chooseBuildMode());
                break;
            case "quit":
                quitBuildMode(player, false, false);
                break;
            case "save":
                quitBuildMode(player, true, false);
                break;
            case "create":
                if (!builderOnline) {
                    InvOpener.closeDelay(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You are going to create a new Map"));
                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Enterd Build Mode"), "Creating: A Great New Map", 6, 60, 2);
                    createMap(player);
                } else {
                    InvOpener.closeDelay(player);
                    player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&cSorry but there is already a builder online, but you can help him"));
                }
                break;
            case "red":
                if (builderOnline) {
                    if (player == builder) {
                        InvOpener.closeDelay(player);
                        player.getInventory().setItem(4, StackCreator.createStack(Material.RED_STAINED_GLASS_PANE, "&cTeam Red", Arrays.asList("&7Sets the respawn point", "&7of Team Red"), "", false));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "this shouldn't happen your not the Builder");
                }
                break;
            case "blue":
                if (builderOnline) {
                    if (player == builder) {
                        InvOpener.closeDelay(player);
                        player.getInventory().setItem(4, StackCreator.createStack(Material.BLUE_STAINED_GLASS_PANE, "&9Team Blue", Arrays.asList("&7Sets the respawn point", "&7of Team Blue"), "", false));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "this shouldn't happen your not the Builder");
                }

                break;
            case "help":
                if (builderOnline) {
                    if (builder != null) {
                        helpBuilder(builder, player);
                    } else {
                        player.sendMessage(ChatColor.RED + "Oops Something went wrong hit toqix when this error stays");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Oops Something went wrong hit toqix when this error stays");
                }

                break;
            case "delete":
            /*
            deleteMap(false);
            quitBuildMode(player, false, true);

             */
                break;
            case "deleteall":
            /*
            deleteMap(true);
            quitBuildMode(player, false, true);
             */
                break;
            case "lobby":
                editLobby(player);
                break;
            case "h+":
                setHeight(getHeight() + 1);
                InvOpener.openDelay(player, Inventories.buildModeWand());
                break;
            case "h-":
                if (getHeight() > 0) {
                    setHeight(getHeight() - 1);
                }
                InvOpener.openDelay(player, Inventories.buildModeWand());
                break;
            case "r+":
                setRounds(getRounds() + 1);
                InvOpener.openDelay(player, Inventories.buildModeWand());
                break;
            case "r-":
                if (getRounds() > 2) {
                    setRounds(getRounds() - 1);
                }
                InvOpener.openDelay(player, Inventories.buildModeWand());
                break;
        }
    }

    public void editMap(int mapnumber, Player player) {
        map = mapnumber;
        HashMap map = MLGRush.getGameManager().Maps.get(mapnumber);
        if(map == null || map.get("p1z") == null) {
            player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", MessageCreator.translate("&cCould not join Map there might be a configuration issue")));
            player.sendTitle(MessageCreator.translate("&cFailed"), MessageCreator.translate("&cto create Map"), 0, 60, 0);
        }else {
            if (map.containsKey("height")) {
                height = (double) map.get("height");
            }
            if (map.containsKey("rounds")) {
                rounds = (double) map.get("rounds");
            }
            player.setGameMode(GameMode.CREATIVE);
            player.setFlying(true);
            player.getInventory().clear();
            playerBuilding.put(player, buildMode.MAIN);
            player.getInventory().setItem(0, StackCreator.createStack(Material.BLAZE_ROD, "&6Build Wand", Arrays.asList("&7Your Tool to edit the Map"), "", true));
            double realz = (double) map.get("p1z");
            MLGRush.resetBlocks(32, 20, (int) realz - 26, (int) realz + 26);
            player.teleport(new Location(player.getWorld(), 0, (double) map.get("p1y") + 5, (double) map.get("p1z") - 0.5));
            player.setFlying(true);
        }
    }


    public void createMap(Player player) {
        if (!playerBuilding.containsKey(player)) {
            if (MLGRush.getGameManager().Maps.get(MLGRush.getGameManager().Maps.size() - 1).get("p1z") == null) {
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&cFailed to create Map there may be an issue with the last Map"));
                player.sendTitle(MessageCreator.translate("&cFailed"), MessageCreator.translate("&cto create Map"), 0, 60, 0);
            } else {
                zcord = (int) MLGRush.getGameManager().Maps.get(MLGRush.getGameManager().Maps.size() - 1).get("p1z");
                playerBuilding.put(player, buildMode.MAIN);
                zcord -= 60;
                player.setGameMode(GameMode.CREATIVE);
                player.getWorld().getBlockAt(0, 100, (int) zcord).setType(Material.DIAMOND_BLOCK);
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
                MLGRush.getGameManager().Maps.get(map).put("author", player.getName());
                player.getInventory().setItem(0, StackCreator.createStack(Material.BLAZE_ROD, "&6Build Wand", Arrays.asList("&7Your Tool to edit the Map"), "", true));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] &cBefore finishing or leaving please define the Spawns to prevent bugs"));
            }
        } else {
            player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&cYou can't join the Build-Mode twice"));
        }
    }

    public void helpBuilder(Player builder, Player playerP) {

        playerBuilding.put(playerP, buildMode.HELPER);

        playerP.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You are going to help " + builder.getName()));
        playerP.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Entered Build Mode"), "Helping building", 6, 60, 2);


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
        if (player.isOp()) {
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.setFlying(true);
            playerBuilding.put(player, buildMode.LOBBY);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You are now editing the Lobby please be careful all changes are permanent"));
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Enterd Build Mode"), "Editing the Lobby", 6, 60, 2);
            TextComponent actionbar = new TextComponent(ChatColor.translateAlternateColorCodes('&', "To leave this mode use &c/leave"));
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] &cYou have to be OP to perform this action"));
        }
    }

    public void quitBuildMode(Player player, boolean enableMap, boolean delete) {
        playerBuilding.remove(player);
        for (Map.Entry<Player, buildMode> build : playerBuilding.entrySet()) {
            if (build.getValue() == buildMode.HELPER) {
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
        if (!delete) {
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Leaving Build Mode"), ChatColor.translateAlternateColorCodes('&', "&7Your Map will be &asaved"), 5, 50, 0);
            MLGRush.getGameManager().Maps.get(map).put("verfügbar", true);
            if (enableMap) {
                MLGRush.getGameManager().Maps.get(map).put("finished", true);
            }
        }
    }

    public void setHeight(double h) {
        height = h;
        MLGRush.getGameManager().Maps.get(map).put("height", height);
    }

    public double getHeight() {
        return height;
    }

    public void setRounds(double r) {
        rounds = r;
        MLGRush.getGameManager().Maps.get(map).put("rounds", rounds);
    }

    public double getRounds() {
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
            if (playerBuilding.get(event.getPlayer()) == buildMode.MAIN) {
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
