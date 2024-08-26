package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class onPlayerChat implements Listener {

    BPM pl;

    public onPlayerChat(BPM pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onPlayerChatListener(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();

        if (pl.getTextToAddToCommandManager().getTextToAddToCommand().containsKey(p)) {
            String command = pl.getTextToAddToCommandManager().getTextToAddToCommand().get(p);

            if(pl.isNumeric(msg)) {
                pl.getDebug().error(p, Language.MSG.NumericsAreNotAllowed.msg(p));
                pl.getDebug().error(p, Language.MSG.Retry.msg(p));
                return;
            }

            final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

            scheduler.runTask(pl, new Runnable() {
                @Override
                public void run() {
                    p.performCommand(command + msg);
                }
            });

            pl.getTextToAddToCommandManager().getTextToAddToCommand().remove(p);
            e.setCancelled(true);
        }
    }
}
