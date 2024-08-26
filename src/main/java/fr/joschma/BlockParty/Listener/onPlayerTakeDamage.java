// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class onPlayerTakeDamage implements Listener {
    BPM pl;
    ArenaManager am;

    public onPlayerTakeDamage(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }

    boolean notCancel;

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            final Player p = (Player) e.getDamager();
            final Arena a = this.am.getArenaPlayer(p);

            if (a != null) {
                if (!a.isAllowPVP()) {
                    e.setCancelled(true);
                } else {
                    notCancel = true;
                    return;
                }
            }
        }

        if (e.getEntity() instanceof Player) {
            final Player p = (Player) e.getEntity();
            final Arena a = this.am.getArenaPlayer(p);
            if (a != null) {
                e.setCancelled(true);
            }
        }

    }

    /*@EventHandler
    public void onEntityDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player p = (Player) e.getEntity();
            final Arena a = this.am.getArenaPlayer(p);

            if (notCancel) {
                notCancel = false;
                return;
            }

            if (a != null) {
                e.setCancelled(true);
                Bukkit.broadcastMessage("cancel2");
            }
        }
    }*/
}
