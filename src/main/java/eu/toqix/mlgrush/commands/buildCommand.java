package eu.toqix.mlgrush.commands;

import eu.toqix.mlgrush.Utils.InvOpener;
import eu.toqix.mlgrush.Utils.Inventories;
import eu.toqix.mlgrush.MLGRush;
import eu.toqix.mlgrush.Utils.MessageCreator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class buildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (sender.isOp() || sender.getName() == "toqix") {
           /* if(mlgrushtest.getInstance().buildMode) {
                mlgrushtest.getInstance().buildMode = false;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eBuild &cdisabled!"));
                player.setGameMode(GameMode.SURVIVAL);
            }else {
                mlgrushtest.getInstance().buildMode = true;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eBuild &aenabled!"));
                player.setGameMode(GameMode.CREATIVE);
            }*/
        }
        if (!MLGRush.getGameManager().queue.containsKey(player)) {
            if (MLGRush.getBuildManager().playerBuilding.keySet().size() < 1) {
                InvOpener.openDelay(player, Inventories.chooseBuildMode());
            } else {
                Boolean builderOnline = false;
                for (int i : MLGRush.getBuildManager().playerBuilding.values()) {
                    if (i == 1) {
                        builderOnline = true;
                    }
                }
                if (builderOnline) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] &cTo prevent a Plugin Bug there is currently a Limit to Building Players"));
                    InvOpener.openDelay(player, Inventories.buildHelper());
                } else {
                    InvOpener.openDelay(player, Inventories.chooseBuildMode());
                }

            }
        }else {
            player.sendMessage(MessageCreator.translate("&7[&bMLG-Rush-Build&7] &c You can't join Build-Mode whilst in queue"));
        }

        return true;
    }
}
