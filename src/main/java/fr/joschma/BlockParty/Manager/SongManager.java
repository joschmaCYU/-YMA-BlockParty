package fr.joschma.BlockParty.Manager;

import fr.joschma.BlockParty.Arena.Arena;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SongManager {

    Arena a;
    public SongManager(Arena a) {
        this.a = a;
    }

    Map<String, Integer> numberOfVote = new HashMap<String, Integer>();
    Map<Player, String> playerVote = new HashMap<Player, String>();

    public Map<String, Integer> getNumberOfVoteMap() {
        return numberOfVote;
    }

    public void setPlayerSongMap(Map<String, Integer> numberOfVote) {
        this.numberOfVote = numberOfVote;
    }

    public Map<Player, String> getPlayerVote() {
        return playerVote;
    }

    public void setPlayerVote(Map<Player, String> playerVote) {
        this.playerVote = playerVote;
    }
}
