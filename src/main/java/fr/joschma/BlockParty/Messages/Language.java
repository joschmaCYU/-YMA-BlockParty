// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Messages;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import fr.joschma.BlockParty.Manager.FileManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class Language {
    File file;
    YamlConfiguration fc;

    public Language() {
        this.file = FileManager.load("Language");
        this.fc = FileManager.load(this.file);
    }

    public static String parse(final MSG msg) {
        return msg.msg();
    }

    public enum MSG {
        couldNotRemoveDanceFloor(fc().getString("CouldNotRemoveDanceFloor")),
        notEnoughtPlayer(fc().getString("notEnoughtPlayer")),
        putFirstLoc(fc().getString("putFirstLoc")),
        putSecondLoc(fc().getString("putSecondLoc")),
        notFinished(fc().getString("notFinished")),
        appreciateTimeNegative(fc().getString("appreciateTimeNegative")),
        baseRemoveFloorTimeNegative(fc().getString("baseRemoveFloorTimeNegative")),
        danceFloorCuboidIsNull(fc().getString("danceFloorCuboidIsNull")),
        danceFloorFloorMaterialsIsNull(fc().getString("danceFloorFloorMaterialsIsNull")),
        durationPowerUpNegative(fc().getString("durationPowerUpNegative")),
        fileIsNull(fc().getString("fileIsNull")),
        levelOfPowerUpNegative(fc().getString("levelOfPowerUpNegative")),
        lobbySpawnNull(fc().getString("lobbySpawnNull")),
        maxNumberOfRoundNegative(fc().getString("maxNumberOfRoundNegative")),
        maxPlayerLowerThanMinPlayer(fc().getString("maxPlayerLowerThanMinPlayer")),
        minPlayerNegative(fc().getString("minPlayerNegative")),
        powerUpHeadNull(fc().getString("powerUpHeadNull")),
        powerUpNameNull(fc().getString("powerUpNameNull")),
        rangeToCatchPowerupNegative(fc().getString("rangeToCatchPowerupNegative")),
        regenerateBlockTimeNegative(fc().getString("regenerateBlockTimeNegative")),
        removeFloorTimeNegative(fc().getString("removeFloorTimeNegative")),
        createdArena(fc().getString("createdArena")),
        arenaAlreadyExists(fc().getString("arenaAlreadyExists")),
        deletedArena(fc().getString("deletedArena")),
        finished(fc().getString("finished")),
        lobbySpawn(fc().getString("lobbySpawn")),
        createdArenaCuboid(fc().getString("createdArenaCuboid")),
        notAllowedToSendMessage(fc().getString("notAllowedToSendMessage")),
        gotPowerUp(fc().getString("gotPowerUp")),
        winnerFallOf(fc().getString("winnerFallOf")),
        playerFallOfInClearing(fc().getString("playerFallOfInClearing")),
        noBlockFoundInLookingDirection(fc().getString("noBlockFoundInLookingDirection")),
        succesfullySetSign(fc().getString("succesfullySetSign")),
        playerFallOfInWaitClear(fc().getString("playerFallOfInWaitClear")),
        toManyPlayers(fc().getString("toManyPlayers")),
        gameAlreadyStarted(fc().getString("gameAlreadyStarted")),
        signAlreadyAdded(fc().getString("signAlreadyAdded")),
        noExitSpawn(fc().getString("noExitSpawn")),
        setExitSpawn(fc().getString("setExitSpawn")),
        noArenaCuboid(fc().getString("noArenaCuboid")),
        youHaveTobeInAGameToExecuteThisCommand(fc().getString("youHaveTobeInAGameToExecuteThisCommand")),
        noGameFound(fc().getString("noGameFound")), ScoreboardMaxNumerOfRound(fc().getString("ScoreboardMaxNumberOfRound")),
        gameOnMaxPlayerStarting(fc().getString("gameOnMaxPlayerStarting")),
        youAreToManyToJoinInYourSquad(fc().getString("youAreToManyToJoinInYourSquad")),
        ScoreboardNumberOfPlayers(fc().getString("ScoreboardNumberOfPlayers")),
        ScoreboardNumerOfRound(fc().getString("ScoreboardNumberOfRound")), Waiting(fc().getString("Waiting")),
        Stop(fc().getString("Stop")), WaitLobbyTimer(fc().getString("WaitLobbyTimer")),
        LeaveSlimeBall(fc().getString("LeaveSlimeBall")), SongSelector(fc().getString("SongSelector")),
        ChoseSongGUI(fc().getString("ChoseSongGUI")), YouHaveVotedForTheSong(fc().getString("YouHaveVotedForTheSong")),
        YouHaveFinishedPlace(fc().getString("YouHaveFinishedPlace")), PowerUpSpeed(fc().getString("PowerUpSpeed")),
        PowerUpJump_Boost(fc().getString("PowerUpJump_Boost")), PowerUpSlow_Falling(fc().getString("PowerUpSlow_Falling")),
        JoinGame(fc().getString("JoinGame")), LeaveGame(fc().getString("LeaveGame")), noPermission(fc().getString("NoPermission")),
        YouWin(fc().getString("YouWin")), StartGameTitle(fc().getString("StartGameTitle")),
        YouDiedTitle(fc().getString("YouDiedTitle")),
        YouDiedTitleSubtitle(fc().getString("YouDiedTitleSubtitle")), PlayerHiderText(fc().getString("PlayerHiderText")),
        PlayerHiderVisible(fc().getString("PlayerHiderVisible")), PlayerHiderHidden(fc().getString("PlayerHiderHidden")),
        PlayerHiderVisibleText(fc().getString("PlayerHiderVisibleText")),
        PlayerHiderHiddenText(fc().getString("PlayerHiderHiddenText")), ScoreboardNumberOfPlayersText(fc().getString("ScoreboardNumberOfPlayersText")),
        ScoreboardNumberOfRoundText(fc().getString("ScoreboardNumberOfRoundText")), ScoreboardTimeText(fc().getString("ScoreboardTimeText")),
        ScoreboardTime(fc().getString("ScoreboardTime")), PowerUpSpawned(fc().getString("PowerUpSpawned")),
        PowerUpCollect(fc().getString("PowerUpCollect")), PowerUpCollector(fc().getString("PowerUpCollector")),
        ColourRainItemName(fc().getString("ColourRainItemName")), PowerUpRain(fc().getString("PowerUpRain")),
        YouHaveLeft(fc().getString("YouHaveLeft")), ScoreboardTitle(fc().getString("ScoreboardTitle")),
        StartTitle(fc().getString("StartTitle")), StartUnderTitle(fc().getString("StartUnderTitle")),
        LeapItemName(fc().getString("LeapItemName")), PowerUpLeap(fc().getString("PowerUpLeap")),
        PowerUpRandomTP(fc().getString("PowerUpRandomTP")), PlayerEliminatedMessage(fc().getString("PlayerEliminatedMessage")),
        CowName(fc().getString("CowName")), PowerUpNightVision(fc().getString("PowerUpNightVision")),
        SuccessfullyAddedCustomDanceFloor(fc().getString("SuccessfullyAddedCustomDanceFloor")),
        TypeCustomDanceFloorName(fc().getString("TypeCustomDanceFloorName")), AddedArenaCuboid(fc().getString("AddedArenaCuboid")),
        DanceFloorAlreadyExist(fc().getString("DanceFloorAlreadyExist")),
        AlreadyCreatingDanceZone(fc().getString("AlreadyCreatingDanceZone")),
        SuccessfullyDeletedCustomDanceFloor(fc().getString("SuccessfullyDeletedCustomDanceFloor")),
        DanceFloorNotExist(fc().getString("DanceFloorNotExist")),
        CreatedCustomDanceCuboid(fc().getString("CreatedCustomDanceCuboid")),
        SuccessfullyRemovedCustomDanceFloor(fc().getString("SuccessfullyRemovedCustomDanceFloor")),
        NumericsAreNotAllowed(fc().getString("NumericsAreNotAllowed")), Retry(fc().getString("Retry")),
        PausedGame(fc().getString("PausedGame")), NotValidName(fc().getString("NotValidName")),
        PowerUpPlayerSwap(fc().getString("PowerUpPlayerSwap")), state_cleared(fc().getString("StateCleared")),
        state_waiting(fc().getString("StateWaiting")), state_in_game(fc().getString("StateInGame")),
        state_clearing(fc().getString("StateClearing")),state_paused(fc().getString("StatePaused"));

        String message;

        private MSG(final String message) {
            this.message = message;
        }

        public String msg() {
            if(message == null) {
                return message = "Text could not be found";
            }

            return message = ChatColor.translateAlternateColorCodes('&', message);
        }

        public String msg(Player p) {
            if(message == null) {
                return message = "Text could not be found";
            }

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

        public static YamlConfiguration fc() {
            return FileManager.load(FileManager.load("Language"));
        }
    }
}
