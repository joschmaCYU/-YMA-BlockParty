// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.InitializeArena;
import fr.joschma.BlockParty.Arena.State.SongProvider;
import fr.joschma.BlockParty.Gui.ArenaGui;
import fr.joschma.BlockParty.Gui.ChoseSongGUI;
import fr.joschma.BlockParty.Gui.JoinArenaGui;
import fr.joschma.BlockParty.Listener.*;
import fr.joschma.BlockParty.Manager.*;
import fr.joschma.BlockParty.Messages.Debugger;
import fr.joschma.BlockParty.TabFinisher.TabCompletor;
import fr.joschma.BlockParty.Utils.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BPM extends JavaPlugin {
    /* 3. Be able to pause the game /bp pause (game paused), and if you run the command again it will say game resumed.
    4. Be able to change the speed round by using /bp round (round) - (screenshot below).*/
    // TODO next:
    // TODO /bp reload reload language file
    // TODO add settings to help config
    // TODO update from git Song help section
    // TODO bungee && sql && point system && file foreach command
    // TODO Players that die should be in survival and invisible with a leave game item as it is impossible for them to leave
    // TODO more powerups,
    // TODO more placeholders, etc...
    // TODO scoreboard more flexible and showing in waitting time.


    // TODO Boss bar (1.32)

    // TODO smart random join (1.31)

    // TODO configurable item (1.30)

    // TODO lobbymusic (1.29)

    // TODO powerup: Safe Walk //

    // TODO UPDATE WIKI

    // TODO progressive floor loading

    // update 1.44
    // you can now use me in placeholders (you already could with player_name)
    // custom head power up
    // give colour group block
    // fixed bug only showing default particle colour

    // update 1.45


    // TODO:
    // admin forcestart item --  vip item/command --
    // fix bug first block
    // bungee support
    // Add default custom dance floor
    // Boss bar
    // best time
    // scoreboard with all players (video: https://www.youtube.com/watch?v=6qZSIGrRjXU)
    // loading blocks
    // add creeper it will bother player by exploding
    // more info in choose song gui (like music duration, artist)

    // TODO
    // give example colour not colour on ground
    // animation rotate + up&down custom head
    // make particle on power up pick up

    // Automatic config.yml update --> change version from int to string

    // TODO test pause feature

    // new
    // Updated x series library
    // BUNGEEEEEEEE
    //

    // TODO bungee
    // TODO send help message
    // TODO extension manager
    // TODO after ini set arena in MOTD
    // TODO if create/delete arena update MOTD + hub server
    // TODO make player leave because he can't join
    // TODO podium on end
    // detect if hub or play server
    // if hub server : get all arenas --> only command for arena sign
    // update sign with info from bungee
    // if game server : (onEnable) send all arena (name, maxPlayer), (onJoin, onLeave) : send info (numb player), (onGameChangeState) sendState
    //

    //https://www.spigotmc.org/threads/messaging-channel-with-no-players-online.393591/

    public String uid = "%%__USER__%%";
    public boolean sts = true;

    final int version;
    boolean hubServer;
    boolean firstPlayerJoined = true;
    boolean noteIsEnable;
    boolean partiesIsEnable;
    boolean placeholderIsEnable;
    boolean MCJukeboxIsEnable;
    boolean OpenAudioMcIsEnable;
    ScoreBoardUtils scoreBoardUtils;
    UtilsPrefix utilsPrefix;
    Debugger debug;
    InitializeArena initializeArena;
    ArenaGui arenaGui;
    ChoseSongGUI choseSongGUI;
    JoinArenaGui joinArenaGui;
    ArenaManager am;
    MusicManager musicManager;
    InventoryManager invManager;
    LeaderboardManager leaderboardManager;
    PlayerHiderManager playerHiderManager;
    TextToAddToCommandManager textToAddToCommandManager;
    CustomDanceFloorManager customDanceFloorManager;
    JustJoinedManager justJoinedManager;
    ExpansionManager expansionManager;
    CustomHeadUtils customHeadUtils;
    List<String> colourName = new ArrayList<>();
    ColourUtils colourUtils;
    static BPM pl;

    public BPM() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        version = Integer.valueOf(packet.replace(".", ",").split(",")[3].split("_")[1]);
        noteIsEnable = true;
        OpenAudioMcIsEnable = true;
        MCJukeboxIsEnable = true;
        partiesIsEnable = false;
        utilsPrefix = new UtilsPrefix(this);
        debug = new Debugger(this);
        initializeArena = new InitializeArena(this);
        arenaGui = new ArenaGui();
        choseSongGUI = new ChoseSongGUI(this);
        joinArenaGui = new JoinArenaGui(this);
        am = new ArenaManager();
        musicManager = new MusicManager();
        invManager = new InventoryManager();
        scoreBoardUtils = new ScoreBoardUtils();
        leaderboardManager = new LeaderboardManager();
        playerHiderManager = new PlayerHiderManager(this);
        textToAddToCommandManager = new TextToAddToCommandManager();
        customDanceFloorManager = new CustomDanceFloorManager();
        justJoinedManager = new JustJoinedManager();
        customHeadUtils = new CustomHeadUtils();
        colourUtils = new ColourUtils();
        colourName.addAll(Arrays.asList("red", "gold", "yellow", "green", "aqua", "blue", "purple", "white", "gray", "black"));
    }

    public void onEnable() {
        if (getConfig().getBoolean("useBungeeCord"))
            bungee();

        (BPM.pl = this).saveDefaultConfig();
        if (!new File(getDataFolder(), "Language.yml").exists()) {
            saveResource("Language.yml", false);
        }
        if (!new File(getDataFolder(), "Leaderboard.yml").exists()) {
            saveResource("Leaderboard.yml", false);
        }
        final File songsFolder = new File(getDataFolder(), "Songs");
        if (!songsFolder.exists()) {
            songsFolder.mkdirs();
        }

        final File customDanceFloorFolder = new File(getDataFolder(), "CustomDanceFloors");
        if (!customDanceFloorFolder.exists()) {
            customDanceFloorFolder.mkdirs();
        }

        if (!new File(getDataFolder(), "ColourLanguage.yml").exists()) {
            saveResource("ColourLanguage.yml", false);
        }

        if (!new File(getDataFolder() + File.separator + "Songs", "RealThing.nbs").exists()) {
            saveResource("Songs" + File.separator + "RealThing.nbs", false);
        }
        if (!new File(getDataFolder() + File.separator + "Songs", "nothing.nbs").exists()) {
            saveResource("Songs" + File.separator + "nothing.nbs", false);
        }
        if (!new File(getDataFolder(), "Leaderboard.yml").exists()) {
            saveResource("Leaderboard.yml", false);
        }

        // TODO if Multiverscore then wait for all world to load
        if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
            MVWorldManager worldManager = core.getMVWorldManager();
        }


        initializeCustomDanceFloor();
        initializeArenaNames();
        initializeArena.initializeArena();

        String version = getDescription().getVersion();

        // idk why 2
        if (version.split("\\.").length == 2) {
            if (getConfig().getDouble("LastVersion") != Double.valueOf(version)) {
                new UtilsConfig(this).update();
            }
        }

        registerEvents();

        registerCommand("blockparty", getConfig().getStringList("Aliases"),
                (CommandExecutor) new CommandManager(this), new TabCompletor(this));

        if (getConfig().getBoolean("useBungeeCord"))
            checkIfBungee();

        isPluginActivated();

        checkSongProvider();

        if (getServer().getPluginManager().getPlugin("Parties") != null && getServer().getPluginManager().getPlugin("Parties").isEnabled()) {
            partiesIsEnable = true;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholderIsEnable = true;
            expansionManager = new ExpansionManager(this);
            expansionManager.register();
        } else {
            debug.broadcastError("Could not load BlockParty you need to download PlaceholderAPI");
            debug.broadcastError("https://www.spigotmc.org/resources/placeholderapi.6245/");
            getServer().getPluginManager().disablePlugin(this);
        }

        initializeLeaderBoard();
        super.onEnable();
    }

    public void setUpHubServer() {
        for (Arena a : am.getArenas()) {
            a.urgentLeaveGame();
            // TODO a.getFile().deleteOnExit();
        }
        am.getArenas().clear();
    }

    private void bungee() {
        if (getConfig().getString("ServerName") == null) {
            getConfig().set("ServerName", "blockpartyserver");
            saveConfig();
        }

        onPluginMessageListener pluginMessageListener = new onPluginMessageListener(this);

        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", pluginMessageListener);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getMessenger().registerIncomingPluginChannel(this, "joschma:blockparty" + getConfig().getString("ServerName").toLowerCase(),
                pluginMessageListener);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "joschma:blockparty");
    }

    private void checkSongProvider() {
        for (Arena a : am.getArenas()) {
            if (a.getSongProvider() == SongProvider.NoteBlock) {
                if (!noteIsEnable) {
                    debug.broadcastError(" *** NoteBlockAPI is not installed or not enabled. ***");
                    debug.broadcastError("https://www.spigotmc.org/resources/noteblockapi.19287/");
                }
            } else if (a.getSongProvider() == SongProvider.MCJukebox) {
                if (!MCJukeboxIsEnable) {
                    debug.broadcastError(" *** MCJukebox is not installed or not enabled. ***");
                    debug.broadcastError("https://www.spigotmc.org/resources/mcjukebox.16024/");
                }
            } else if (a.getSongProvider() == SongProvider.OpenAudioMC) {
                if (!OpenAudioMcIsEnable) {
                    debug.broadcastError(" *** OpenAudio is not installed or not enabled. ***");
                    debug.broadcastError("https://www.spigotmc.org/resources/openaudiomc-proximity-voice-chat-and-music-without-mods.30691/");
                }
            }
        }
    }

    private void isPluginActivated() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
            placeholderIsEnable = false;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) {
            noteIsEnable = false;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("MCJukebox")) {
            MCJukeboxIsEnable = false;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("OpenAudioMc")) {
            OpenAudioMcIsEnable = false;
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new onServerListPingEvent(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerSwapHandItemsListener(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerLeave(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerPlaceBlock(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerTakeDamage(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerDropItem(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerCommand(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerBreakBlock(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerClickInventory(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerArmorStandManipulateListener(this), this);
        getServer().getPluginManager().registerEvents(new onSnowballLand(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoin(this), this);
    }

    private void checkIfBungee() {
        if (!getServer().spigot().getConfig().getConfigurationSection("settings").getBoolean("bungeecord")) {
            debug.broadcastError("You have set bungee setting to true");
            debug.broadcastError("But no bungeecord has been detected");
        }
    }

    private void initializeCustomDanceFloor() {
        final File folder = new File(getDataFolder() + File.separator + "CustomDanceFloors");
        final File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; ++i) {
                if (listOfFiles[i].isFile()) {
                    YamlConfiguration fcCustomDanceFloor = YamlConfiguration.loadConfiguration(listOfFiles[i]);
                    List<Material> mas = new ArrayList<Material>();
                    for (String materialName : fcCustomDanceFloor.getStringList("Material")) {
                        mas.add(Material.valueOf(materialName));
                    }

                    customDanceFloorManager.getCustomDanceFloor().put(listOfFiles[i].getName().replace(".yml", ""),
                            mas);
                }
            }
        }
    }

    private void initializeLeaderBoard() {
        if (!new File(getDataFolder(), "Leaderboard").exists()) {
            FileManager.createFile("Leaderboard");
        }
        File leaderboard = FileManager.load("Leaderboard");
        YamlConfiguration fc = FileManager.load(leaderboard);
        for (String uuid : fc.getConfigurationSection("").getKeys(false)) {
            String pName = fc.getString(uuid + ".name");
            int games = fc.getInt(uuid + ".game");
            pl.getLeaderboardManager().getGameMap().put(pName, games);
            int wins = fc.getInt(uuid + ".win");
            pl.getLeaderboardManager().getWinMap().put(pName, wins);
            pl.getLeaderboardManager().getLooseMap().put(pName, games - wins);
        }
    }

    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        for (final Arena a : am.getArenas()) {
            a.urgentLeaveGame();
        }

        for (final Arena a : am.getArenas()) {
            initializeArena.saveArena(a);
        }

        BPM.pl = null;
        super.onDisable();
    }

    public boolean isNumeric(String string) {
        if (string == null || string.equals("")) {
            return false;
        }
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void initializeArenaNames() {
        final File folder = new File(getDataFolder() + File.separator + "Arenas");
        final File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; ++i) {
                if (listOfFiles[i].isFile()) {
                    am.addArenaNames(listOfFiles[i].getName().replace(".yml", ""));
                }
            }
        }
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String msg(Player p, String message) {
        String msg = message;
        Arena a = BPM.getPl().getAm().getArenaPlayer(Bukkit.getPlayer(p.getUniqueId()));
        msg = ChatColor.translateAlternateColorCodes('&', msg);

        if (a == null) {
            return msg;
        }

        if (BPM.getPl().isPlaceholderIsEnable())
            return PlaceholderAPI.setPlaceholders(p, msg);
        return msg;
    }

    public void registerCommand(String cmd, List<String> aliases, CommandExecutor executor, TabCompletor tab) {
        PluginCommand plc = null;
        Class<?> cl = PluginCommand.class;
        Constructor<?> cons = null;
        try {
            cons = cl.getDeclaredConstructor(String.class, Plugin.class);
            cons.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
        }

        try {
            plc = (PluginCommand) cons.newInstance(cmd, this); // made the instance
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (plc == null) {
            debug.broadcastError("Problem when initialize commands!");
            return;
        }

        plc.setAliases(aliases);

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(cmd, plc); // Register on Bukkit's Map
            plc.register(commandMap); // Register Map on your Command
        } catch (Exception e) {
            e.printStackTrace();
        }

        plc.setPermission("BlockParty.Player");
        plc.setTabCompleter(tab);
        plc.setExecutor(executor); // Set executor
    }

    public void sendCustomData(Player player, String subChannel, String... datas) {
        // perform a check to see if globally are no players
        if (getServer().getOnlinePlayers() == null || getServer().getOnlinePlayers().isEmpty())
            return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subChannel); // the channel could be whatever you want

        for (String data : datas) {
            out.writeUTF(data);
        }

        // we send the data to the server
        player.getServer().sendPluginMessage(this, "joschma:blockparty", out.toByteArray());
    }

    public void sendBungeeCustomData(/*Player player,*/ String subChannel, String... datas) {
        // perform a check to see if globally are no players
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subChannel);

        for (String data : datas) {
            out.writeUTF(data);
        }

        // If you don't care about the player
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public PlayerHiderManager getPlayerHiderManager() {
        return playerHiderManager;
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    public TextToAddToCommandManager getTextToAddToCommandManager() {
        return textToAddToCommandManager;
    }

    public boolean isPlaceholderIsEnable() {
        return placeholderIsEnable;
    }

    public ChoseSongGUI getChoseSongGUI() {
        return choseSongGUI;
    }

    public boolean isPartiesIsEnable() {
        return partiesIsEnable;
    }

    public InventoryManager getInvManager() {
        return invManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public ArenaManager getAm() {
        return am;
    }

    public ArenaGui getArenaGui() {
        return arenaGui;
    }

    public InitializeArena getInitializeArena() {
        return initializeArena;
    }

    public boolean isNoteIsEnable() {
        return noteIsEnable;
    }

    public boolean isMCJukeboxIsEnable() {
        return MCJukeboxIsEnable;
    }

    public static BPM getPl() {
        return BPM.pl;
    }

    public Debugger getDebug() {
        return debug;
    }

    public UtilsPrefix getUtilsPrefix() {
        return utilsPrefix;
    }

    public ScoreBoardUtils getScoreBoardUtils() {
        return scoreBoardUtils;
    }

    public int getVersion() {
        return version;
    }

    public CustomDanceFloorManager getCustomDanceFloorManager() {
        return customDanceFloorManager;
    }

    public JoinArenaGui getJoinArenaGui() {
        return joinArenaGui;
    }

    public JustJoinedManager getJustJoinedManager() {
        return justJoinedManager;
    }

    public ExpansionManager getExpansionManager() {
        return expansionManager;
    }

    public CustomHeadUtils getCustomHeadUtils() {
        return customHeadUtils;
    }

    public List<String> getColourName() {
        return colourName;
    }

    public ColourUtils getColourUtils() {
        return colourUtils;
    }

    public boolean isFirstPlayerJoined() {
        return firstPlayerJoined;
    }

    public void setFirstPlayerJoined(boolean firstPlayerJoined) {
        this.firstPlayerJoined = firstPlayerJoined;
    }

    public boolean isHubServer() {
        return hubServer;
    }

    public void setHubServer(boolean hubServer) {
        this.hubServer = hubServer;
    }
}
