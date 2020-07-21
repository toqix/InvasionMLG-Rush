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
            MLGRush.getBuildManager().inventoryHandler(args, player);
        } else if (command == 'e') {
            MLGRush.getBuildManager().editMapInventory(player, args);
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
                                    }//zettelappel war hier
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
