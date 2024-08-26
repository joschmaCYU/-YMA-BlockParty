// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena;

import com.cryptomorin.xseries.XMaterial;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.Arena.State.ParticleColourSetting;
import fr.joschma.BlockParty.Arena.State.SongProvider;
import fr.joschma.BlockParty.Arena.State.SongSetting;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Cuboid.Cuboid;
import fr.joschma.BlockParty.Manager.FileManager;
import fr.joschma.BlockParty.Utils.UtilsLoc;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class InitializeArena {
    BPM pl;

    public InitializeArena(final BPM pl) {
        this.pl = pl;
    }

    public void initializeArena() {
        for (final String arenaName : this.pl.getAm().getArenaNames()) {
            final File file = FileManager.loadArenaFile(arenaName);
            final YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
            final String powerUpName = fc.getString("PowerUp.Name");
            final ArenaState state = ArenaState.CLEARED;
            SongSetting songSetting = SongSetting.CHOOSE;

            try {
                songSetting = SongSetting.valueOf(fc.getString("Settings.SongSetting"));
            } catch (Exception error) {
                Bukkit.broadcastMessage(pl.getUtilsPrefix().getPrefix() + "Song Setting not detected setting to CHOOSE");
            }

            final SongProvider songProvider = SongProvider.valueOf(fc.getString("Settings.SongProvider"));
            final boolean finished = fc.getBoolean("Settings.Finished");
            final boolean saveInventory = fc.getBoolean("Settings.SaveInventory");
            final boolean changeTime = fc.getBoolean("Settings.ChangeTime");
            final boolean clearInv = fc.getBoolean("Settings.ClearInventory");
            final boolean giveSlimeBall = fc.getBoolean("Settings.GiveSlimeBall");
            final boolean resetExp = fc.getBoolean("Settings.HandleExp");
            final boolean giveDye = fc.getBoolean("Settings.GiveDye");
            final boolean giveBlock = fc.getBoolean("Settings.GiveBlock");
            final boolean allowPVP = fc.getBoolean("Settings.AllowPVP");
            final boolean autoRestart = fc.getBoolean("Settings.AutoRestart");
            final boolean enableLightnings = fc.getBoolean("Settings.EnableLightnings");
            final boolean enablePowerUps = fc.getBoolean("Settings.EnablePowerUps");
            final boolean enableFireworksOnWin = fc.getBoolean("Settings.EnableFireworksOnWin");
            final boolean allowJoinDuringGame = fc.getBoolean("Settings.AllowJoinDuringGame");
            final boolean enableScoreboard = fc.getBoolean("Settings.EnableScoreboard");
            final boolean onlyCustomFloors = fc.getBoolean("Settings.OnlyCustomFloors");
            final boolean randomizeCustomFloor = fc.getBoolean("Settings.RandomizeCustomFloor");
            final boolean useColourPalette = fc.getBoolean("Settings.UseColourPalette");
            final boolean isNoTitleBar = fc.getBoolean("Settings.IsNoTitleBar");
            final boolean showColorNameInBarTitle = fc.getBoolean("Settings.ShowColorNameInBarTitle");
            final boolean smartRandomJoin = fc.getBoolean("Settings.SmartRandomJoin");
            final double mmspttgadc = fc.getDouble("Settings.MaxMillisSecondPerTickToGenerateArenaDanceFloor");
            final GameMode gmOnLeave = GameMode.valueOf(fc.getString("Settings.GameModeOnLeave").toUpperCase());

            // GAME
            final GameMode gmOnDeath = GameMode.valueOf(fc.getString("Game.GameModeOnDeath").toUpperCase());
            final boolean collision = fc.getBoolean("Game.Collision");
            final int maxPlayer = fc.getInt("Game.MaxPlayer");
            final int minPlayer = fc.getInt("Game.MinPlayer");
            final int lobbyWaitTime = fc.getInt("Game.LobbyCountDown");
            final int appreciateTime = fc.getInt("Game.TimeOnStageAfterWin");
            final int regenerateBlockTime = fc.getInt("Game.RegenerateBlockTime");
            final int waitTimeBeforeGiveColorFirstRound = fc.getInt("Game.WaitTimeBeforeGiveColorFirstRound");
            final int waitTimeBeforeGiveColor = fc.getInt("Game.WaitTimeBeforeGiveColor");
            final double timeToRemoveFromRemoveFloorTime = fc.getDouble("Game.TimeReductionPerChosenLevel");
            final double removeFloorTime;
            final double baseRemoveFloorTime = removeFloorTime = fc.getDouble("Game.InitialTimeToSearch");
            final int durationPowerUp = fc.getInt("PowerUp.Duration");
            final int levelOfPowerUp = fc.getInt("PowerUp.LevelOf");
            final double rangeToCatchPowerup = fc.getDouble("PowerUp.RangeToCatch");
            final int maxNumberOfRound = fc.getInt("Game.MaxNumberOfRound");
            Material powerUpHead = null;
            try {
                powerUpHead = XMaterial.valueOf(fc.getString("PowerUp.Head")).parseMaterial();
            } catch (Exception e) {
                Bukkit.broadcastMessage(ChatColor.RED + "No head for power up");
                Bukkit.broadcastMessage(ChatColor.RED + "Adding head for power up");
                powerUpHead = XMaterial.CREEPER_HEAD.parseMaterial();
            }
            final List<Material> danceFloorFloorMaterials = new ArrayList<Material>();
            for (final String dffm : fc.getStringList("Game.DanceFloorFloorMaterials")) {
                danceFloorFloorMaterials.add(XMaterial.valueOf(dffm).parseMaterial());
            }

            final List<Material> excludedDanceFloorBlocks = new ArrayList<Material>();
            for (final String dffm : fc.getStringList("Game.ExcludedDanceFloorFloorMaterials")) {
                excludedDanceFloorBlocks.add(XMaterial.valueOf(dffm).parseMaterial());
            }

            final List<Integer> roundToSpawnPowerUp = (List<Integer>) fc.getIntegerList("Game.RoundToSpawnPowerUp");
            final List<Integer> roundToReduceTime = (List<Integer>) fc.getIntegerList("Game.RoundToReduceTime");
            final List<String> pathToMusic = (List<String>) fc.getStringList("Sound.NoteBlock.PathToMusic");
            Map<String, String> linkToMusic = new HashMap<>();

            if (fc.getConfigurationSection("Sound.MCJukebox.LinkToMusic") != null) {
                for (String str : fc.getConfigurationSection("Sound.MCJukebox.LinkToMusic").getKeys(false)) {
                    linkToMusic.put(str, fc.getString("Sound.MCJukebox.LinkToMusic." + str));
                }
            }

            final String linkToStopMusic = fc.getString("Sound.MCJukebox.LinkToStopMusic");
            final String pathToStopMusic = fc.getString("Sound.NoteBlock.PathToStopMusic");
            final int VolumeNoteBlock = fc.getInt("Sound.NoteBlock.Volume");

            final Location lobbySpawn = UtilsLoc.stringToLoc(fc.getString("Spawn.LobbySpawn"), pl);
            final Location exitSpawn = UtilsLoc.stringToLoc(fc.getString("Spawn.ExitSpawn"), pl);
            int YLevelToDie = 0;
            Cuboid danceFloorCuboid = null;
            final Location locDance1 = UtilsLoc.stringToLoc(fc.getString("Cuboid.Dance.Loc1"), pl);
            final Location locDance2 = UtilsLoc.stringToLoc(fc.getString("Cuboid.Dance.Loc2"), pl);
            if (locDance1 != null && locDance2 != null) {
                danceFloorCuboid = new Cuboid(locDance1, locDance2);
                YLevelToDie = (int) (danceFloorCuboid.getLowestY() - 1.0);
            }
            Cuboid arenaFloorCuboid = null;
            final Location locArena1 = UtilsLoc.stringToLoc(fc.getString("Cuboid.Arena.Loc1"), pl);
            final Location locArena2 = UtilsLoc.stringToLoc(fc.getString("Cuboid.Arena.Loc2"), pl);
            if (locArena1 != null && locArena2 != null) {
                arenaFloorCuboid = new Cuboid(locArena1, locArena2);
            }

            final String winDanceFloor = fc.getString("Cuboid.WinDanceFloor");
            final String waitDanceFloor = fc.getString("Cuboid.WaitDanceFloor");

            final List<Sign> signs = new ArrayList<Sign>();
            for (final String sign : fc.getStringList("Signs")) {
                if (UtilsLoc.stringToLoc(sign, pl) != null) {
                    if (UtilsLoc.stringToLoc(sign, pl).getBlock() != null) {
                        final BlockState bl = UtilsLoc.stringToLoc(sign, pl).getBlock().getState();
                        if (bl instanceof Sign) {
                            signs.add((Sign) bl);
                        }
                    }
                }
            }

            final List<String> commandsOnEnd = (List<String>) fc.getStringList("Game.CommandsOnGameEnd");
            final List<String> allowedCommands = (List<String>) fc.getStringList("AllowedCommands");
            final List<String> customDanceFloorName = (List<String>) fc.getStringList("Cuboid.CustomDanceFloorName");
            final List<String> scoreBoard = (List<String>) fc.getStringList("ScoreBoard");
            final List<String> powerUps = (List<String>) fc.getStringList("PowerUp.PowerUps");

            Map<String, String> openAudioMusic = new HashMap<String, String>();

            if (fc.getConfigurationSection("Sound.OpenAudioMusic") != null) {
                for (String str : fc.getConfigurationSection("Sound.OpenAudioMusic.LinkToMusic").getKeys(false)) {
                    openAudioMusic.put(str, fc.getString("Sound.OpenAudioMusic.LinkToMusic." + str));
                }
            }

            String openAudioStopMusic = fc.getString("Sound.OpenAudioMusic.LinkToStopMusic");

            List<String> signDisplay = fc.getStringList("SignDisplay");

            Map<String, Integer> potionEffects = new HashMap<>();

            if (fc.getConfigurationSection("Game.Potions") != null) {
                for (String str : fc.getConfigurationSection("Game.Potions").getKeys(false)) {
                    potionEffects.put(str, fc.getInt("Game.Potions." + str));
                }
            }

            boolean powerUpAsBlock = fc.getBoolean("PowerUp.PowerUpAsBlock");
            boolean useSameColourGroup = fc.getBoolean("Game.UseSameColourGroup");
            boolean useParticles = fc.getBoolean("Game.Particles.Use");
            int particlesCount = fc.getInt("Game.Particles.Count");
            Double particlesSize = fc.getDouble("Game.Particles.Size");
            Particle particle = Particle.valueOf(fc.getString("Game.Particles.Type").toUpperCase());
            Color defaultColour = fc.getColor("Game.Particles.DefaultColour");
            ParticleColourSetting particleColourSetting = ParticleColourSetting.
                    valueOf(fc.getString("Game.Particles.ColourSetting").toUpperCase());
            String customHeadLink = fc.getString("PowerUp.CustomHead.Link");
            boolean useCustomHead = fc.getBoolean("PowerUp.CustomHead.Use");

            final File colourLanguageFile = FileManager.load("ColourLanguage");
            YamlConfiguration fcColourLanguage = YamlConfiguration.loadConfiguration(colourLanguageFile);
            String colour_suffix = fcColourLanguage.getString("Suffix");

            final Arena a = new Arena(pl, arenaName, powerUpName, file, state, songSetting, finished, maxPlayer, minPlayer,
                    lobbyWaitTime, appreciateTime, regenerateBlockTime, waitTimeBeforeGiveColorFirstRound, waitTimeBeforeGiveColor,
                    timeToRemoveFromRemoveFloorTime, removeFloorTime, baseRemoveFloorTime, 0, durationPowerUp, levelOfPowerUp,
                    rangeToCatchPowerup, YLevelToDie, maxNumberOfRound, powerUpHead, danceFloorFloorMaterials, roundToSpawnPowerUp,
                    roundToReduceTime, pathToMusic, linkToMusic, linkToStopMusic, signs, pathToStopMusic, lobbySpawn, exitSpawn, danceFloorCuboid,
                    arenaFloorCuboid, allowedCommands, commandsOnEnd, collision, changeTime, gmOnDeath, saveInventory, songProvider, giveSlimeBall,
                    clearInv, giveDye, resetExp, giveBlock, allowPVP, autoRestart, enableLightnings, enablePowerUps, enableFireworksOnWin,
                    allowJoinDuringGame, enableScoreboard, customDanceFloorName, randomizeCustomFloor, onlyCustomFloors, useColourPalette, false,
                    openAudioMusic, openAudioStopMusic, isNoTitleBar, showColorNameInBarTitle, smartRandomJoin, scoreBoard,
                    powerUps, mmspttgadc, signDisplay, potionEffects, winDanceFloor, waitDanceFloor, powerUpAsBlock, gmOnLeave,
                    useSameColourGroup, useParticles, particlesCount, particlesSize, particle, defaultColour,
                    particleColourSetting, customHeadLink, useCustomHead, colour_suffix, excludedDanceFloorBlocks, VolumeNoteBlock);

            this.pl.getAm().addArena(a);

            if (fc.getConfigurationSection("Game.ColourPalette.Round") != null) {
                for (String round : fc.getConfigurationSection("Game.ColourPalette.Round").getKeys(false)) {
                    List<Material> mas = new ArrayList<>();

                    for (String ma : fc.getStringList("Game.ColourPalette.Round." + round)) {
                        mas.add(Material.valueOf(ma));
                    }

                    a.getColourGroupMap().put(Integer.valueOf(round), mas);
                }
            }
        }
    }

    public void saveArena(final Arena a) {
        final File file = a.getFile();
        final YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);

        fc.set("Settings.Finished", (Object) a.getFinished());
        fc.set("Settings.SongSetting", (Object) a.getSongSetting().toString());
        fc.set("Settings.ChangeTime", a.isChangeTime());
        fc.set("Settings.SaveInventory", a.isSaveInventory());
        fc.set("Settings.ClearInventory", a.isClearInventory());
        fc.set("Settings.GiveSlimeBall", a.isGiveSlimeBall());
        fc.set("Settings.HandleExp", a.isResetExp());
        fc.set("Settings.GiveDye", a.isGiveDye());
        fc.set("Settings.SongProvider", a.getSongProvider().toString());
        fc.set("Settings.GiveBlock", a.isGiveBlock());
        fc.set("Settings.AllowPVP", a.isAllowPVP());
        fc.set("Settings.AutoRestart", a.isAutoRestart());
        fc.set("Settings.EnableLightnings", a.isEnableLightnings());
        fc.set("Settings.EnablePowerUps", a.isEnablePowerUps());
        fc.set("Settings.EnableFireworksOnWin", a.isEnableFireworksOnWin());
        fc.set("Settings.AllowJoinDuringGame", a.isAllowJoinDuringGame());
        fc.set("Settings.EnableScoreboard", a.isEnableScoreboard());
        fc.set("Settings.OnlyCustomFloors", a.isOnlyCustomFloors());
        fc.set("Settings.RandomizeCustomFloor", a.isRandomizeCustomFloor());
        fc.set("Settings.UseColourPalette", a.iscolourGroup());
        fc.set("Settings.IsNoTitleBar", a.isNoTitleBar());
        fc.set("Settings.ShowColorNameInBarTitle", a.isShowColorNameInBarTitle());
        fc.set("Settings.SmartRandomJoin", a.isSmartRandomJoin());
        fc.set("Settings.MaxMillisSecondPerTickToGenerateArenaDanceFloor",
                a.getMaxMillisSecondPerTickToGenerateArenaDanceFloor());
        fc.set("Settings.GameModeOnLeave", a.getGameModeOnLeave().toString());


        // Game
        fc.set("Game.Collision", a.isPlayerColision());
        fc.set("Game.MaxPlayer", (Object) a.getMaxPlayer());
        fc.set("Game.MinPlayer", (Object) a.getMinPlayer());
        fc.set("Game.LobbyCountDown", (Object) a.getLobbyWaitTime());
        fc.set("Game.TimeOnStageAfterWin", (Object) a.getAppreciateTime());
        fc.set("Game.RegenerateBlockTime", (Object) a.getRegenerateBlockTime());
        fc.set("Game.WaitTimeBeforeGiveColorFirstRound", (Object) a.getWaitTimeBeforeGiveColorFirstRound());
        fc.set("Game.WaitTimeBeforeGiveColor", (Object) a.getWaitTimeBeforeGiveColor());
        fc.set("Game.TimeReductionPerChosenLevel", (Object) a.getSecondsToRemoveFromRemoveFloorTime());
        fc.set("Game.InitialTimeToSearch", (Object) a.getBaseRemoveFloorTime());
        fc.set("Game.RoundToSpawnPowerUp", (Object) a.getRoundToSpawnPowerUp());
        fc.set("Game.RoundToReduceTime", (Object) a.getRoundToReduceTime());
        fc.set("Game.CommandsOnGameEnd", (Object) a.getCommandsOnGameEnd());
        fc.set("Game.GameModeOnDeath", a.getGmOnDeath().toString());
        fc.set("Game.MaxNumberOfRound", (Object) a.getMaxNumberOfRound());
        for (String potion : a.getPotionEffects().keySet())
            fc.set("Game.Potions." + potion, a.getPotionEffects().get(potion));

        final List<String> dfm = new ArrayList<String>();
        for (final Material ma : a.getDanceFloorFloorMaterials()) {
            dfm.add(String.valueOf(ma));
        }
        fc.set("Game.DanceFloorFloorMaterials", (Object) dfm);

        final List<String> excludedDanceFloorBlocks = new ArrayList<>();
        for (final Material mat : a.getExcludedDanceFloorBlocks()) {
            excludedDanceFloorBlocks.add(String.valueOf(mat));
        }
        fc.set("Game.ExcludedDanceFloorFloorMaterials", (Object) excludedDanceFloorBlocks);

        fc.set("Game.Particles.Use", a.isUseParticles());
        fc.set("Game.Particles.Count", a.getParticleCount());
        fc.set("Game.Particles.Size", a.getParticleSize());
        fc.set("Game.Particles.Type", a.getParticle().toString());
        fc.set("Game.Particles.DefaultColour", a.getDefaultParticleColour());
        fc.set("Game.Particles.ColourSetting", a.getParticleColourSetting().toString());
        fc.set("Game.UseSameColourGroup", a.isUseSameColourGroup());

        // Power ups
        fc.set("PowerUp.Duration", (Object) a.getDurationPowerUp());
        fc.set("PowerUp.RangeToCatch", (Object) a.getRangeToCatchPowerup());
        fc.set("PowerUp.LevelOf", (Object) a.getLevelOfPowerUp());
        fc.set("PowerUp.Name", (Object) a.getPowerUpName());
        fc.set("PowerUp.Head", (Object) String.valueOf(a.getPowerUpHead()));
        fc.set("PowerUp.PowerUps", a.getPowerUps());
        fc.set("PowerUp.PowerUpAsBlock", a.isPowerUpAsBlock());
        fc.set("PowerUp.CustomHead.Link", a.getCustomHeadLink());
        fc.set("PowerUp.CustomHead.Use", a.isUseCustomHeadLink());

        // Sound
        fc.set("Sound.NoteBlock.Volume", a.getVolumeNoteBlock());
        fc.set("Sound.NoteBlock.PathToMusic", (Object) a.getPathToMusic());
        fc.set("Sound.NoteBlock.PathToStopMusic", (Object) a.getPathToStopMusic());

        for (String name : a.getLinkToMusic().keySet()) {
            fc.set("Sound.MCJukebox.LinkToMusic." + name, a.getLinkToMusic().get(name));
        }

        for (String name : a.getOpenAudioMusic().keySet()) {
            fc.set("Sound.OpenAudioMusic.LinkToMusic." + name, a.getOpenAudioMusic().get(name));
        }

        fc.set("Sound.OpenAudioMusic.LinkToStopMusic", a.getOpenAudioStopMusic());
        fc.set("Sound.MCJukebox.LinkToStopMusic", a.getLinkToStopMusic());

        // Spawn
        if (a.getLobbySpawn() != null) {
            fc.set("Spawn.LobbySpawn", (Object) UtilsLoc.locToString(a.getLobbySpawn()));
        }
        if (a.getExitSpawn() != null) {
            fc.set("Spawn.ExitSpawn", (Object) UtilsLoc.locToString(a.getExitSpawn()));
        }
        final List<String> signsLoc = new ArrayList<String>();
        if (a.getSigns() != null) {
            for (final Sign sign : a.getSigns()) {
                signsLoc.add(UtilsLoc.locToString(sign.getLocation()));
            }
        }
        // Sign
        fc.set("Signs", (Object) signsLoc);

        // Cuboid
        fc.set("Cuboid.WinDanceFloor", a.getWinDanceFloor());
        fc.set("Cuboid.WaitDanceFloor", a.getWaitDanceFloor());
        fc.set("Cuboid.CustomDanceFloorName", a.getCustomDanceFloor());
        if (a.getDanceFloorCuboid() != null) {
            fc.set("Cuboid.Dance.Loc1", (Object) UtilsLoc.locToString(a.getDanceFloorCuboid().getPoint1()));
            fc.set("Cuboid.Dance.Loc2", (Object) UtilsLoc.locToString(a.getDanceFloorCuboid().getPoint2()));
        }
        if (a.getArenaCuboid() != null) {
            fc.set("Cuboid.Arena.Loc1", (Object) UtilsLoc.locToString(a.getArenaCuboid().getPoint1()));
            fc.set("Cuboid.Arena.Loc2", (Object) UtilsLoc.locToString(a.getArenaCuboid().getPoint2()));
        }

        fc.set("AllowedCommands", a.getAllowedCommands());
        fc.set("ScoreBoard", a.getScoreBoard());

        fc.set("SignDisplay", a.getSignDisplay());

        if (a.getPl().getVersion() > 17)
            setComments(fc);

        FileManager.save(file, fc);
    }

    public void setComments(YamlConfiguration fc) {
        fc.setComments("Game.ExcludedDanceFloorFloorMaterials", Arrays.asList("All the blocks that can be on the ground but not be selected as a colour"));
        fc.setComments("Spawn", Arrays.asList("All the coordinates for the spawns"));
        fc.setComments("Game.Particles.Use", Arrays.asList("Should particles spawn when a block disappear"));
        fc.setComments("Game.Particles.Count", Arrays.asList("The number of particles spawned"));
        fc.setComments("Game.Particles.Size", Arrays.asList("The size of each particle spawned"));
        fc.setComments("Game.Potions", Arrays.asList("All the potion effect a player has in game (POTION_NAME:POTION STRENGTH)",
                "Values can be found at https://javadoc.io/doc/com.github.cryptomorin/XSeries/latest/com/cryptomorin/xseries/XPotion.html"));
        fc.setComments("SignDisplay", Arrays.asList("What will be seen on signs for this arena"));
        fc.setComments("Settings.Finished", Arrays.asList("Is the party ready to be played"));
        fc.setComments("Settings.SongSetting", Arrays.asList("RANDOM --> players will not be able to chose the song." +
                "CHOOSE --> players can chose what song will be played"));
        fc.setComments("Settings.ChangeTime", Arrays.asList("Only in the waiting room the time of the day " +
                " is going to change depending on the wait lobby timer"));
        fc.setComments("Settings.SaveInventory", Arrays.asList("Should the plugin save the inventory of the player " +
                "and give it back at the end of the game"));
        fc.setComments("Settings.ClearInventory", Arrays.asList("If false players can keep their stuff in game"));
        fc.setComments("Settings.GiveSlimeBall", Arrays.asList("Should the plugin give the item to leave the game"));
        fc.setComments("Settings.HandleExp", Arrays.asList("Should the plugin save the exp of the player " +
                "and give it back at the end of the game"));
        fc.setComments("Settings.GiveDye", Arrays.asList("Should the plugin give the item to hide other player"));
        fc.setComments("Settings.SongProvider", Arrays.asList("The plugin that will play the songs (MCJukebox, NoteBlock, OpenAudioMC"));
        fc.setComments("Settings.GiveBlock", Arrays.asList("Should the plugin give the block that need to be stand on"));
        fc.setComments("Settings.AllowPVP", Arrays.asList("Can player attack other player"));
        fc.setComments("Settings.AutoRestart", Arrays.asList("Should the game start again after the end of it"));
        fc.setComments("Settings.EnableLightnings", Arrays.asList("Should the plugin shoot lightnings on the player death location"));
        fc.setComments("Settings.EnablePowerUps", Arrays.asList("Should all power ups be active"));
        fc.setComments("Settings.EnableFireworksOnWin", Arrays.asList("Should the plugin shoot fire works at the end of the game"));
        fc.setComments("Settings.AllowJoinDuringGame", Arrays.asList("Can people join the game as spectator"));
        fc.setComments("Settings.EnableScoreboard", Arrays.asList("Should the player have the scoreboard"));
        fc.setComments("Settings.OnlyCustomFloors", Arrays.asList("Play only with custom floors"));
        fc.setComments("Settings.RandomizeCustomFloor", Arrays.asList("Should the plugin swap the same colours for another one with custom " +
                "floors"));
        fc.setComments("Settings.UseColourPalette", Arrays.asList("Should the plugin use only the colours in the colour palette"));
        fc.setComments("Settings.IsNoTitleBar", Arrays.asList("Disable the bar above the health"));
        fc.setComments("Settings.ShowColorNameInBarTitle", Arrays.asList("Should the name of the colour be displayed on the bar above the health"));
        fc.setComments("Settings.SmartRandomJoin", Arrays.asList("This will help to fill lobbies quicker"));
        fc.setComments("Settings.MaxMillisSecondPerTickToGenerateArenaDanceFloor",
                Arrays.asList("This should be dealt with caution : the time (in milli seconds) in which the plugin will place blocks ever tick",
                        "The greater the surface of your dance floor the bigger this number has to be",
                        "The max is 50"));

        // Game
        fc.setComments("Game.Collision", Arrays.asList("Should player collide"));
        fc.setComments("Game.MaxPlayer", Arrays.asList("What is the maximum of player in this arena"));
        fc.setComments("Game.MinPlayer", Arrays.asList("What is the number of player necessary to start the lobby count down"));
        fc.setComments("Game.LobbyCountDown", Arrays.asList("The length of the lobby count down until the game starts"));
        fc.setComments("Game.TimeOnStageAfterWin", Arrays.asList("The time that winners stay on the dance floor"));
        fc.setComments("Game.RegenerateBlockTime", Arrays.asList("The time that player have to stop dancing"));
        fc.setComments("Game.WaitTimeBeforeGiveColorFirstRound", Arrays.asList("The time (only for the first round) that people have to wait to" +
                "get the block they need to go on"));
        fc.setComments("Game.WaitTimeBeforeGiveColor", Arrays.asList("For all the other round the time that people get to rest from the last" +
                "time they stopped dancing"));
        fc.setComments("Game.TimeReductionPerChosenLevel", Arrays.asList("The time that will be subtracted to each round (so that the time players" +
                "have to go to the block is smaller)"));
        fc.setComments("Game.InitialTimeToSearch", Arrays.asList("The time for the first round that people have to go on the block they have to"));
        fc.setComments("Game.RoundToSpawnPowerUp", Arrays.asList("The rounds in which power ups will spawn"));
        fc.setComments("Game.RoundToReduceTime", Arrays.asList("The round where the TimeReductionPerChosenLevel is going to be applied"));
        fc.setComments("Game.CommandsOnGameEnd", Arrays.asList("All the command that will be executed at the end of each game"));
        fc.setComments("Game.GameModeOnDeath", Arrays.asList("The game mode in which all spectator will be put"));
        fc.setComments("Game.MaxNumberOfRound", Arrays.asList("The maximum number of round before all the remaining player will win"));
        fc.setComments("Game.DanceFloorFloorMaterials", Arrays.asList("The list of all the blocks that could be in the dance floor"));
        fc.setComments("Game.SameColourGroup.Use", Arrays.asList("If true, all block of same colour will stay"));
        fc.setComments("Game.SameColourGroup.ExcludeDarkInHand", Arrays.asList("Shouldn't the block containing the name dark will be given to the player",
                "But they will stay on the dance floor"));
        fc.setComments("Game.SameColourGroup.ExcludeLightInHand", Arrays.asList("If true, the block containing the name light will not be given to the player",
                "But they will stay on the dance floor"));
        fc.setComments("Game.Particles.DefaultColour", Arrays.asList("The colour of the particle if the plugin can't find the colour of the block"));
        fc.setComments("Game.Particles.ColourSetting", Arrays.asList("It can be [Random, RemovedBlockColour, InHandBlockColour]"));

        // Power ups
        fc.setComments("PowerUp.Duration", Arrays.asList("The time the power up will last"));
        fc.setComments("PowerUp.RangeToCatch", Arrays.asList("The distance needed to catch the power up"));
        fc.setComments("PowerUp.LevelOf", Arrays.asList("The level of the potion effect that will be given"));
        fc.setComments("PowerUp.Name", Arrays.asList("The name that people will see above power ups"));
        fc.setComments("PowerUp.Head", Arrays.asList("The block that people will see as the power up"));
        fc.setComments("PowerUp.PowerUps", Arrays.asList("The list of all the power up (you can remove some)"));
        fc.setComments("PowerUp.PowerUpAsBlock", Arrays.asList("Should the power up be a block or just a floating entity"));
        fc.setComments("PowerUp.CustomHead.Link", Arrays.asList("You can find custom head here: https://minecraft-heads.com",
                "You need to get the last link (Minecraft-URL)"));
        fc.setComments("PowerUp.CustomHead.Use", Arrays.asList("Should the plugin use custom heads"));

        // Sound
        fc.setComments("Sound.NoteBlock.Volume", Arrays.asList("The volume at which the music will play"));
        fc.setComments("Sound.NoteBlock.PathToMusic", Arrays.asList("The file path to all the music that will be played in game"));
        fc.setComments("Sound.NoteBlock.PathToStopMusic", Arrays.asList("The file path to the music that will be played when the dance music stops"));
        fc.setComments("Sound.MCJukebox.LinkToMusic", Arrays.asList("The link to all the music that will be played in game"));
        fc.setComments("Sound.MCJukebox.LinkToStopMusic", Arrays.asList("The link to the music that will be played when the dance music stops"));
        fc.setComments("Sound.OpenAudioMusic.LinkToMusic", Arrays.asList("The link to all the music that will be played in game"));
        fc.setComments("Sound.OpenAudioMusic.LinkToStopMusic", Arrays.asList("The link to the music that will be played when the dance music stops"));

        fc.setComments("Signs", Arrays.asList("All the signs that lead to this party"));

        // Cuboid
        fc.setComments("Cuboid.CustomDanceFloorName", Arrays.asList("All the custom dance floors that can be played in this party"));
        fc.setComments("Cuboid.WinDanceFloor", Arrays.asList("The custom dance floor that will will be loaded when player win"));
        fc.setComments("Cuboid.WaitDanceFloor", Arrays.asList("The custom dance floor that will will be loaded when player wait"));

        fc.setComments("AllowedCommands", Arrays.asList("All the allowed command in game for all player"));
        fc.setComments("ScoreBoard", Arrays.asList("The scoreboard that every one can see on their right"));
    }
}
