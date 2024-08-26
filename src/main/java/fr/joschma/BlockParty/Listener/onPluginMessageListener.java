package fr.joschma.BlockParty.Listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.HubArena;
import fr.joschma.BlockParty.Arena.JoinArena.JoinArena;
import fr.joschma.BlockParty.Arena.JoinArena.JoinArenaCheck;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.BPM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class onPluginMessageListener implements PluginMessageListener {

    BPM pl;

    public onPluginMessageListener(BPM pl) {
        this.pl = pl;
    }

    // TODO which player is it ?
    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] bytes) {
        if (!channel.equalsIgnoreCase("joschma:blockparty" + pl.getConfig().getString("ServerName")))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();

        switch (subChannel) {
            case "connect":
                String aName = in.readUTF();
                Arena ar = pl.getAm().getArena(aName);
                Bukkit.broadcastMessage("I want to connect " + p.getName() + " to " + ar.getName());
                if (JoinArenaCheck.canJoin(p, ar)) {
                    JoinArena.joinArena(p, ar);
                } else {
                    // TODO make player leave because he can't join
                }

                break;
            case "enabled":
                String isHubServer = in.readUTF();
                Bukkit.broadcastMessage("is hub : " + (isHubServer));
                if (isHubServer.equals("true")) {
                    // TODO get all arenas --> only command for arena sign
                    pl.setHubServer(true);
                    pl.setUpHubServer();

                    pl.sendCustomData(p, "givearenas");
                } else {
                    // is mini game server
                }
                break;
            case "getarenas":
                for (Arena a : pl.getAm().getArenas()) {
                    pl.sendCustomData(p, "arenadescription", a.getName(), String.valueOf(a.getMaxPlayer()));
                }
                Bukkit.broadcastMessage("getArenas");
                break;
            case "creationhubarena":
                String motd = in.readUTF();
                String[] arenas = motd.split("\\.");

                for (String arenaString : arenas) {
                    String[] arenaDetails = arenaString.split(",");
                    String arenaName = arenaDetails[0];

                    if (!pl.getAm().getHubArenaNames().contains(arenaName)) {
                        String maxPlayer = arenaDetails[1];
                        Bukkit.broadcastMessage("Arena: " + arenaName + " " + maxPlayer);
                        HubArena hubArena = new HubArena(pl, arenaName, Integer.valueOf(maxPlayer), ArenaState.CLEARED);
                        pl.getAm().getHubArenas().add(hubArena);
                        pl.getAm().getHubArenaNames().add(arenaName);
                    }
                }

                break;
        }
    }
}
