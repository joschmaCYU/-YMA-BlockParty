package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.BPM;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class onPlayerJoin implements Listener {

    BPM pl;
    // Player lastConnectedPlayer;

    public onPlayerJoin(BPM pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onPlayerJoinListener(PlayerJoinEvent e) {
        if (pl.getConfig().getBoolean("useBungeeCord")) {
            Player p = e.getPlayer();
            //if (pl.isFirstPlayerJoined()) {
            pl.setFirstPlayerJoined(false);
            final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.runTaskLater(pl, new Runnable() {
                @Override
                public void run() {
                    pl.sendCustomData(p, "enable", pl.getConfig().getString("ServerName"));
                }
            }, 20L);
            //}

            //lastConnectedPlayer = p

        /*if(pl.getConfig().getBoolean("useBungeeCord")) {
            for (Arena a : pl.getAm().getArenas()) {
                if (JoinArenaCheck.canJoin(p, a)) {
                    JoinArena.joinArena(p, a);
                    return;
                }
            }
        }*/
        }
    }
}
