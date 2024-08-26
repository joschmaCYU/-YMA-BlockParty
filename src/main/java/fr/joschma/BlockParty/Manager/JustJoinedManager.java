package fr.joschma.BlockParty.Manager;

import fr.joschma.BlockParty.BPM;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JustJoinedManager {

    List<UUID> joined = new ArrayList<UUID>();

    public List<UUID> getJoined() {
        return joined;
    }

    public void setJoined(List<UUID> joined) {
        this.joined = joined;
    }
}
