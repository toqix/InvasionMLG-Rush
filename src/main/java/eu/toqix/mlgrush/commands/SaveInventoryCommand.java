package eu.toqix.mlgrush.commands;

import eu.toqix.mlgrush.MLGRush;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SaveInventoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            MLGRush mlgrush = MLGRush.getInstance();

            Player player = (Player) sender;
            String name = player.getUniqueId().toString();

            Inventory inv = player.getInventory();
            for (int id = 0; id <= 8; id++) {
                ItemStack item = inv.getItem(id);
                if(item != null) {
                    if(item.getType() == Material.STICK) {
                        mlgrush.playerStick.put(name, id);
                    }else if(item.getType() == Material.WOODEN_PICKAXE) {
                        mlgrush.playerPicke.put(name, id);
                    }else if(item.getType() == Material.SANDSTONE) {
                        mlgrush.playerBlocks.put(name, id);
                    }
                }
            }


        }else {

        }
        return false;
    }
}