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
import fr.joschma.BlockParty.Manager.FileManager;
import org.bukkit.*;
import org.bukkit.block.Sign;

import java.util.*;

public class CreateArena {
    BPM pl;

    public CreateArena(final BPM pl) {
        this.pl = pl;
    }

    public Arena createArena(final String name) {
        final List<Material> dfMaterials = new ArrayList<Material>();
        dfMaterials.add(XMaterial.BLACK_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.BLUE_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.BROWN_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.CYAN_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.GRAY_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.GREEN_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.LIGHT_BLUE_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.LIGHT_GRAY_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.LIME_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.MAGENTA_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.ORANGE_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.PINK_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.PURPLE_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.RED_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.WHITE_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.YELLOW_TERRACOTTA.parseMaterial());
        dfMaterials.add(XMaterial.TERRACOTTA.parseMaterial());
        final List<Integer> roundToSpawnPowerUp = new ArrayList<Integer>();
        roundToSpawnPowerUp.add(3);
        roundToSpawnPowerUp.add(4);
        roundToSpawnPowerUp.add(6);
        roundToSpawnPowerUp.add(7);
        roundToSpawnPowerUp.add(9);
        roundToSpawnPowerUp.add(11);
        final List<Integer> roundToReduceTime = new ArrayList<Integer>();
        roundToReduceTime.add(3);
        roundToReduceTime.add(6);
        roundToReduceTime.add(9);
        roundToReduceTime.add(12);
        final List<String> pathToMusic = new ArrayList<String>();
        pathToMusic.add("Songs/RealThing.nbs");
        final Map<String, String> linkToMusic = new HashMap<String, String>();
        linkToMusic.put("InsertTitle1", "https://audio.jukehost.co.uk/sEJewZ4CFF7xRJoP7xjzw3IusePkIYLc.mp3");
        linkToMusic.put("InsertTitle2", "https://audio.jukehost.co.uk/v80VCCs7mGCHyrNiFg92BwuOvQGTOU9R.mp3");
        final Map<String, String> OpenAudioMusic = new HashMap<String, String>();
        OpenAudioMusic.put("InsertTitle1", "https://soundcloud.com/boyzteeth/ch3rryb0mb-p0pular-bootleg");
        OpenAudioMusic.put("LinkToMusic2", "https://soundcloud.com/djbarnhem/songz-1");
        final List<String> allowedCommands = new ArrayList<String>();
        allowedCommands.add("/hub");
        allowedCommands.add("/lobby");
        allowedCommands.add("/bp forcestart");
        allowedCommands.add("/blockparty forcestart");
        allowedCommands.add("/blockparty leave");
        allowedCommands.add("/bp leave");
        final List<String> commandsOnEnd = new ArrayList<String>();
        commandsOnEnd.add("give %player_name% iron_ingot 1");
        commandsOnEnd.add("give %winners% diamond 1");
        commandsOnEnd.add("give %losers% stone 1");
        final List<String> scoreBoard = new ArrayList<String>();
        scoreBoard.addAll(Arrays.asList(
                "&b&lDancers",
                "&f%bpm_number_of_player_playing%  ",
                "  ",
                "&a&lRound Number",
                "&f%bpm_round% ",
                " ",
                "&c&lRound Speed",
                "&f%bpm_round_length%s"));
        final List<String> powerUps = new ArrayList<String>();
        powerUps.addAll(Arrays.asList("Speed", "Jump Boost", "Slow Falling", "Rain", "Leap", "Random Teleport",
                "Cow", "Night Vision", "Player Swap"));

        final List<String> signDisplay = new ArrayList<String>();
        signDisplay.addAll(Arrays.asList(ChatColor.GREEN + "[BlockParty]", name, ChatColor.WHITE + "%bpm_number_of_player% / %bpm_maximum_player%"));

        final Map<String, Integer> potionEffects = new HashMap<>();
        potionEffects.put("SPEED", 0);

        final List<Material> excludedDanceFloorMaterials = new ArrayList<Material>();
        excludedDanceFloorMaterials.add(XMaterial.NETHER_BRICK_FENCE.parseMaterial());

        final Arena a = new Arena(this.pl, name, ChatColor.GRAY + "" + ChatColor.BOLD + "PowerUp", FileManager.createArenaFile(name),
                ArenaState.CLEARED, SongSetting.CHOOSE, false, 8, 2, 40, 10, 5,
                5, 5, 0.3, 10.1, 10.1,
                1, 10, 0, 0.5, 0, 13,
                XMaterial.BEACON.parseMaterial(), dfMaterials, roundToSpawnPowerUp, roundToReduceTime, pathToMusic, linkToMusic,
                "https://audio.jukehost.co.uk/fi3C1HLoGCw3hxeixkIJz6YOjEWObRAB.mp3", new ArrayList<Sign>(),
                "Songs/nothing.nbs", null, null, null, null, allowedCommands,
                commandsOnEnd, false, true, GameMode.SPECTATOR, true, SongProvider.NoteBlock, true,
                true, true, true, true, false, true, true, true,
                true, false, true, new ArrayList<String>(), true,
                false,false, true, OpenAudioMusic,
                "https://www.youtube.com/watch?v=XQktVJiBhos", false,true, true,
                scoreBoard, powerUps, 2.5, signDisplay, potionEffects, "none",
                "none", false, GameMode.SURVIVAL,false, true, 10,
                1.0, Particle.DRAGON_BREATH, Color.WHITE, ParticleColourSetting.INHANDBLOCKCOLOUR,
                "http://textures.minecraft.net/texture/ac4970ea91ab06ece59d45fce7604d255431f2e03a737b226082c4cce1aca1c4",
                true, "_Group", excludedDanceFloorMaterials, 100);

        this.pl.getAm().addArenaListNFile(a);
        this.pl.getInitializeArena().saveArena(a);

        return a;
    }
}
