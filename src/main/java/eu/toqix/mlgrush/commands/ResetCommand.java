package eu.toqix.mlgrush.commands;

import com.google.common.collect.Maps;
import eu.toqix.mlgrush.MLGRush;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ResetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("Server is reloading maps...");
        }
        for (Integer i : MLGRush.getGameManager().Maps.keySet()) {
            MLGRush.getGameManager().Maps.get(i).put("verfügbar", true);
        }
        try {
            MLGRush.getInstance().safeConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MLGRush.getInstance().loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
