package eu.toqix.mlgrush.commands;

import eu.toqix.mlgrush.MLGRush;
import eu.toqix.mlgrush.Utils.MessageCreator;
import eu.toqix.mlgrush.Utils.TimeManager;
import eu.toqix.mlgrush.Utils.statsType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Time;
import java.util.HashMap;

public class statsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            Player statsP = null;
            if(args == null || args.length == 0) {
                statsP = player;
            }else {
                if(Bukkit.getPlayer(args[0]) != null) {
                    statsP = Bukkit.getPlayer(args[0]);
                }else {
                    player.sendMessage(MessageCreator.prefix("StatsSystem", "&cDiesen Spieler gibt es nicht du Witzknochen"));
                }
            }
            if(statsP != null) {
                HashMap<statsType, Integer> stats =MLGRush.getStatsManager().getAllStats(statsP);
                if(stats != null) {

                    player.sendMessage(MessageCreator.prefix("StatsSystem", "&7Die Stats von " + statsP.getName()));
                    player.sendMessage("---------------------------------");
                    player.sendMessage(MessageCreator.translate("&6Gespielte Runden: &7&l" + stats.getOrDefault(statsType.ROUNDS, 0)));
                    StringBuilder time = TimeManager.getTimeString(stats.getOrDefault(statsType.PLAYTIME, 0));
                    player.sendMessage(MessageCreator.translate("&6Zeit in Runden: &7&l" + time));
                    player.sendMessage(MessageCreator.translate("&6Gewonnene Runden: &7&l" + stats.getOrDefault(statsType.WINS, 0)));
                    player.sendMessage(MessageCreator.translate("&6Zerstörte Betten: &7&l" + stats.getOrDefault(statsType.BEDS, 0)));
                    player.sendMessage(MessageCreator.translate("&6Tode: &7&l" + stats.getOrDefault(statsType.DEATHS, 0)));
                    player.sendMessage("---------------------------------");
                }
            }

        }
        return true;
    }
}
