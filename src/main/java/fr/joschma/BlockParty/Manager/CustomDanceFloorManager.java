package fr.joschma.BlockParty.Manager;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomDanceFloorManager {

    Map<String, List<Material>> customDanceFloor = new HashMap<String, List<Material>>();

    public Map<String, List<Material>> getCustomDanceFloor() {
        return customDanceFloor;
    }

    public void setCustomDanceFloor(Map<String, List<Material>> customDanceFloor) {
        this.customDanceFloor = customDanceFloor;
    }
}
