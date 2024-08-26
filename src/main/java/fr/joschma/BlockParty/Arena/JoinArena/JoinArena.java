// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena.JoinArena;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.Cuboid.DistributedFiller;
import fr.joschma.BlockParty.Cuboid.WorkloadRunnable;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinArena {
    public static void joinArena(final Player p, final Arena a) {
        if (JoinArenaCheck.canJoin(p, a)) {
            if (a.isSaveInventory())
                a.getPl().getInvManager().saveInventory(p);
            p.teleport(a.getLobbySpawn());
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
            p.setAllowFlight(false);
            if (a.isClearInventory())
                p.getInventory().clear();
            a.giveStuff(p);
            if (!a.getPlayers().contains(p))
                a.addPlayer(p);
            if (a.isResetExp())
                p.setExp(0.0f);
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.setFoodLevel(20);

            if (a.getState() == ArenaState.CLEARED) {
                a.getRoundFile().generateDF(a);
                if (a.getMinPlayer() <= a.getPlayers().size()) {
                    a.setState(ArenaState.WATTING);
                    a.getWaitLobbyTimer().startCountDown();
                } else {
                    a.getPl().getDebug().error(p, Language.MSG.notEnoughtPlayer.msg(p));
                }
                if (a.getPlayers().size() == a.getMaxPlayer()) {
                    a.getWaitLobbyTimer().stopTimer(a);
                    a.getStartGame().startGame(a);
                    a.getPl().getDebug().msg(p, Language.MSG.gameOnMaxPlayerStarting.msg(p));
                }
            }
            a.updateSign();

            for (Player pla : a.getPlayers()) {
                if (pla != p) {
                    // Check if connected
                    a.getPl().getDebug().msg(pla, Language.MSG.JoinGame.msg(p));
                }
            }
        } else if (a.getState() == ArenaState.INGAME) {
            if (a.isAllowJoinDuringGame() || p.hasPermission("BlockParty.VIP")) {
                final Location loc = a.getDanceFloorCuboid().getRandomLocation();
                loc.setY(a.getDanceFloorCuboid().getHighestY() + 3.0);
                p.teleport(loc);

                p.teleport(loc);
                p.setGameMode(GameMode.SPECTATOR);
                if (!a.getPlayers().contains(p))
                    a.addPlayer(p);
            }
        }
    }
}
