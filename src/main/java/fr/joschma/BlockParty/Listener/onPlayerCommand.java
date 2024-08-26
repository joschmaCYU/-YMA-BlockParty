// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.ArenaManager;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class onPlayerCommand implements Listener
{
    BPM pl;
    ArenaManager am;
    
    public onPlayerCommand(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }
    
    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        final Arena a = this.am.getArenaPlayer(p);

        if (a != null && !a.getAllowedCommands().contains(e.getMessage())) {
            this.pl.getDebug().msg(p, Language.MSG.notAllowedToSendMessage.msg(p));
            e.setCancelled(true);
        }
    }
}
