// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena.End;

import com.cryptomorin.xseries.messages.Titles;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Messages.Language;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class End
{
    public static void noMoreRoundWin(final Arena a) {
        for (final Player p : a.getPlayers()) {
            if (!a.getPlayersAlive().contains(p)) {
                p.teleport(a.getLobbySpawn());
            }
        }
        final List<Player> winners = new ArrayList<Player>();
        winners.addAll(a.getPlayersAlive());
        a.setWinners(winners);
        for (final Player p2 : a.getWinners()) {
            if(a.isClearInventory())
                p2.getInventory().clear();
            p2.sendTitle(Language.MSG.YouWin.msg(), "", 1, 80, 1);
        }
        a.getAppreciateTimeFile().startCountDown(a);
    }
    
    public static void playerWin(final Arena a, final Player p) {
        for (final Player pla : a.getPlayers()) {
            if (!a.getPlayersAlive().contains(pla)) {
                pla.teleport(a.getLobbySpawn());
            }
        }
        if(a.isClearInventory())
            p.getInventory().clear();
        p.sendTitle(Language.MSG.YouWin.msg(), "", 1, 80, 1);
        final List<Player> winners = new ArrayList<Player>();
        winners.add(p);
        a.setWinners(winners);
        a.getAppreciateTimeFile().startCountDown(a);
    }
}
