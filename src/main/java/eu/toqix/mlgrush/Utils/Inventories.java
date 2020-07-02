package eu.toqix.mlgrush.Utils;


import eu.toqix.mlgrush.MLGRush;
import eu.toqix.mlgrush.StackCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Inventories {
    public static Inventory mapchooseinv() {
        //MLGRush.setPage(0);
        Inventory inv = Bukkit.createInventory(null, 27, "§2Choose a Map");
        for (int n = 0; n < 27; n++) {
            inv.setItem(n, StackCreator.getNothing());
        }
        inv.setItem(0, StackCreator.createStack(Material.FIREWORK_ROCKET, "&6&lRandom Map", Arrays.asList("&7Startet eine Runde mit", "&7einer zufälligen freien Map"), "z(random)", true));
        inv.setItem(8, StackCreator.createStack(Material.BOOK, "&7Statistics", Arrays.asList("&eMaps: &7" + MLGRush.getGameManager().Maps.size(), "&ePages: &7" + (short) MLGRush.getGameManager().Maps.size() / 9), "", false));
        for (int n = 9; n < 18; n++) {
            inv.setItem(n, new ItemStack(Material.AIR));
        }
        for (int i = 0; i < 9; i++) {

            Integer slot = 9 + i;
            Integer map = MLGRush.getPage() * 9 + i;
            HashMap<Integer, HashMap> maps = MLGRush.getGameManager().Maps;
            if (maps.containsKey(map)) {
                String name = "&9" + maps.get(map).get("name");
                if (maps.get(map).containsKey("finished")) {
                    if (!(Boolean) maps.get(map).get("finished")) {
                        inv.setItem(slot, StackCreator.createStack(Material.BARRIER, name, Arrays.asList("&cMap not available!", "&7MapID:&3 " + map, "&7Author not saved yet"), "", false));
                    } else {
                        if ((Boolean) maps.get(map).get("verfügbar")) {
                            inv.setItem(slot, StackCreator.createStack(Material.SANDSTONE, name, Arrays.asList("&7MapID:&3 " + map, "&7Author not saved yet"), "m(" + map + ")", false));
                        } else {
                            inv.setItem(slot, StackCreator.createStack(Material.BARRIER, name, Arrays.asList("&cMap not available!", "&7MapID:&3 " + map, "&7Author not saved yet"), "", false));
                        }
                    }
                } else {
                    if ((Boolean) maps.get(map).get("verfügbar")) {
                        inv.setItem(slot, StackCreator.createStack(Material.SANDSTONE, name, Arrays.asList("&7MapID:&3 " + map, "&7Author not saved yet"), "m(" + map + ")", false));
                    } else {
                        inv.setItem(slot, StackCreator.createStack(Material.BARRIER, name, Arrays.asList("&cMap not available!", "&7MapID:&3 " + map, "&7Author not saved yet"), "", false));
                    }
                }
            }
        }
        inv.setItem(18, StackCreator.createStack(Material.END_CRYSTAL, "&7Previous Page", Arrays.asList("&7Current Page: " + MLGRush.getPage()), "p(-)", false));
        inv.setItem(26, StackCreator.createStack(Material.END_CRYSTAL, "&7Next Page", Arrays.asList("&7Current Page: " + MLGRush.getPage()), "p(+)", false));
        return inv;
    }

    public static Inventory chooseBuildMode() {
        Inventory inv = Bukkit.createInventory(null, 45, "§2Choose a Mode");
        for (int n = 0; n < 18; n++) {
            inv.setItem(n, StackCreator.getNothing());
        }
        for (int n = 26; n < 45; n++) {
            inv.setItem(n, StackCreator.getNothing());
        }
        inv.setItem(18, StackCreator.createStack(Material.SANDSTONE, "&6Edit an Existing Map", Arrays.asList("&7Let's you choose", "&7and edit an existing Map"), "b(edit)", true));
        inv.setItem(20, StackCreator.createStack(Material.SANDSTONE, "&aCreate a New Map", Arrays.asList("&7Let's you creat", "&7a great new Map"), "b(create)", false));
        inv.setItem(22, StackCreator.createStack(Material.GRASS_BLOCK, "&6Edit the Lobby", Arrays.asList("&7Place and Destory Blocks", "&7in the Lobby", "&7Op &crequired"), "b(lobby)", false));

        return inv;
    }

    public static Inventory editAMap() {
        // MLGRush.setPage(0);
        Inventory inv = Bukkit.createInventory(null, 27, "§2Choose a Map to edit");
        for (int n = 0; n < 27; n++) {
            inv.setItem(n, StackCreator.getNothing());
        }
        inv.setItem(0, StackCreator.createStack(Material.BARRIER, "&7Back", Arrays.asList(""), "b(back)", false));
        inv.setItem(8, StackCreator.createStack(Material.BOOK, "&7All of our great Builders", Arrays.asList("&etoqix", "&edia_block_mcg", "&eqrashi", "&ebetz_bua"), "", false));
        for (int n = 9; n < 18; n++) {
            inv.setItem(n, new ItemStack(Material.AIR));
        }
        for (int i = 0; i < 9; i++) {

            Integer slot = 9 + i;
            Integer map = MLGRush.getPage() * 9 + i;
            HashMap<Integer, HashMap> maps = MLGRush.getGameManager().Maps;
            if (maps.containsKey(map)) {
                String name = "&9" + maps.get(map).get("name");
                if (maps.get(map).containsKey("finished")) {
                    if (!(Boolean) maps.get(map).get("finished") && (Boolean) maps.get(map).get("verfügbar")) {
                        inv.setItem(slot, StackCreator.createStack(Material.SANDSTONE, name, Arrays.asList("&7MapID:&3 " + map, "&7Map unfinished or beeing edited", "&7Author not saved yet"), "e(" + map + ")", false));
                    }else if((Boolean) maps.get(map).get("finished") && (Boolean) maps.get(map).get("verfügbar")) {
                        inv.setItem(slot, StackCreator.createStack(Material.SANDSTONE, name, Arrays.asList("&7MapID:&3 " + map, "&7Map unfinished or beeing edited", "&7Author not saved yet"), "e(" + map + ")", false));
                    }else {
                        inv.setItem(slot, StackCreator.createStack(Material.BARRIER, name, Arrays.asList("&cMap not available!", "&7MapID:&3 " + map, "&7Author not saved yet"), "", false));
                    }
                } else {
                    if ((Boolean) maps.get(map).get("verfügbar")) {
                        inv.setItem(slot, StackCreator.createStack(Material.SANDSTONE, name, Arrays.asList("&7MapID:&3 " + map, "&7Author not saved yet"), "e(" + map + ")", false));
                    } else {
                        inv.setItem(slot, StackCreator.createStack(Material.BARRIER, name, Arrays.asList("&cMap not available!", "&7MapID:&3 " + map, "&7Author not saved yet"), "", false));
                    }
                }
            }
        }
        inv.setItem(18, StackCreator.createStack(Material.END_CRYSTAL, "&7Previous Page", Arrays.asList("&7Current Page: " + MLGRush.getPage()), "p(e-)", false));
        inv.setItem(26, StackCreator.createStack(Material.END_CRYSTAL, "&7Next Page", Arrays.asList("&7Current Page: " + MLGRush.getPage()), "p(e+)", false));
        return inv;
    }

    public static Inventory buildHelper() {
        Inventory inv = Bukkit.createInventory(null, 27, "§2Build Mode Full");
        for (int n = 0; n < 27; n++) {
            inv.setItem(n, StackCreator.getNothing());
        }
        inv.setItem(13, StackCreator.createStack(Material.LIME_DYE, "&6Help", Arrays.asList("&7Help the currently online Builder"), "b(help)", false));
        return inv;
    }

    public static Inventory buildModeWand() {
        Inventory inv = Bukkit.createInventory(null, 27, "§2Build Tools");
        for (int n = 0; n < 27; n++) {
            inv.setItem(n, StackCreator.getNothing());
        }
        inv.setItem(0, StackCreator.createStack(Material.BARRIER, "&cQuit Build Mode", Arrays.asList("&7Quits the Build Mode", "&7and saves the current Map", "&cbut doesn't enable the to play it save it"), "b(quit)", false));
        inv.setItem(16, StackCreator.createStack(Material.BOOK, "&6Set Name", Arrays.asList("&7With &e/name &7+ name", "&7Please Note that the name &acan &7also", "&7Include spaces"), "", true));
        inv.setItem(9, StackCreator.createStack(Material.RED_STAINED_GLASS_PANE, "&cTeam Red Spawn", Arrays.asList("&7Gives you a tool to set", "&7the Spawn point of team Red"), "b(red)", false));
        inv.setItem(10, StackCreator.createStack(Material.BLUE_STAINED_GLASS_PANE, "&9Team Blue Spawn", Arrays.asList("&7Gives you a tool to set", "&7the Spawn point of team Blue"), "b(blue)", false));
        inv.setItem(3, StackCreator.createStack(Material.LIME_DYE, "&7&l+", Arrays.asList(""), "b(h+)", false));
        inv.setItem(12, StackCreator.createStack(Material.PAPER, "&6Height", Arrays.asList("&7Current height: &l" + MLGRush.getBuildManager().getHeight(), "&7Increase or decrease", "&7the max build height"), "", true));
        inv.setItem(21, StackCreator.createStack(Material.RED_DYE, "&7&l-", Arrays.asList(""), "b(h-)", false));
        inv.setItem(5, StackCreator.createStack(Material.LIME_DYE, "&7&l+", Arrays.asList(""), "b(r+)", false));
        inv.setItem(14, StackCreator.createStack(Material.RED_BED, "&6Rounds", Arrays.asList("&7Current rounds: &l" + MLGRush.getBuildManager().getRounds(), "&7Increase or decrease", "&7the rounds to win"), "", true));
        inv.setItem(23, StackCreator.createStack(Material.RED_DYE, "&7&l-", Arrays.asList(""), "b(r-)", false));
        inv.setItem(8, StackCreator.createStack(Material.LIME_DYE, "&a&lFinish", Arrays.asList("&7Quits the Build Mode", "&7and saves the curren Map", "&aAlso enables it"), "b(save)", false));

        inv.setItem(18, StackCreator.createStack(Material.RED_STAINED_GLASS, "&7Delete the Map Data", Arrays.asList("&7Deletes all the saved Data about the Map", "&7This Doesn't delete the Blocks", "&7Only all data which makes the Map a Map"), "b(delete)", false));
        inv.setItem(26, StackCreator.createStack(Material.RED_DYE, "&cDelete the Map", Arrays.asList("&cDeletes the whole Map", "&c&lThis is not undoable"), "b(deleteall)" ,false));
        return inv;
    }

    public static Inventory mlgTrainer() {
        Inventory inv = Bukkit.createInventory(null, 27, "§6MLG-Trainer");
        for(int i = 0; i<27; i++) {
            inv.setItem(i, StackCreator.getNothing());
        }
        inv.setItem(11, StackCreator.createStack(Material.COBWEB, "&fCobWeb MLG" , Arrays.asList("&7Mit höhe"), "t(web)", false));
      //  inv.setItem(11, StackCreator.createStack(Material.COBWEB, "&fCobweb MLG", Arrays.asList("&7Ohne Höhe"), "t(sweb)", true));
        inv.setItem(15, StackCreator.createStack(Material.LADDER, "&6Leiter MLG", Arrays.asList("&7Ohne Blöcke") , "t(leiter)" , false));
        //inv.setItem(15, StackCreator.createStack(Material.LADDER, "&6Leiter MLG", Arrays.asList("&7Mit Blöcken") , "t(sleiter)" , true));
        return inv;
    }

}