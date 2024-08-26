// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.ArenaManager;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class onPlayerBreakBlock implements Listener
{
    BPM pl;
    ArenaManager am;
    
    public onPlayerBreakBlock(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }
    
    @EventHandler
    public void onBreakBlock(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        final Arena a = this.am.getArenaPlayer(p);
        if (a != null) {
            e.setCancelled(true);
        }
        if (p.hasPermission("BlockParty.admin")) {
            for (final Arena ar : this.pl.getAm().getArenas()) {
                if (ar.getSigns() != null) {
                    for (final Sign sign : ar.getSigns()) {
                        if (sign.getLocation().equals((Object)e.getBlock().getLocation())) {
                            ar.getSigns().remove(sign);
                            this.pl.getDebug().msg(p, "You have deleted the sign");
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onBreakBlock(final HangingBreakByEntityEvent e) {
        if (e.getRemover() instanceof Player) {
            final Player p = (Player)e.getRemover();
            final Arena a = this.am.getArenaPlayer(p);
            if (a != null) {
                e.setCancelled(true);
            }
            for (final Arena ar : this.pl.getAm().getArenas()) {
                if (ar.getSigns() != null) {
                    for (final Sign sign : ar.getSigns()) {
                        if (sign.getLocation().equals((Object)e.getEntity().getLocation())) {
                            ar.getSigns().remove(sign);
                            this.pl.getDebug().msg(p, "You have deleted the sign");
                        }
                    }
                }
            }
        }
    }
}
