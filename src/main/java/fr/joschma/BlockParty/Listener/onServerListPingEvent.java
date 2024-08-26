package fr.joschma.BlockParty.Listener;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class onServerListPingEvent implements Listener {

    BPM pl;
    String oldMotd= "";

    public onServerListPingEvent(BPM pl) {
        this.pl = pl;
    }

    @EventHandler
    public void ping(ServerListPingEvent e) {
        if (oldMotd.isEmpty())
            oldMotd = e.getMotd();

        if (!pl.isHubServer()) {
            String arenasName = "";
            for (Arena arena : pl.getAm().getArenas()) {
                arenasName = arenasName + arena.getName() + "," + arena.getMaxPlayer() + ".";
            }
            e.setMotd(arenasName);
        }
    }
}
