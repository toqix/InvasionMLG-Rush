package eu.toqix.mlgrush.commands;

import eu.toqix.mlgrush.MLGRush;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class leaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Map<Player, Integer> players = MLGRush.getGameManager().queue;
        Player player = (Player) sender;
        if(players.containsKey(player)) {
            if(players.get(player) == 1) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush§7] §cYou left the Game!"));
                MLGRush.getGameManager().queue.remove(player);
            }else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[§bMLG-Rush&7] &fDu kannst das Spiel erst verlassen wenn es begonnen hat"));
            }
        }else if(MLGRush.getBuildManager().playerBuilding.containsKey(player)) {
            if(MLGRush.getBuildManager().playerBuilding.get(player) == 1) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush-Build§7] §cThe main Builder can't leave the Build No!"));
            }else if(MLGRush.getBuildManager().playerBuilding.get(player) == 4) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush-Build§7] §cYou left the Build-Lobby Mode!"));
                MLGRush.getBuildManager().playerBuilding.remove(player);
                player.teleport(MLGRush.spawn);
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush-Build§7] §cYou left the Build Mode!"));
                MLGRush.getBuildManager().playerBuilding.remove(player);
                player.teleport(MLGRush.spawn);
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();


            }
        }else if (MLGRush.getTrainer().playersInJump.contains(player)){

            MLGRush.getTrainer().leaveJumpAndRun(player);
        }else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "§7[§bMLG-Rush§7] §fDu bist doch nicht in einem Spiel du Witzknochen"));
        }

        return true;
    }
}
