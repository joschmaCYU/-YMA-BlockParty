
package fr.joschma.BlockParty.Arena;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.craftmend.openaudiomc.api.ClientApi;
import com.craftmend.openaudiomc.api.MediaApi;
import com.cryptomorin.xseries.XMaterial;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import fr.joschma.BlockParty.Arena.End.End;
import fr.joschma.BlockParty.Arena.JoinArena.JoinArena;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.Arena.State.ParticleColourSetting;
import fr.joschma.BlockParty.Arena.State.SongProvider;
import fr.joschma.BlockParty.Arena.State.SongSetting;
import fr.joschma.BlockParty.Arena.Timer.*;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Cuboid.Cuboid;
import fr.joschma.BlockParty.Cuboid.DistributedFiller;
import fr.joschma.BlockParty.Cuboid.WorkloadRunnable;
import fr.joschma.BlockParty.Manager.FileManager;
import fr.joschma.BlockParty.Manager.SongManager;
import fr.joschma.BlockParty.Messages.Language;
import me.clip.placeholderapi.PlaceholderAPI;
import net.mcjukebox.plugin.bukkit.api.models.Media;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.util.*;

public class Arena {

    BPM pl;
    SongProvider songProvider;
    SongManager songManager;
    String name;
    String powerUpName;
    File file;
    ArenaState state;
    SongSetting songSetting;
    GameMode gmOnDeath;
    boolean powerUpAsBlock;
    boolean giveBlock;
    boolean giveDye;
    boolean resetExp;
    boolean giveSlimeBall;
    boolean clearInventory;
    boolean saveInventory;
    boolean finished;
    boolean playMusic;
    boolean playStopMusic;
    boolean playerColision;
    boolean changeTime;
    boolean allowPVP;
    boolean enableLightnings;
    boolean enableFireworksOnWin;
    boolean enableScoreboard;
    boolean autoRestart;
    boolean enablePowerUps;
    boolean allowJoinDuringGame;
    boolean onlyCustomFloors;
    boolean randomizeCustomFloor;
    boolean colourGroup;
    boolean soloGame;
    boolean nightVision;
    boolean noTitleBar;
    boolean showColorNameInBarTitle;
    boolean smartRandomJoin;
    boolean useSameColourGroup;
    boolean useParticles;
    Map<Integer, List<Material>> colourGroupMap = new HashMap<>();
    int maxPlayer;
    int minPlayer;
    int lobbyWaitTime;
    int appreciateTime;
    int regenerateBlockTime;
    int waitTimeBeforeGiveColorFirstRound;
    int waitTimeBeforeGiveColor;
    double timeToRemoveFromRemoveFloorTime;
    double removeFloorTime;
    double baseRemoveFloorTime;
    int round;
    int durationPowerUp;
    int levelOfPowerUp;
    double rangeToCatchPowerup;
    int YLevelToDie;
    int maxNumberOfRound;
    int particleCount;
    Double particleSize;
    Material danceFloorActualMaterial;
    Material powerUpHead;
    List<Material> danceFloorColourGroupActualMaterials;
    List<Material> danceFloorFloorMaterials;
    List<Integer> roundToSpawnPowerUp;
    List<Integer> roundToReduceTime;
    List<String> pathToMusic;
    String pathToStopMusic;
    Map<String, String> linkToMusic;
    String linkToStopMusic;
    List<Player> winners;
    List<Player> players;
    List<Player> playersAlive;
    List<Sign> signs;
    Location lobbySpawn;
    Location exitSpawn;
    Location powerUpLoc;
    Cuboid danceFloorCuboid;
    Map<Material, List<Location>> savedDanceFloorMaterialMap = new HashMap<Material, List<Location>>();
    Cuboid arenaCuboid;
    Song[] songs;
    Song stopSong;
    Song chosenSong;
    RadioSongPlayer radioSongPlayer;
    RadioSongPlayer radioStopSongPlayer;
    Media mcJukeboxSong;
    Media mcJukeboxStopSong;
    Map<String, String> openAudioMusic;
    String openAudioStopMusic;
    List<String> allowedCommands;
    List<String> commandsOnGameEnd;
    final StartGame startGame;
    final Round roundFile;
    final AppreciateTime appreciateTimeFile;
    final GiveTerracotaTimer giveTerracotaTimer;
    final RegenerateBlockTimer regenerateBlockTimer;
    final RemoveFloorCountDown removeFloorCountDown;
    final WaitLobbyTimer waitLobbyTimer;
    Map<String, Integer> numberOfVote;
    Map<Player, String> playerVote;
    final ItemStack pinkDye;
    final ItemStack limeDye;
    List<String> customDanceFloor;
    List<String> scoreBoard;
    List<String> powerUps;
    List<String> signDisplay;
    DistributedFiller distributedFiller;
    WorkloadRunnable workloadRunnable;
    double maxMillisSecondPerTickToGenerateArenaDanceFloor;
    Map<String, Integer> potionEffects;
    String winDanceFloor;
    String waitDanceFloor;
    GameMode gameModeOnLeave;
    Particle particle;
    Color defaultParticleColour;
    ParticleColourSetting particleColourSetting;
    String customHeadLink;
    boolean useCustomHeadLink;
    String suffix_colour;
    List<Material> excludedDanceFloorBlocks = new ArrayList<>();
    int VolumeNoteBlock;

    public Arena(final BPM pl, final String name, final String powerUpName, final File file, final ArenaState state,
                 final SongSetting songSetting, final boolean finished, final int maxPlayer, final int minPlayer, final int lobbyWaitTime,
                 final int appreciateTime, final int regenerateBlockTime, final int waitTimeBeforeGiveColorFirstRound,
                 final int waitTimeBeforeGiveColor, final double timeToRemoveFromRemoveFloorTime, final double removeFloorTime,
                 final double baseRemoveFloorTime, final int round, final int durationPowerUp, final int levelOfPowerUp,
                 final double rangeToCatchPowerup, final int yLevelToDie, final int maxNumberOfRound, final Material powerUpHead,
                 final List<Material> danceFloorFloorMaterials, final List<Integer> roundToSpawnPowerUp, final List<Integer> roundToReduceTime,
                 final List<String> pathToMusic, final Map<String, String> linkToMusic, final String linkToStopMusic, final List<Sign> signs,
                 final String pathToStopMusic, final Location lobbySpawn, final Location exitSpawn, final Cuboid danceFloorCuboid,
                 final Cuboid arenaCuboid, final List<String> allowedCommands, final List<String> commandsOnGameEnd, boolean playerColision,
                 boolean changeTime, GameMode gmOnDeath, boolean saveInventory, SongProvider songProvider, boolean giveSlimeBall,
                 boolean clearInventory, boolean giveDye,boolean resetExp, boolean giveBlock, boolean allowPVP, boolean autoRestart,
                 boolean enableLightnings, boolean enablePowerUps, boolean enableFireworksOnWin, boolean allowJoinDuringGame,
                 boolean enableScoreboard, List<String> customDanceFloor, boolean randomizeCustomFloor,
                 boolean onlyCustomFloors, boolean colourGroup, boolean isNightVision,
                 Map<String, String> openAudioMusic, String openAudioStopMusic, boolean noTitleBar,
                 boolean showColorNameInBarTitle, boolean smartRandomJoin, List<String> scoreBoard, List<String> powerUps,
                 double maxMillisSecondPerTickToGenerateArenaDanceFloor, List<String> signDisplay,
                 Map<String, Integer> potionEffects, String winDanceFloor, String waitDanceFloor,
                 boolean powerUpAsBlock, GameMode gameModeOnLeave, boolean useSameColourGroup, boolean useParticles,
                 int particleCount, Double particleSize, Particle particle, Color defaultParticleColour,
                 ParticleColourSetting particleColourSetting, String customHeadLink, boolean useCustomHeadLink,
                 String suffix_colour, List<Material> excludedDanceFloorBlocks, int VolumeNoteBlock) {
        this.VolumeNoteBlock = VolumeNoteBlock;
        this.excludedDanceFloorBlocks = excludedDanceFloorBlocks;
        this.suffix_colour = suffix_colour;
        this.useCustomHeadLink = useCustomHeadLink;
        this.customHeadLink = customHeadLink;
        this.particleColourSetting = particleColourSetting;
        this.defaultParticleColour = defaultParticleColour;
        this.particle = particle;
        this.particleSize = particleSize;
        this.particleCount = particleCount;
        this.useParticles = useParticles;
        this.gameModeOnLeave = gameModeOnLeave;
        this.powerUpAsBlock = powerUpAsBlock;
        this.winDanceFloor = winDanceFloor;
        this.waitDanceFloor = waitDanceFloor;
        this.potionEffects = potionEffects;
        this.signDisplay = signDisplay;
        this.powerUps = powerUps;
        this.scoreBoard = scoreBoard;
        this.smartRandomJoin = smartRandomJoin;
        this.showColorNameInBarTitle = showColorNameInBarTitle;
        this.noTitleBar = noTitleBar;
        this.openAudioMusic = openAudioMusic;
        this.openAudioStopMusic = openAudioStopMusic;
        this.danceFloorColourGroupActualMaterials = new ArrayList<>();
        this.danceFloorFloorMaterials = new ArrayList<Material>();
        this.roundToSpawnPowerUp = new ArrayList<Integer>();
        this.roundToReduceTime = new ArrayList<Integer>();
        this.pathToMusic = new ArrayList<String>();
        this.winners = new ArrayList<Player>();
        this.players = new ArrayList<Player>();
        this.playersAlive = new ArrayList<Player>();
        this.signs = new ArrayList<Sign>();
        this.appreciateTimeFile = new AppreciateTime();
        this.giveTerracotaTimer = new GiveTerracotaTimer();
        this.regenerateBlockTimer = new RegenerateBlockTimer();
        this.removeFloorCountDown = new RemoveFloorCountDown();
        this.songManager = new SongManager(this);
        this.pl = pl;
        this.roundFile = new Round(pl);
        this.startGame = new StartGame(pl);
        this.waitLobbyTimer = new WaitLobbyTimer(this);
        this.name = name;
        this.powerUpName = powerUpName;
        this.file = file;
        this.state = state;
        this.songSetting = songSetting;
        this.finished = finished;
        this.maxPlayer = maxPlayer;
        this.minPlayer = minPlayer;
        this.lobbyWaitTime = lobbyWaitTime;
        this.appreciateTime = appreciateTime;
        this.regenerateBlockTime = regenerateBlockTime;
        this.waitTimeBeforeGiveColorFirstRound = waitTimeBeforeGiveColorFirstRound;
        this.waitTimeBeforeGiveColor = waitTimeBeforeGiveColor;
        this.timeToRemoveFromRemoveFloorTime = timeToRemoveFromRemoveFloorTime;
        this.removeFloorTime = removeFloorTime;
        this.baseRemoveFloorTime = baseRemoveFloorTime;
        this.round = round;
        this.durationPowerUp = durationPowerUp;
        this.levelOfPowerUp = levelOfPowerUp;
        this.rangeToCatchPowerup = rangeToCatchPowerup;
        this.YLevelToDie = yLevelToDie;
        this.maxNumberOfRound = maxNumberOfRound;
        this.powerUpHead = powerUpHead;
        this.signs = signs;
        this.danceFloorFloorMaterials = danceFloorFloorMaterials;
        this.roundToSpawnPowerUp = roundToSpawnPowerUp;
        this.roundToReduceTime = roundToReduceTime;
        this.pathToMusic = pathToMusic;
        this.pathToStopMusic = pathToStopMusic;
        this.lobbySpawn = lobbySpawn;
        this.exitSpawn = exitSpawn;
        this.danceFloorCuboid = danceFloorCuboid;
        this.arenaCuboid = arenaCuboid;
        this.allowedCommands = allowedCommands;
        this.commandsOnGameEnd = commandsOnGameEnd;
        this.playerColision = playerColision;
        this.linkToMusic = linkToMusic;
        this.linkToStopMusic = linkToStopMusic;
        this.changeTime = changeTime;
        this.gmOnDeath = gmOnDeath;
        this.saveInventory = saveInventory;
        this.songProvider = songProvider;
        this.giveSlimeBall = giveSlimeBall;
        this.giveDye = giveDye;
        this.resetExp = resetExp;
        this.clearInventory = clearInventory;
        this.giveBlock = giveBlock;
        this.allowPVP = allowPVP;
        this.autoRestart = autoRestart;
        this.enableLightnings = enableLightnings;
        this.enablePowerUps = enablePowerUps;
        this.enableFireworksOnWin = enableFireworksOnWin;
        this.allowJoinDuringGame = allowJoinDuringGame;
        this.enableScoreboard = enableScoreboard;
        this.customDanceFloor = customDanceFloor;
        this.onlyCustomFloors = onlyCustomFloors;
        this.randomizeCustomFloor = randomizeCustomFloor;
        this.colourGroup = colourGroup;
        this.nightVision = isNightVision;
        this.useSameColourGroup = useSameColourGroup;

        this.maxMillisSecondPerTickToGenerateArenaDanceFloor = maxMillisSecondPerTickToGenerateArenaDanceFloor;
        this.distributedFiller = new DistributedFiller();
        this.workloadRunnable = new WorkloadRunnable(this);
        Bukkit.getScheduler().runTaskTimer(pl, workloadRunnable, 1, 1);

        playerVote = songManager.getPlayerVote();
        numberOfVote = songManager.getNumberOfVoteMap();

        pinkDye = new ItemStack(Material.PINK_DYE);
        limeDye = new ItemStack(Material.LIME_DYE);
    }

    int taskID = 0;
    public void findASetDanceFloorActualMaterial() {
        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
            @Override
            public void run() {
                if (workloadRunnable.workloadDeque.poll() == null) {
                    Material ma = danceFloorCuboid.getRandomLocation().getBlock().getType();
                    int i = 0;

                    while (ma == Material.AIR || excludedDanceFloorBlocks.contains(ma)) {
                        if (i == danceFloorCuboid.getTotalBlockSize()) {
                            pl.getDebug().broadcastError("No block to give found!");
                            clear();
                            break;
                        }

                        ma = danceFloorCuboid.getRandomLocation().getBlock().getType();
                        i++;
                    }

                    setDanceFloorActualMaterial(ma);
                    Bukkit.getScheduler().cancelTask(taskID);
                }
            }
        }, 0, 5);
    }

    public String msg(Player p, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (BPM.getPl().isPlaceholderIsEnable())
            return PlaceholderAPI.setPlaceholders(p, message);
        return message;
    }

    public String msg(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public void updateSign() {
        if (this.state != ArenaState.INGAME) {
            for (final Sign sign : signs) {
                int y = 0;

                for (String str : signDisplay) {
                    if (str.contains("%")) {
                        String[] sentence = str.split("%");

                        for (String stri : sentence) {
                            String replaced = pl.getExpansionManager().arenaPlaceHolder(stri, this);
                            if (replaced != null) {
                                str = str.replace("%" + stri + "%", replaced);
                            }
                        }

                        sign.setLine(y, msg(str));
                    }

                    if (players.size() > 0)
                        sign.setLine(y, msg(players.get(0), str));
                    y++;
                }

                sign.update();
            }
        } else {
            for (final Sign sign : signs) {
                sign.setLine(2, ChatColor.RED + "In game !");
                sign.update();
            }
        }
    }

    public void clear() {
        File leaderboard = FileManager.load("Leaderboard");
        YamlConfiguration fc = FileManager.load(leaderboard);
        savedDanceFloorMaterialMap.clear();
        playMusic = false;
        playStopMusic = false;
        soloGame = false;
        state = ArenaState.CLEARED;
        pl.getMusicManager().stopMusic(this);
        for (final Player p : players) {
            p.setGameMode(GameMode.SURVIVAL);
            if (clearInventory)
                p.getInventory().clear();
            clearPotionEffect(p);
            if (isEnableScoreboard())
                pl.getScoreBoardUtils().rmvScoreBoard(p);
            //p.teleport(exitSpawn);

            if (isSaveInventory())
                pl.getInvManager().loadInventory(p);

            p.teleport(exitSpawn);
            if (playerVote.keySet().contains(p)) {
                String lastVotedSong = playerVote.get(p);
                numberOfVote.put(lastVotedSong, numberOfVote.get(lastVotedSong) - 1);
                playerVote.remove(p);
                songManager.setPlayerSongMap(numberOfVote);
                songManager.setPlayerVote(playerVote);
            }

            int games = fc.getInt(p.getUniqueId() + ".game") + 1;
            fc.set(p.getUniqueId() + ".game", games);
            fc.set(p.getUniqueId() + ".name", p.getName());
            pl.getLeaderboardManager().getGameMap().put(p.getName(), games);
            pl.getLeaderboardManager().getLooseMap().put(p.getName(), games - fc.getInt(p.getUniqueId() + ".win"));
        }

        pl.getMusicManager().stopMusic(this);

        final List<Player> losers = new ArrayList<Player>();
        losers.addAll(players);
        losers.removeAll(winners);

        for (Player p : players) {
            for (String str : commandsOnGameEnd) {
                if (pl.isPlaceholderIsEnable()) {
                    str = PlaceholderAPI.setPlaceholders(p, str);
                }

                if (str.contains("%winners%")) {
                    if (winners.contains(p)) {
                        str = str.replace("%winners%", p.getName());
                    } else {
                        str = "";
                    }
                }

                if (str.contains("%losers%")) {
                    if (losers.contains(p)) {
                        str = str.replace("%losers%", p.getName());
                    } else {
                        str = "";
                    }
                }

                Bukkit.dispatchCommand((CommandSender) Bukkit.getServer().getConsoleSender(), str);
            }
        }

        this.playersAlive = new ArrayList<Player>();
        this.danceFloorActualMaterial = null;
        danceFloorColourGroupActualMaterials = new ArrayList<>();//.clear()

        for (Player winner : winners) {
            int wins = fc.getInt(winner.getUniqueId() + ".win") + 1;
            fc.set(winner.getUniqueId() + ".win", wins);
            pl.getLeaderboardManager().getWinMap().put(winner.getName(), wins);
            pl.getLeaderboardManager().getLooseMap().put(winner.getName(), fc.getInt(winner.getUniqueId() + ".game") - wins);
        }
        FileManager.save(leaderboard, fc);


        this.winners = new ArrayList<Player>();

        clearPowerUps();

        List<Player> rejoin = new ArrayList<>();

        if (isAutoRestart()) {
            rejoin.addAll(players);
        }

        this.players.clear();
        this.updateSign();

        if (isAutoRestart()) {
            if (arenaCuboid.getPoint1().getWorld().getUID() != exitSpawn.getWorld().getUID()) {
                Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
                    @Override
                    public void run() {
                        for (Player p : rejoin)
                            p.performCommand("blockparty:blockparty join " + name);
                    }
                }, 10L);
            } else {
                for (Player p : rejoin)
                    p.performCommand("blockparty:blockparty join " + name);
            }

            rejoin.clear();
        }

        if (!waitDanceFloor.equals("none")) {
            // load wait dance floor
            List<Block> bls = danceFloorCuboid.blockList();
            List<Material> customBls = pl.getCustomDanceFloorManager().getCustomDanceFloor().get(waitDanceFloor);

            if (bls.size() != customBls.size()) {
                pl.getDebug().broadcastError("Wait dance floor and the arena dance floor are not the same size");
                pl.getDebug().broadcastError("Could not place wait dance floor");
            } else {
                for (int i = 0; i < customBls.size(); i++) {
                    distributedFiller.fillLocation(bls.get(i).getLocation(), customBls.get(i), workloadRunnable);
                }
            }
        }
    }

    public void clearPowerUps() {
        if (powerUpLoc != null) {
            for (final Entity en : powerUpLoc.getWorld().getNearbyEntities(powerUpLoc, rangeToCatchPowerup,
                    rangeToCatchPowerup, rangeToCatchPowerup)) {
                if (en.getType() == EntityType.ARMOR_STAND) {
                    en.remove();
                } else if (isPowerUpAsBlock()) {
                    powerUpLoc.getBlock().setType(Material.AIR);
                }

                powerUpLoc = null;
            }
        }
    }

    public void urgentLeaveGame() {
        giveTerracotaTimer.stopTimer();
        regenerateBlockTimer.stopTimer();
        removeFloorCountDown.stopTimer();
        waitLobbyTimer.stopTimer(this);
        appreciateTimeFile.stopTimer();

        for (final Player p : players) {
            p.setGameMode(gameModeOnLeave);
            if (clearInventory)
                p.getInventory().clear();
            clearPotionEffect(p);
            if (isSaveInventory())
                pl.getInvManager().loadInventory(p);
            if (isEnableScoreboard())
                pl.getScoreBoardUtils().rmvScoreBoard(p);
            p.teleport(exitSpawn);
            if (radioSongPlayer != null) {
                radioSongPlayer.removePlayer(p);
                if (radioStopSongPlayer == null) {
                    continue;
                }
                radioStopSongPlayer.removePlayer(p);
            }
        }
        if (powerUpLoc != null) {
            for (final Entity en : powerUpLoc.getWorld().getNearbyEntities(powerUpLoc, 0.1, 0.1, 0.1)) {
                if (en.getType() == EntityType.ARMOR_STAND) {
                    en.remove();
                }
            }
        }

        pl.getMusicManager().stopMusic(this);
        pl.getMusicManager().stopStopMusic(this);
        state = ArenaState.CLEARED;
        playersAlive.clear();
        players.clear();
        winners.clear();
        updateSign();
    }

    public void leaveGame(final Player p) {
        for (Player pla : players) {
            if (p != pla)
                pl.getDebug().msg(pla, Language.MSG.LeaveGame.msg(p));
        }
        players.remove(p);

        if (playerVote.keySet().contains(p)) {
            String lastVotedSong = playerVote.get(p);
            numberOfVote.put(lastVotedSong, numberOfVote.get(lastVotedSong) - 1);
            playerVote.remove(p);
            songManager.setPlayerSongMap(numberOfVote);
            songManager.setPlayerVote(playerVote);
        }

        if (state == ArenaState.INGAME) {
            if (playersAlive.contains(p)) {
                playersAlive.remove(p);
                checkWin();
            }
        } else if (state == ArenaState.WATTING && this.players.size() < this.minPlayer) {
            waitLobbyTimer.stopTimer(this);
            state = ArenaState.CLEARED;
        }

        p.setGameMode(gameModeOnLeave);

        if (songProvider == SongProvider.NoteBlock) {
            if (radioSongPlayer != null) {
                radioSongPlayer.removePlayer(p);
            }
        }

        if (songProvider == SongProvider.MCJukebox)
            pl.getMusicManager().stopMcJukeboxMusic(p);


        if (songProvider == SongProvider.OpenAudioMC) {
            MediaApi.getInstance().stopFor("BlockParty", ClientApi.getInstance().getClient(p.getUniqueId()));
            MediaApi.getInstance().stopFor("BlockPartyStop", ClientApi.getInstance().getClient(p.getUniqueId()));
        }

        if (clearInventory)
            p.getInventory().clear();
        for (Player players : Bukkit.getOnlinePlayers()) {
            p.showPlayer(players);
        }
        if (pl.getPlayerHiderManager().getHidden() != null) {
            pl.getPlayerHiderManager().getHidden().remove(p.getUniqueId());
        }

        clearPotionEffect(p);
        if (isSaveInventory())
            pl.getInvManager().loadInventory(p);
        if (isEnableScoreboard())
            pl.getScoreBoardUtils().rmvScoreBoard(p);
        p.teleport(this.exitSpawn);
        if (winners.contains(p)) {
            winners.remove(p);
        }
        updateSign();
    }

    public boolean joinParty(final Player p) {
        if (this.pl.isPartiesIsEnable()) {
            final PartiesAPI api = Parties.getApi();
            final PartyPlayer player = api.getPartyPlayer(p.getUniqueId());
            if (!player.isInParty()) {
                JoinArena.joinArena(p, this);
                return true;
            }
            final Party party = api.getParty(player.getPartyId());
            if (party != null) {
                if (party.getMembers().size() + players.size() <= maxPlayer) {
                    for (final UUID uuid : party.getMembers()) {
                        JoinArena.joinArena(Bukkit.getPlayer(uuid), this);
                    }
                    return true;
                }
                pl.getDebug().error(p, Language.MSG.youAreToManyToJoinInYourSquad.msg(p));
            }
        }
        return false;
    }

    public boolean checkWin() {
        if (this.playersAlive.size() == 1 && !soloGame) {
            End.playerWin(this, playersAlive.get(0));
            return true;
        } else if (maxNumberOfRound == round || this.playersAlive.size() == 0) {
            End.noMoreRoundWin(this);
            return true;
        }
        return false;
    }

    public void clearPotionEffect(final Player p) {
        for (final PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
    }

    public void giveStuff(final Player p) {
        if (giveSlimeBall) {
            final ItemStack quit = new ItemStack(XMaterial.SLIME_BALL.parseMaterial());
            final ItemMeta quitM = quit.getItemMeta();
            quitM.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + Language.MSG.LeaveSlimeBall.msg());
            quit.setItemMeta(quitM);
            p.getInventory().setItem(8, quit);
        }

        if (giveDye) {
            final ItemMeta pinkDyeMeta = pinkDye.getItemMeta();
            pinkDyeMeta.setDisplayName(ChatColor.GRAY + Language.MSG.PlayerHiderText.msg() + " \u27AF " + ChatColor.RED + Language.MSG.PlayerHiderVisible.msg());
            pinkDye.setItemMeta(pinkDyeMeta);

            final ItemMeta limeDyeMeta = limeDye.getItemMeta();
            limeDyeMeta.setDisplayName(ChatColor.GRAY + Language.MSG.PlayerHiderText.msg() + " \u27AF " + ChatColor.GREEN + Language.MSG.PlayerHiderHidden.msg());
            limeDye.setItemMeta(limeDyeMeta);
            p.getInventory().setItem(7, limeDye);
        }

        if (songSetting == SongSetting.CHOOSE)
            p.getInventory().setItem(0, pl.getArenaGui().createIT(XMaterial.PAPER.parseMaterial(), ChatColor.GRAY + Language.MSG.SongSelector.msg()));
    }

    public boolean iscolourGroup() {
        return colourGroup;
    }

    public Map<Integer, List<Material>> getColourGroupMap() {
        return colourGroupMap;
    }

    public boolean isClearInventory() {
        return clearInventory;
    }

    public boolean isGiveSlimeBall() {
        return giveSlimeBall;
    }

    public SongProvider getSongProvider() {
        return songProvider;
    }

    public void setSongProvider(SongProvider songProvider) {
        this.songProvider = songProvider;
    }

    public SongManager getSongManager() {
        return songManager;
    }

    public Song getChosenSong() {
        return chosenSong;
    }

    public void setChosenSong(Song chosenSong) {
        this.chosenSong = chosenSong;
    }

    public void setSongSetting(SongSetting songSetting) {
        this.songSetting = songSetting;
    }

    public SongSetting getSongSetting() {
        return songSetting;
    }

    public void setWinners(final List<Player> winners) {
        this.winners = winners;
    }

    public boolean isPlayMusic() {
        return this.playMusic;
    }

    public void setPlayMusic(final boolean playMusic) {
        this.playMusic = playMusic;
    }

    public boolean isPlayStopMusic() {
        return this.playStopMusic;
    }

    public void setPlayStopMusic(final boolean playStopMusic) {
        this.playStopMusic = playStopMusic;
    }

    public List<String> getCommandsOnGameEnd() {
        return this.commandsOnGameEnd;
    }

    public AppreciateTime getAppreciateTimeFile() {
        return this.appreciateTimeFile;
    }

    public GiveTerracotaTimer getGiveTerracotaTimer() {
        return this.giveTerracotaTimer;
    }

    public RegenerateBlockTimer getRegenerateBlockTimer() {
        return this.regenerateBlockTimer;
    }

    public RemoveFloorCountDown getRemoveFloorCountDown() {
        return this.removeFloorCountDown;
    }

    public WaitLobbyTimer getWaitLobbyTimer() {
        return this.waitLobbyTimer;
    }

    public Round getRoundFile() {
        return this.roundFile;
    }

    public BPM getPl() {
        return this.pl;
    }

    public void setPl(final BPM pl) {
        this.pl = pl;
    }

    public StartGame getStartGame() {
        return this.startGame;
    }

    public RadioSongPlayer getRadioSongPlayer() {
        return this.radioSongPlayer;
    }

    public void setRadioSongPlayer(final RadioSongPlayer radioSongPlayer) {
        this.radioSongPlayer = radioSongPlayer;
    }

    public Song[] getSongs() {
        return this.songs;
    }

    public void setSongs(final Song[] songs) {
        this.songs = songs;
    }

    public Song getStopSong() {
        return this.stopSong;
    }

    public void setStopSong(final Song stopSong) {
        this.stopSong = stopSong;
    }

    public ArenaState getState() {
        return this.state;
    }

    public void setState(final ArenaState state) {
        this.state = state;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPowerUpName() {
        return this.powerUpName;
    }

    public void setPowerUpName(final String powerUpName) {
        this.powerUpName = powerUpName;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public boolean getFinished() {
        return this.finished;
    }

    public void setFinished(final boolean finished) {
        this.finished = finished;
    }

    public int getMaxPlayer() {
        return this.maxPlayer;
    }

    public void setMaxPlayer(final int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public int getMinPlayer() {
        return this.minPlayer;
    }

    public void setMinPlayer(final int minPlayer) {
        this.minPlayer = minPlayer;
    }

    public int getLobbyWaitTime() {
        return this.lobbyWaitTime;
    }

    public void setLobbyWaitTime(final int lobbyWaitTime) {
        this.lobbyWaitTime = lobbyWaitTime;
    }

    public int getAppreciateTime() {
        return this.appreciateTime;
    }

    public void setAppreciateTime(final int appreciateTime) {
        this.appreciateTime = appreciateTime;
    }

    public double getSecondsToRemoveFromRemoveFloorTime() {
        return this.timeToRemoveFromRemoveFloorTime;
    }

    public void setSecondsToRemoveFromRemoveFloorTime(final double secondsToRemoveFromRemoveFloorTime) {
        this.timeToRemoveFromRemoveFloorTime = secondsToRemoveFromRemoveFloorTime;
    }

    public double getBaseRemoveFloorTime() {
        return this.baseRemoveFloorTime;
    }

    public void setBaseRemoveFloorTime(final double baseRoundTime) {
        this.baseRemoveFloorTime = baseRoundTime;
    }

    public int getRegenerateBlockTime() {
        return this.regenerateBlockTime;
    }

    public void setRegenerateBlockTime(final int regenerateBlockTime) {
        this.regenerateBlockTime = regenerateBlockTime;
    }

    public double getRemoveFloorTime() {
        return this.removeFloorTime;
    }

    public void setRemoveFloorTime(final double d) {
        this.removeFloorTime = d;
    }

    public int getWaitTimeBeforeGiveColorFirstRound() {
        return this.waitTimeBeforeGiveColorFirstRound;
    }

    public void setWaitTimeBeforeGiveColorFirstRound(final int waitTimeBeforeGiveColorFirstRound) {
        this.waitTimeBeforeGiveColorFirstRound = waitTimeBeforeGiveColorFirstRound;
    }

    public int getWaitTimeBeforeGiveColor() {
        return this.waitTimeBeforeGiveColor;
    }

    public void setWaitTimeBeforeGiveColor(final int waitTimeBeforeGiveColor) {
        this.waitTimeBeforeGiveColor = waitTimeBeforeGiveColor;
    }

    public boolean isPlayerColision() {
        return playerColision;
    }

    public int getDurationPowerUp() {
        return this.durationPowerUp;
    }

    public void setDurationPowerUp(final int durationPowerUp) {
        this.durationPowerUp = durationPowerUp;
    }

    public int getLevelOfPowerUp() {
        return this.levelOfPowerUp;
    }

    public void setLevelOfPowerUp(final int levelOfPowerUp) {
        this.levelOfPowerUp = levelOfPowerUp;
    }

    public double getRangeToCatchPowerup() {
        return this.rangeToCatchPowerup;
    }

    public void setRangeToCatchPowerup(final double rangeToCatchPowerup) {
        this.rangeToCatchPowerup = rangeToCatchPowerup;
    }

    public int getYLevelToDie() {
        return this.YLevelToDie;
    }

    public void setYLevelToDie(final int yLevelToDie) {
        this.YLevelToDie = yLevelToDie;
    }

    public int getMaxNumberOfRound() {
        return this.maxNumberOfRound;
    }

    public void setMaxNumberOfRound(final int maxNumberOfRound) {
        this.maxNumberOfRound = maxNumberOfRound;
    }

    public int getRound() {
        return this.round;
    }

    public void setRound(final int round) {
        this.round = round;
    }

    public Material getDanceFloorActualMaterial() {
        return this.danceFloorActualMaterial;
    }

    public void setDanceFloorActualMaterial(final Material danceFloorActualMaterial) {
        this.danceFloorActualMaterial = danceFloorActualMaterial;
    }

    public Material getPowerUpHead() {
        return this.powerUpHead;
    }

    public void setPowerUpHead(final Material powerUpHead) {
        this.powerUpHead = powerUpHead;
    }

    public List<Material> getDanceFloorFloorMaterials() {
        if (colourGroup) {
            if (colourGroupMap.containsKey(round)) {
                return colourGroupMap.get(round);
            }
        }

        return this.danceFloorFloorMaterials;
    }

    public void setDanceFloorFloorMaterials(final List<Material> danceFloorFloorMaterials) {
        this.danceFloorFloorMaterials = danceFloorFloorMaterials;
    }

    public List<Integer> getRoundToReduceTime() {
        return this.roundToReduceTime;
    }

    public void setRoundToReduceTime(final List<Integer> roundToReduceTime) {
        this.roundToReduceTime = roundToReduceTime;
    }

    public List<Integer> getRoundToSpawnPowerUp() {
        return this.roundToSpawnPowerUp;
    }

    public void setRoundToSpawnPowerUp(final List<Integer> roundToSpawnPowerUp) {
        this.roundToSpawnPowerUp = roundToSpawnPowerUp;
    }

    public List<String> getPathToMusic() {
        return this.pathToMusic;
    }

    public void setPathToMusic(final List<String> pathToMusic) {
        this.pathToMusic = pathToMusic;
    }

    public String getPathToStopMusic() {
        return this.pathToStopMusic;
    }

    public void setPathToStopMusic(final String stopMusic) {
        this.pathToStopMusic = stopMusic;
    }

    public List<Player> getWinners() {
        return this.winners;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void addPlayer(final Player p) {
        this.players.add(p);
    }

    public void setPlayers(final List<Player> players) {
        this.players = players;
    }

    public Map<String, String> getLinkToMusic() {
        return linkToMusic;
    }

    public Media getMcJukeboxSong() {
        return mcJukeboxSong;
    }

    public Media getMcJukeboxStopSong() {
        return mcJukeboxStopSong;
    }

    public String getLinkToStopMusic() {
        return linkToStopMusic;
    }

    public void setMcJukeboxSong(Media mcJukeboxSong) {
        this.mcJukeboxSong = mcJukeboxSong;
    }

    public void setMcJukeboxStopSong(Media mcJukeboxStopSong) {
        this.mcJukeboxStopSong = mcJukeboxStopSong;
    }

    public void rmvAlivePlayer(final Player p) {
        this.playersAlive.remove(p);
    }

    public List<Player> getPlayersAlive() {
        return this.playersAlive;
    }

    public void addAllPlayersAlive(final List<Player> playersAlive) {
        playersAlive.addAll(playersAlive);
    }

    public Location getLobbySpawn() {
        return this.lobbySpawn;
    }

    public void setLobbySpawn(final Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Location getPowerUpLoc() {
        return this.powerUpLoc;
    }

    public void setPowerUpLoc(final Location powerUpLoc) {
        this.powerUpLoc = powerUpLoc;
    }

    public Cuboid getDanceFloorCuboid() {
        return this.danceFloorCuboid;
    }

    public void setDanceFloorCuboid(final Cuboid danceFloorCuboid) {
        this.danceFloorCuboid = danceFloorCuboid;
    }

    public List<Sign> getSigns() {
        return this.signs;
    }

    public void setSigns(final List<Sign> signs) {
        this.signs = signs;
    }

    public Location getExitSpawn() {
        return this.exitSpawn;
    }

    public void setExitSpawn(final Location exitSpawn) {
        this.exitSpawn = exitSpawn;
    }

    public RadioSongPlayer getRadioStopSongPlayer() {
        return this.radioStopSongPlayer;
    }

    public void setRadioStopSongPlayer(final RadioSongPlayer radioStopSongPlayer) {
        this.radioStopSongPlayer = radioStopSongPlayer;
    }

    public Cuboid getArenaCuboid() {
        return this.arenaCuboid;
    }

    public void setArenaCuboid(final Cuboid arenaCuboid) {
        this.arenaCuboid = arenaCuboid;
    }

    public List<String> getAllowedCommands() {
        return this.allowedCommands;
    }

    public void setAllowedCommands(final List<String> allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    public boolean isChangeTime() {
        return changeTime;
    }

    public void setChangeTime(boolean changeTime) {
        this.changeTime = changeTime;
    }

    public GameMode getGmOnDeath() {
        return gmOnDeath;
    }

    public void setGmOnDeath(GameMode gmOnDeath) {
        this.gmOnDeath = gmOnDeath;
    }

    public boolean isSaveInventory() {
        return saveInventory;
    }

    public void setSaveInventory(boolean saveInventory) {
        this.saveInventory = saveInventory;
    }

    public boolean isGiveDye() {
        return giveDye;
    }

    public boolean isResetExp() {
        return resetExp;
    }

    public boolean isGiveBlock() {
        return giveBlock;
    }

    public ItemStack getLimeDye() {
        return limeDye;
    }

    public ItemStack getPinkDye() {
        return pinkDye;
    }

    public boolean isAllowJoinDuringGame() {
        return allowJoinDuringGame;
    }

    public boolean isAllowPVP() {
        return allowPVP;
    }

    public boolean isAutoRestart() {
        return autoRestart;
    }

    public boolean isEnableFireworksOnWin() {
        return enableFireworksOnWin;
    }

    public boolean isEnableLightnings() {
        return enableLightnings;
    }

    public boolean isEnablePowerUps() {
        return enablePowerUps;
    }

    public boolean isEnableScoreboard() {
        return enableScoreboard;
    }

    public List<String> getCustomDanceFloor() {
        return customDanceFloor;
    }

    public void setCustomDanceFloor(List<String> customDanceFloor) {
        this.customDanceFloor = customDanceFloor;
    }

    public Map<Material, List<Location>> getSavedDanceFloorMaterialMap() {
        return savedDanceFloorMaterialMap;
    }

    public void setSavedDanceFloorMaterialMap(Map<Material, List<Location>> savedDanceFloorMaterialMap) {
        this.savedDanceFloorMaterialMap = savedDanceFloorMaterialMap;
    }

    public boolean isOnlyCustomFloors() {
        if (customDanceFloor.size() == 0 && onlyCustomFloors)
            pl.getDebug().broadcastError("There are no custom dance floor even though OnlyCustomFloors setting is set to true");
        return onlyCustomFloors;
    }

    public boolean isRandomizeCustomFloor() {
        return randomizeCustomFloor;
    }

    public boolean isSoloGame() {
        return soloGame;
    }

    public void setSoloGame(boolean soloGame) {
        this.soloGame = soloGame;
    }

    public boolean isNightVision() {
        return nightVision;
    }

    public Map<String, String> getOpenAudioMusic() {
        return openAudioMusic;
    }

    public void setOpenAudioMusic(Map<String, String> openAudioMusic) {
        this.openAudioMusic = openAudioMusic;
    }

    public String getOpenAudioStopMusic() {
        return openAudioStopMusic;
    }

    public void setOpenAudioStopMusic(String openAudioStopMusic) {
        this.openAudioStopMusic = openAudioStopMusic;
    }

    public boolean isNoTitleBar() {
        return noTitleBar;
    }

    public void setNoTitleBar(boolean noTitleBar) {
        this.noTitleBar = noTitleBar;
    }

    public boolean isShowColorNameInBarTitle() {
        return showColorNameInBarTitle;
    }

    public void setShowColorNameInBarTitle(boolean showColorNameInBarTitle) {
        this.showColorNameInBarTitle = showColorNameInBarTitle;
    }

    public boolean isSmartRandomJoin() {
        return smartRandomJoin;
    }

    public void setSmartRandomJoin(boolean smartRandomJoin) {
        this.smartRandomJoin = smartRandomJoin;
    }

    public List<String> getScoreBoard() {
        return scoreBoard;
    }

    public void setScoreBoard(List<String> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public List<String> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(List<String> powerUps) {
        this.powerUps = powerUps;
    }

    public DistributedFiller getDistributedFiller() {
        return distributedFiller;
    }

    public void setDistributedFiller(DistributedFiller distributedFiller) {
        this.distributedFiller = distributedFiller;
    }

    public WorkloadRunnable getWorkloadRunnable() {
        return workloadRunnable;
    }

    public void setWorkloadRunnable(WorkloadRunnable workloadRunnable) {
        this.workloadRunnable = workloadRunnable;
    }

    public double getMaxMillisSecondPerTickToGenerateArenaDanceFloor() {
        return maxMillisSecondPerTickToGenerateArenaDanceFloor;
    }

    public void setMaxMillisSecondPerTickToGenerateArenaDanceFloor(double maxMillisSecondPerTickToGenerateArenaDanceFloor) {
        this.maxMillisSecondPerTickToGenerateArenaDanceFloor = maxMillisSecondPerTickToGenerateArenaDanceFloor;
    }

    public List<String> getSignDisplay() {
        return signDisplay;
    }

    public void setSignDisplay(List<String> signDisplay) {
        this.signDisplay = signDisplay;
    }

    public Map<String, Integer> getPotionEffects() {
        return potionEffects;
    }

    public void setPotionEffects(Map<String, Integer> potionEffects) {
        this.potionEffects = potionEffects;
    }

    public String getWinDanceFloor() {
        return winDanceFloor;
    }

    public void setWinDanceFloor(String winDanceFloor) {
        this.winDanceFloor = winDanceFloor;
    }

    public String getWaitDanceFloor() {
        return waitDanceFloor;
    }

    public void setWaitDanceFloor(String waitDanceFloor) {
        this.waitDanceFloor = waitDanceFloor;
    }

    public boolean isPowerUpAsBlock() {
        return powerUpAsBlock;
    }

    public void setPowerUpAsBlock(boolean powerUpAsBlock) {
        this.powerUpAsBlock = powerUpAsBlock;
    }

    public GameMode getGameModeOnLeave() {
        return gameModeOnLeave;
    }

    public void setGameModeOnLeave(GameMode gameModeOnLeave) {
        this.gameModeOnLeave = gameModeOnLeave;
    }
    public boolean isUseSameColourGroup() {
        return useSameColourGroup;
    }

    public void setUseSameColourGroup(boolean useSameColourGroup) {
        this.useSameColourGroup = useSameColourGroup;
    }

    public List<Material> getDanceFloorColourGroupActualMaterials() {
        return danceFloorColourGroupActualMaterials;
    }

    public void setDanceFloorColourGroupActualMaterials(List<Material> danceFloorColourGroupActualMaterials) {
        this.danceFloorColourGroupActualMaterials = danceFloorColourGroupActualMaterials;
    }

    public int getVolumeNoteBlock() {
        return VolumeNoteBlock;
    }

    public boolean isUseParticles() {
        return useParticles;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public Double getParticleSize() {
        return particleSize;
    }

    public Particle getParticle() {
        return particle;
    }

    public Color getDefaultParticleColour() {
        return defaultParticleColour;
    }

    public ParticleColourSetting getParticleColourSetting() {
        return particleColourSetting;
    }

    public String getCustomHeadLink() {
        return customHeadLink;
    }

    public boolean isUseCustomHeadLink() {
        return useCustomHeadLink;
    }

    public String getSuffix_colour() {
        return suffix_colour;
    }

    public void setSuffix_colour(String suffix_colour) {
        this.suffix_colour = suffix_colour;
    }

    public List<Material> getExcludedDanceFloorBlocks() {
        return excludedDanceFloorBlocks;
    }

    public void setExcludedDanceFloorBlocks(List<Material> excludedDanceFloorBlocks) {
        this.excludedDanceFloorBlocks = excludedDanceFloorBlocks;
    }
}