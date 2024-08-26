// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Manager;

import fr.joschma.BlockParty.Arena.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class CreationZoneManager {
    static HashMap<Player, List<Location>> playerLocation;
    static HashMap<Player, Arena> creationZone;
    static HashMap<Player, Arena> creationZoneArena;
    static HashMap<Player, String> creationDanceZone;

    public static HashMap<Player, Arena> getCreationZone() {
        return CreationZoneManager.creationZone;
    }

    public static void setCreationZone(final HashMap<Player, Arena> creationZone) {
        CreationZoneManager.creationZone = creationZone;
    }

    public static void addCreationZone(final Player p, final Arena a) {
        CreationZoneManager.creationZone.put(p, a);
    }

    public static void rmvCreationZone(final Player p) {
        CreationZoneManager.creationZone.remove(p);
    }

    public static HashMap<Player, Arena> getCreationZoneArena() {
        return CreationZoneManager.creationZoneArena;
    }

    public static void setCreationZoneArena(final HashMap<Player, Arena> creationZoneArena) {
        CreationZoneManager.creationZoneArena = creationZoneArena;
    }

    public static void addCreationZoneArena(final Player p, final Arena a) {
        CreationZoneManager.creationZoneArena.put(p, a);
    }

    public static void rmvCreationZoneArena(final Player p) {
        CreationZoneManager.creationZoneArena.remove(p);
    }

    public static HashMap<Player, String> getCreationDanceZone() {
        return creationDanceZone;
    }

    public static void setCreationDanceZone(HashMap<Player, String> creationDanceZone) {
        CreationZoneManager.creationDanceZone = creationDanceZone;
    }

    public static void addCreationDanceZone(Player p, String name) {
        CreationZoneManager.creationDanceZone.put(p, name);
    }

    public static void rmvCreationDanceZone(Player p) {
        CreationZoneManager.creationDanceZone.remove(p);
    }

    public static HashMap<Player, List<Location>> getPlayerLocation() {
        return CreationZoneManager.playerLocation;
    }

    public static void setPlayerLocation(HashMap<Player, List<Location>> PlayerLocation) {
        CreationZoneManager.playerLocation = playerLocation;
    }

    public static void addPlayerLocation(Player p, List<Location> locations) {
        CreationZoneManager.playerLocation.put(p, locations);
    }

    public static void rmvPlayerLocation(Player p) {
        CreationZoneManager.playerLocation.remove(p);
    }

    static {
        CreationZoneManager.creationZone = new HashMap<Player, Arena>();
        CreationZoneManager.creationZoneArena = new HashMap<Player, Arena>();
        CreationZoneManager.creationDanceZone = new HashMap<Player, String>();
        CreationZoneManager.playerLocation = new HashMap<Player, List<Location>>();
    }
}
