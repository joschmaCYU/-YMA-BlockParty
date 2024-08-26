package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.ArenaManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class onSnowballLand implements Listener {

    BPM pl;
    ArenaManager am;

    public onSnowballLand(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }

    @EventHandler
    public void onSnowFall(final ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            final Player p = (Player) e.getEntity().getShooter();
            final Arena a = am.getArenaPlayer(p);
            final Entity en = e.getEntity();

            if (a != null && en.getType() != null && e.getHitBlock() != null) {
                if (en.getType() == EntityType.SNOWBALL) {
                    if (a.getDanceFloorCuboid().isIn(e.getHitBlock().getLocation())) {
                        final Material ma = a.getRoundFile().randomFloorMa(a);
                        if (e.getHitBlock() != null) {
                            final Block b = e.getHitBlock().getLocation().getBlock();
                            b.setType(ma);
                            for (Player players : a.getPlayers()) {
                                players.playSound(players.getLocation(), Sound.BLOCK_SNOW_BREAK, 1.0f, 1.0f);
                            }
                        }
                    }
                }
            }
        }
    }
}
