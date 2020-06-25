package eu.toqix.mlgrush.commands;

import eu.toqix.mlgrush.MLGRush;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setName implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(MLGRush.getBuildManager().playerBuilding.containsKey((Player) sender)) {
            String name = "";
            for (String str : args) {
                name += str;
                name += " ";
            }
            if(MLGRush.getGameManager().Maps.containsKey(MLGRush.getBuildManager().map)) {
                MLGRush.getGameManager().Maps.get(MLGRush.getBuildManager().map).put("name", name);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] You set the Name of " + "map " + "to: &6" + name));
        }else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] &c You are not editing a Map"));
        }
        return true;
    }
}
