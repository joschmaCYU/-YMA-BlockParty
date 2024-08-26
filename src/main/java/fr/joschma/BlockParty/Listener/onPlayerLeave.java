// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onPlayerLeave implements Listener {
    BPM pl;
    ArenaManager am;

    public onPlayerLeave(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }

    @EventHandler
    public void onPlayerLeaveListener(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final Arena a = this.am.getArenaPlayer(p);

        if (a != null) {
            a.leaveGame(p);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            p.showPlayer(player);
        }

        if(pl.getPlayerHiderManager().getHidden().contains(p))
            pl.getPlayerHiderManager().getHidden().remove(p.getUniqueId());
    }
}
