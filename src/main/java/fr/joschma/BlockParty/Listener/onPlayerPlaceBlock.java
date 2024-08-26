// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onPlayerPlaceBlock implements Listener
{
    BPM pl;
    ArenaManager am;
    
    public onPlayerPlaceBlock(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }
    
    @EventHandler
    public void onPlaceBlock(final BlockPlaceEvent e) {
        final Player p = e.getPlayer();
        final Arena a = this.am.getArenaPlayer(p);
        if (a != null) {
            e.setCancelled(true);
        }
    }
}
