package eu.toqix.mlgrush;

import eu.toqix.mlgrush.Listeners.*;
import eu.toqix.mlgrush.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class MLGRush extends JavaPlugin {

    private static MLGRush instance;
    private static World world;
    private static buildModeManager build;

    public boolean buildMode = false;


    private static gameManager manager;

    public String pp1 = null;
    public String pp2 = null;
    Player player1;
    Player player2;
    public Map<String, Integer> playerBlocks = new HashMap<String, Integer>();
    public Map<String, Integer> playerPicke = new HashMap<String, Integer>();
    public Map<String, Integer> playerStick = new HashMap<String, Integer>();



    public static Location p1 = new Location(Bukkit.getWorld("world"), 13.5, 101, 0.5);
    public static Location p2 = new Location(Bukkit.getWorld("world"), -13.5, 101, 0.5);
    public static Location spawn = new Location(Bukkit.getWorld("world"), 0.5, 105, 155.5);

    public int gameTime = 0;
    public boolean gameHasStarted = false;
    public boolean playerStartedGame = false;

    private boolean playersOnMap = false;

    private static BuildTrainer trainer;

    private static Integer page;
    private BuildTrainer buildTrainer;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        manager = new gameManager();
        build = new buildModeManager();
        buildTrainer = new BuildTrainer();

        try {
            loadData();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        manager.init();

        getLogger().info(Bukkit.getWorld("world").getClass().getSimpleName());
        setInstance(this);
        setPage(0);
        // Plugin startup logic
        p1.setYaw(90);
        p2.setYaw(270);
        spawn.setYaw(0);
        buildTrainer.runTrainer();
        trainer = buildTrainer;

        for(Player player : Bukkit.getOnlinePlayers()) {
            spawn.setWorld(player.getWorld());

            player.teleport(spawn);
        }

        listenerRegistration();
        getLogger().info("MLG-Rush enabled");
        getLogger().info("The nice Plugin by @toqix");
        commandRegistration();
        // resetBlocks(40, 40, -15, 15);

        manager.startGames();
        build.handler();
        MLGRush.resetBlocks(70, 60, 160, 400);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getLogger().info("Saving Inventorys");
        try {
            safeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        getInstance().getConfig().getMapList("playerconfig.inventory").set(0, playerStick);
        getInstance().getConfig().getMapList("playerconfig.inventory").set(1, playerPicke);
        getInstance().getConfig().getMapList("playerconfig.inventory").set(2, playerBlocks);
        */

        getLogger().info("aus");
    }

    public static gameManager getGameManager() {
        return manager;
    }
    public static buildModeManager getBuildManager() {
        return build;
    }

    public static void resetBlocks(int x, int y, int z1, int z2) {
        for (int xa = -x;xa<x;xa++) {
            for (int ya = 100-y; ya<100+y; ya++) {
                for (int za = z1; za<z2; za++) {

                    Block block = Bukkit.getWorld("world").getBlockAt(xa, ya, za);
                    if(block.getType() != Material.AIR) {
                        if(block.getType() == Material.SANDSTONE) {
                            Bukkit.getWorld("world").getBlockAt(xa,ya,za).setType(Material.AIR);
                        }
                    }

                }
            }
        }

    }

    private void commandRegistration() {
        getCommand("si").setExecutor(new SaveInventoryCommand());
        getCommand("build").setExecutor(new buildCommand());
        getCommand("startgame").setExecutor(new startCommand());
        getCommand("leave").setExecutor(new leaveCommand());
        getCommand("god").setExecutor(new godCommand());
        getCommand("name").setExecutor(new setName());
    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new BlockListeners(), this);
        pluginManager.registerEvents(new ItemDropListener(), this);
        pluginManager.registerEvents(new PlayerListeners(), this);
        pluginManager.registerEvents(manager, this);
        pluginManager.registerEvents(new StackCreator(), this);
        pluginManager.registerEvents(getBuildManager(), this);
        pluginManager.registerEvents(buildTrainer, this);
        // pluginManager.registerEvents(new gameRunner(), this);

    }

    public static void setPage(Integer toSet) {
        page = toSet;
    }

    public static Integer getPage() {
        return page;
    }

    private void loadData() throws OptionalDataException, ClassNotFoundException, IOException{
        for(String str:getConfig().getKeys(true)) {
            if(str.contains("player.inventory.stick.")) {
                int p = getConfig().getInt(str);
                playerStick.put(str.substring(23), p);
            }
        }
        for(String str:getConfig().getKeys(true)) {
            if(str.contains("player.inventory.picke.")) {
                int p = getConfig().getInt(str);
                playerPicke.put(str.substring(23), p);
            }
        }
        for(String str:getConfig().getKeys(true)) {
            if(str.contains("player.inventory.blocks.")) {
                int p = getConfig().getInt(str);
                playerBlocks.put(str.substring(24), p);
            }
        }
        String filepath = this.getDataFolder().getPath() + "/maps.properties";
        File file = new File(filepath);
        FileInputStream f = new FileInputStream(file);
        ObjectInputStream s = new ObjectInputStream(f);
        manager.Maps = (HashMap<Integer, HashMap>) s.readObject();
        s.close();


    }
    private void safeConfig() throws FileNotFoundException, IOException {

        for(Map.Entry<String, Integer> stick:playerStick.entrySet()) {

            this.getConfig().set("player.inventory.stick." + stick.getKey(), stick.getValue());
            this.saveConfig();
        }
        for(Map.Entry<String, Integer> picke:playerPicke.entrySet()) {
            this.getConfig().set("player.inventory.picke." + picke.getKey(), picke.getValue());
            this.saveConfig();
        }
        for(Map.Entry<String, Integer> blocks:playerBlocks.entrySet()) {
            this.getConfig().set("player.inventory.blocks." + blocks.getKey(), blocks.getValue());
            this.saveConfig();

        }

        String filepath = this.getDataFolder().getPath() + "/maps.properties";
        File file = new File(filepath);

        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(manager.Maps);
        s.close();

        //this.getConfig().set("maps", manager.Maps);
        //this.saveConfig();

    }

    public static BuildTrainer getTrainer() {
        return trainer;
    }

    public static MLGRush getInstance() {
        return instance;
    }

    public static void setInstance(MLGRush instance) {
        MLGRush.instance = instance;
    }

}
