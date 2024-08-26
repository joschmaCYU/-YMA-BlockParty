// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Manager;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.HubArena;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager
{
    List<Arena> arenas = new ArrayList<>();
    List<String> arenaNames = new ArrayList<>();
    List<HubArena> hubArenas = new ArrayList<>();
    List<String> hubArenaNames = new ArrayList<>();
    
    public ArenaManager() {
        this.arenas = new ArrayList<Arena>();
        this.arenaNames = new ArrayList<String>();
    }
    
    public void addArenaListNFile(final Arena a) {
        this.arenas.add(a);
        this.addArenaNames(a.getName());
        //FileManager.saveComments(a.getFile());
    }
    
    public void rmvArenaListNFile(final Arena a) {
        this.arenas.remove(a);
        this.rmvArenaNames(a.getName());
        a.getFile().delete();
    }
    
    public Arena getArena(final String arena) {
        for (final Arena a : this.getArenas()) {
            if (a.getName().equals(arena)) {
                return a;
            }
        }
        return null;
    }

    public HubArena getHubArena(String name) {
        for (final HubArena a : this.getHubArenas()) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }
    
    public Arena getArenaPlayer(final Player p) {
        for (final Arena a : this.arenas) {
            if (a.getPlayers().contains(p)) {
                return a;
            }
        }
        return null;
    }
    
    public void addArenaNames(final String name) {
        if (!this.arenaNames.contains(name)) {
            this.arenaNames.add(name);
        }
    }
    
    public void rmvArenaNames(final String name) {
        if (this.arenaNames.contains(name)) {
            this.arenaNames.remove(name);
        }
    }
    
    public List<String> getArenaNames() {
        return this.arenaNames;
    }
    
    public void setArenaNames(final List<String> arenaNames) {
        this.arenaNames = arenaNames;
    }
    
    public List<Arena> getArenas() {
        return this.arenas;
    }
    
    public void addArena(final Arena a) {
        this.arenas.add(a);
    }
    
    public void rmvArena(final Arena a) {
        this.arenas.remove(a);
    }
    
    public void setArenas(final List<Arena> arenas) {
        this.arenas = arenas;
    }

    public List<HubArena> getHubArenas() {
        return hubArenas;
    }

    public List<String> getHubArenaNames() {
        return hubArenaNames;
    }
}
