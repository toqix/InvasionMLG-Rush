package eu.toqix.mlgrush.commands;

import eu.toqix.mlgrush.Utils.InvOpener;
import eu.toqix.mlgrush.Utils.Inventories;
import eu.toqix.mlgrush.MLGRush;
import eu.toqix.mlgrush.Utils.MessageCreator;
import eu.toqix.mlgrush.buildMode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class buildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (!MLGRush.getGameManager().queue.containsKey(player)) {
            if (!MLGRush.getTrainer().playersTraining.contains(player)) {
                if (MLGRush.getBuildManager().playerBuilding.keySet().size() < 1) {
                    InvOpener.openDelay(player, Inventories.chooseBuildMode());
                } else {
                    boolean builderOnline = false;
                    for (buildMode mode : MLGRush.getBuildManager().playerBuilding.values()) {
                        if (mode == buildMode.MAIN) {
                            builderOnline = true;
                            break;
                        }
                    }
                    if (builderOnline) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush-Build&7] &cTo prevent a Plugin Bug there is currently a Limit to Building Players"));
                        InvOpener.openDelay(player, Inventories.buildHelper());
                    } else {
                        InvOpener.openDelay(player, Inventories.chooseBuildMode());
                    }
                }
            } else {
                player.sendMessage(MessageCreator.translate("&7[&bMLG-Rush&7] You can't join Build-Mode whilst in Trainer"));
            }

        } else {
            player.sendMessage(MessageCreator.translate("&7[&bMLG-Rush-Build&7] &c You can't join Build-Mode whilst in queue"));
        }

        return true;
    }
}
