package eu.toqix.mlgrush;


import eu.toqix.mlgrush.Utils.InvOpener;
import eu.toqix.mlgrush.Utils.Inventories;
import eu.toqix.mlgrush.Utils.MessageCreator;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.IntStream;

public class StackCreator implements Listener {
    public static ItemStack createStack(Material material, String name, List<String> lore, String command, boolean enchanted) {
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta stack_meta = stack.getItemMeta();
        assert stack_meta != null;
        stack_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> newlore = new java.util.ArrayList<>(Collections.emptyList());
        for (String i : lore) {
            newlore.add(ChatColor.translateAlternateColorCodes('&', i));
        }
        newlore.add(ChatColor.translateAlternateColorCodes('&', "&0&o" + command));
        newlore.add(ChatColor.translateAlternateColorCodes('&', "&0&oMLG-Rush"));
        stack_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        stack_meta.setLore(newlore);
        stack.setItemMeta(stack_meta);
        if (enchanted) {
            stack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
        return stack;
    }

    public static ItemStack getNothing() {
        ItemStack nothing = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta nothing_meta = nothing.getItemMeta();
        assert nothing_meta != null;
        nothing_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', " "));
        nothing_meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&0&oMLG-Rush")));
        nothing.setItemMeta(nothing_meta);
        return nothing;
    }

    public void handleClick(char command, String args, Player player) {
        if (command == 'z') {
            if (args.equals("random")) {
                MLGRush.getGameManager().selectedMap = -1;
                MLGRush.getGameManager().chooseTime = 21;
                InvOpener.closeDelay(player);
            }
        } else if (command == 'm') {
            MLGRush.getGameManager().selectedMap = Integer.parseInt(args);
            MLGRush.getGameManager().chooseTime = 21;
            InvOpener.closeDelay(player);
        } else if (command == 'p') {
            if (args.equals("-")) {
                if (MLGRush.getPage() != 0) {
                    MLGRush.setPage(MLGRush.getPage() - 1);
                }
                InvOpener.openDelay(player, Inventories.mapchooseinv());
            } else if (args.equals("+")) {
                MLGRush.setPage(MLGRush.getPage() + 1);
                InvOpener.openDelay(player, Inventories.mapchooseinv());
            } else if (args.equals("e-")) {
                if (MLGRush.getPage() != 0) {
                    MLGRush.setPage(MLGRush.getPage() - 1);
                }
                InvOpener.openDelay(player, Inventories.editAMap());
            } else if (args.equals("e+")) {
                MLGRush.setPage(MLGRush.getPage() + 1);
                InvOpener.openDelay(player, Inventories.editAMap());
            }

        } else if (command == 'b') {
            if (args.equals("edit")) {
                if(player.isOp()) {
                    InvOpener.openDelay(player, Inventories.editAMap());
                }else {
                    InvOpener.closeDelay(player);
                    player.sendMessage(MessageCreator.translate("&7[&bMLG-Rush-Build&7] &cSorry you don't have permissions to edit this map if you think this is a mistake please contact us on Discord"));
                }
            } else if (args.equals("back")) {
                InvOpener.openDelay(player, Inventories.chooseBuildMode());
            } else if (args.equals("quit")) {
                MLGRush.getBuildManager().quitBuildMode(player, false, false);
            } else if (args.equals("save")) {
                MLGRush.getBuildManager().quitBuildMode(player, true, false);
            } else if (args.equals("create")) {
                InvOpener.closeDelay(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You are going to create a new Map"));
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Enterd Build Mode"), "Creating: A Great New Map", 6, 60, 2);
                MLGRush.getBuildManager().createMap(player);
            } else if (args.equals("red")) {
                InvOpener.closeDelay(player);
                player.getInventory().setItem(4, StackCreator.createStack(Material.RED_STAINED_GLASS_PANE, "&cTeam Red", Arrays.asList("&7Sets the respawn point", "&7of Team Red"), "", false));
            } else if (args.equals("blue")) {
                InvOpener.closeDelay(player);
                player.getInventory().setItem(4, StackCreator.createStack(Material.BLUE_STAINED_GLASS_PANE, "&9Team Blue", Arrays.asList("&7Sets the respawn point", "&7of Team Blue"), "", false));
            } else if (args.equals("help")) {
                if (MLGRush.getBuildManager().playerBuilding.size() > 0) {
                    Boolean builderOnline = false;
                    for (int i : MLGRush.getBuildManager().playerBuilding.values()) {
                        if (i == 1) {
                            builderOnline = true;
                            break;
                        }
                    }

                    if (builderOnline) {
                        Player build;
                        for (Map.Entry<Player, Integer> builder : MLGRush.getBuildManager().playerBuilding.entrySet()) {
                            if (builder.getValue() == 1) {
                                build = builder.getKey();
                                MLGRush.getBuildManager().helpBuilder(build, player);
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Oops Something went wrong hit toqix when this error stays");
                    }
                }

            } else if (args.equals("delete")) {
                MLGRush.getBuildManager().deleteMap(false);
                MLGRush.getBuildManager().quitBuildMode(player, false, true);
            } else if (args.equals("deleteall")) {
                MLGRush.getBuildManager().deleteMap(true);
                MLGRush.getBuildManager().quitBuildMode(player, false, true);
            } else if (args.equals("lobby")) {
                MLGRush.getBuildManager().editLobby(player);
            } else if (args.equals("h+")) {
                InvOpener.openDelay(player, Inventories.buildModeWand());
                MLGRush.getBuildManager().setHeight(MLGRush.getBuildManager().getHeight() + 1);
            } else if (args.equals("h-")) {
                InvOpener.openDelay(player, Inventories.buildModeWand());
                if (MLGRush.getBuildManager().getHeight() > 0) {
                    MLGRush.getBuildManager().setHeight(MLGRush.getBuildManager().getHeight() - 1);
                }
            } else if (args.equals("r+")) {
                InvOpener.openDelay(player, Inventories.buildModeWand());
                MLGRush.getBuildManager().setRounds(MLGRush.getBuildManager().getRounds() + 1);
            } else if (args.equals("r-")) {
                InvOpener.openDelay(player, Inventories.buildModeWand());
                if (MLGRush.getBuildManager().getRounds() > 2) {
                    MLGRush.getBuildManager().setRounds(MLGRush.getBuildManager().getRounds() - 1);
                }
            }
        } else if (command == 'e') {
            InvOpener.closeDelay(player);
            Integer Map = Integer.parseInt(args);
            String author = "unkown";
            if(MLGRush.getGameManager().Maps.get(Map).containsKey("author")) {
                author = (String) MLGRush.getGameManager().Maps.get(Map).get("author");
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You are going to edit " + MLGRush.getGameManager().Maps.get(Integer.parseInt(args)).get("name") + " by " + author));

            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6Enterd Build Mode"), "Editing: " + MLGRush.getGameManager().Maps.get(Map).get("name") + "  by " + author, 6, 60, 2);

            if (MLGRush.getGameManager().Maps.get(Map).containsKey("verfügbar")) {
                MLGRush.getGameManager().Maps.get(Map).put("verfügbar", false);
                MLGRush.getGameManager().Maps.get(Map).put("finished", false);
                MLGRush.getBuildManager().editMap(Map, player);
            }
        }else if(command == 't') {
            InvOpener.closeDelay(player);
            MLGRush.getTrainer().startMLG(player, args);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked != null) {
            if (clicked.getType() != Material.AIR) {
                if (clicked.hasItemMeta()) {
                    ItemMeta meta = clicked.getItemMeta();
                    if (meta != null) {
                        if (meta.hasLore()) {
                            List lore = meta.getLore();
                            if (lore != null) {
                                if (lore.size() > 0) {
                                    if (ChatColor.stripColor((String) lore.get(lore.size() - 1)).equals("MLG-Rush")) {
                                        event.setCancelled(true);
                                    }
                                    if (lore.size() > 1) {
                                        String commandraw = ChatColor.stripColor((String) lore.get(lore.size() - 2));
                                        HumanEntity player = event.getWhoClicked();
                                        Player playerP = Bukkit.getPlayer(player.getName());
                                        if (playerP != null) {
                                            playerP.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                        }
                                        if (commandraw.length() > 0) {
                                            Character command = commandraw.charAt(0);
                                            StringBuilder arg = new StringBuilder();
                                            IntStream.range(2, commandraw.length() - 1).forEachOrdered(n -> arg.append(commandraw.charAt(n)));
                                            //Bukkit.broadcastMessage("Command: " + command + " Action: " + arg);
                                            String args = arg.toString();
                                            handleClick(command, args, playerP);

                                        }


                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack clicked = event.getCursor();
        if (clicked != null) {
            if (clicked.getType() != Material.AIR) {
                if (clicked.hasItemMeta()) {
                    ItemMeta meta = clicked.getItemMeta();
                    if (meta != null) {
                        if (meta.hasLore()) {
                            List lore = meta.getLore();
                            if (lore != null) {
                                if (lore.size() > 0) {

                                    if (ChatColor.stripColor((String) lore.get(lore.size() - 1)).equals("InvasionBW")) {
                                        event.setCancelled(true);
                                    }
                                    if (lore.size() > 1) {
                                        String commandraw = ChatColor.stripColor((String) lore.get(lore.size() - 2));
                                        HumanEntity player = event.getWhoClicked();
                                        Player playerP = Bukkit.getPlayer(player.getName());
                                        if (playerP != null) {
                                            playerP.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                        }
                                        if (commandraw.length() > 0) {
                                            Character command = commandraw.charAt(0);
                                            StringBuilder arg = new StringBuilder();
                                            IntStream.range(2, commandraw.length() - 1).forEachOrdered(n -> arg.append(commandraw.charAt(n)));
                                            //Bukkit.broadcastMessage("Command: " + command + " Action: " + arg);
                                            String args = arg.toString();
                                            handleClick(command, args, playerP);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
