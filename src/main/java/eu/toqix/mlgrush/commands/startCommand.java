package eu.toqix.mlgrush.commands;

import eu.toqix.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class startCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(MLGRush.getBuildManager().playerBuilding.containsKey(player)) {
            player.sendMessage("§7[§bMLG-Rush§7] §cYou cannot join a queue whilst in Build Mode");
        }else {
            if (MLGRush.getGameManager().queue.containsKey(player)) {

                if (MLGRush.getGameManager().queue.get(player) == 0) {
                    MLGRush.getGameManager().leaveQueue(player);
                    player.sendMessage("§7[§bMLG-Rush§7] §cYou left the §b§lMLG-Rush§r§c queue");
                } else {
                    player.sendMessage("§7[§bMLG-Rush§7] §cYou cannot leave or join a queue whilst in a Game");
                }

            } else {

                player.sendMessage("§7[§bMLG-Rush§7] §aYou joined the §b§lMLG-Rush§r§a queue");
                MLGRush.getGameManager().joinQueue(player);


            }
        }
        return true;
    }
}
