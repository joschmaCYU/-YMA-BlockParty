package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.BPM;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class onPlayerSwapHandItemsListener implements Listener {

    BPM pl;

    public onPlayerSwapHandItemsListener(BPM pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
       if (pl.getAm().getArenaPlayer(e.getPlayer()) != null)
           e.setCancelled(true);
    }
}
