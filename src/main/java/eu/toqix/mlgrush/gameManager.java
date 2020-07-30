package eu.toqix.mlgrush;


import eu.toqix.mlgrush.Utils.InvOpener;
import eu.toqix.mlgrush.Utils.Inventories;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;


public final class gameManager implements Listener {

    public static Location p1 = new Location(Bukkit.getWorld("world"), 13.5, 101, 0.5);
    public static Location p2 = new Location(Bukkit.getWorld("world"), -13.5, 101, 0.5);


    MLGRush main = MLGRush.getInstance();
    public Map<Player, Integer> queue = new HashMap<Player, Integer>();

    public HashMap<Integer, HashMap> Maps = new HashMap<>();

    public int selectedMap = -1;


    Player player1 = null;
    Player player2 = null;
    Integer chooseTime = 0;


    public void init() {
        for (Integer i : Maps.keySet()) {
            if (Maps.containsKey(i)) {
                Maps.get(i).put("verfügbar", true);
            }
        }
    }

    public void joinQueue(Player player) {
        queue.put(player, 0);
    }

    public void leaveQueue(Player player) {
        queue.remove(player);
        if (player1 == player) {
            player1 = null;
        } else if (player2 == player) {
            player2 = null;
        }
    }




    public void startGames() {


        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Player, Integer> Queue : queue.entrySet()) {
                    if (Queue.getValue() == 0) {
                        if(!Queue.getKey().isOnline()) {
                            queue.remove(Queue.getKey());
                        }
                        if (player1 == null) {
                            player1 = Queue.getKey();

                        } else if (player2 == null && player1 != Queue.getKey()) {
                            player2 = Queue.getKey();
                            queue.put(player2, 1);
                            queue.put(player1, 1);

                            player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush&7] &eA Match with " + player2.getName() + " was found"));
                            player2.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush&7] &eA Match with " + player1.getName() + " was found"));

                        }

                    }
                }
                if(player1 != null) {
                    if(!player1.isOnline()) {
                        player1 = null;
                    }
                }
                if(player2 != null) {
                    if(!player2.isOnline()) {
                        player2 = null;
                    }
                }
                if (player1 != null && player2 != null) {
                    if(player1.getName().equals(player2.getName())) {
                        leaveQueue(player1);
                        leaveQueue(player2);
                        player1 = null;
                        player2 = null;

                    }
                    chooseTime++;
                    if (chooseTime < 10) {
                        InvOpener.openDelay(player1, Inventories.mapchooseinv());
                    } else {
                        chooseTime = 0;

                        String author = "unknown";
                        gameRunner game = new gameRunner();
                        Bukkit.getPluginManager().registerEvents(game, MLGRush.getInstance());
                        game.saveInstance(game);
                        List<Integer> verfügbareMaps = new ArrayList<>();

                        for (int i : Maps.keySet()) {
                            if ((boolean) Maps.get(i).get("verfügbar")) {
                                verfügbareMaps.add(i);
                            }
                        }
                        Random random = new Random();
                        Integer map;


                        if (selectedMap >= 0 && (Boolean) Maps.get(selectedMap).get("verfügbar")) {
                            map = selectedMap;
                        } else {
                            map = verfügbareMaps.get(random.nextInt(verfügbareMaps.size()));
                        }
                        Maps.get(map).put("verfügbar", false);

                        Location spawn1 = new Location(player1.getWorld(), (double) Maps.get(map).get("p1x"), (double) Maps.get(map).get("p1y"), (double) Maps.get(map).get("p1z"));
                        Location spawn2 = new Location(player1.getWorld(), (double) Maps.get(map).get("p2x"), (double) Maps.get(map).get("p2y"), (double) Maps.get(map).get("p2z"));
                        player1.closeInventory();
                        player2.closeInventory();
                        MLGRush.getTrainer().playersInBuild.remove(player1);
                        MLGRush.getTrainer().playersInBuild.remove(player2);
                        MLGRush.getTrainer().playersInJump.remove(player1);
                        MLGRush.getTrainer().playersInJump.remove(player2);
                        MLGRush.getTrainer().playersInMLG.remove(player1);
                        MLGRush.getTrainer().playersInMLG.remove(player2);
                        MLGRush.getTrainer().playersTraining.remove(player1);
                        MLGRush.getTrainer().playersTraining.remove(player2);

                        double games = 10;
                        if (Maps.get(map).containsKey("rounds")) {
                            games = (double) Maps.get(map).get("rounds");
                        }
                        double height = 10;
                        if (Maps.get(map).containsKey("height")) {
                            height = (double) Maps.get(map).get("height");
                        }
                        if(Maps.get(map).containsKey("author")) {
                            author = (String) Maps.get(map).get("author");
                        }

                        game.runGame(player1, player2, spawn1, spawn2, (String) Maps.get(map).get("name"), author, games, map, height);

                        player1 = null;
                        player2 = null;
                    }
                }
            }

        }.runTaskTimer(MLGRush.getInstance(), 0, 40);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item != null && item.hasItemMeta()) {
            if (queue.containsKey(player)) {
                if (player == player1) {

                    if (item.getItemMeta().hasLore()) {
                        for (Integer i : Maps.keySet()) {
                            if ((Boolean) Maps.get(i).get("verfügbar")) {
                                if (ChatColor.translateAlternateColorCodes('&', "&6" + (String) Maps.get(i).get("name")).equals(item.getItemMeta().getDisplayName())) {

                                    selectedMap = i;
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bMLG-Rush&7] &eYou choose the Map " + item.getItemMeta().getDisplayName()));
                                    event.setCancelled(true);
                                    event.setCursor(new ItemStack(Material.AIR));
                                    player.closeInventory();


                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
