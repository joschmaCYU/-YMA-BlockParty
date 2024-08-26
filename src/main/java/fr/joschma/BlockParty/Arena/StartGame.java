// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena;

import com.cryptomorin.xseries.XPotion;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Locale;

public class StartGame {
    int i;
    BPM pl;

    public StartGame(final BPM pl) {
        this.i = 1;
        this.pl = pl;
    }

    public void startGame(final Arena a) {
        pl.getMusicManager().setUpMusics(a);
        a.setState(ArenaState.INGAME);
        a.setRound(1);
        a.setRemoveFloorTime(a.getBaseRemoveFloorTime());
        final Material ma = a.getRoundFile().randomFloorMa(a);
        a.setDanceFloorActualMaterial(ma);
        a.getRoundFile().generateDF(a);
        for (final Player p : a.getPlayers()) {
            if (a.isClearInventory())
                p.getInventory().setItem(0, new ItemStack(Material.AIR));
            if (!a.getPlayersAlive().contains(p) && p.getGameMode() != GameMode.SPECTATOR) {
                a.getPlayersAlive().add(p);
            }
            p.teleport(a.getArenaCuboid().getCenter());
        }

        if (a.getPlayers().size() == 1)
            a.setSoloGame(true);

        for (final Player p : a.getPlayers()) {
            this.spawnPlayer(a, p);

            for (String potion : a.getPotionEffects().keySet()) {
                potion = potion.toUpperCase();
                try {
                    p.addPotionEffect(new PotionEffect(XPotion.valueOf(potion).getPotionEffectType(), Integer.MAX_VALUE,
                            a.getPotionEffects().get(potion)));
                } catch (Exception ex) {
                    pl.getDebug().broadcastError("Could not find potion effect for: " + potion);
                }
            }

            if (a.isEnableScoreboard())
                pl.getScoreBoardUtils().addScoreBoard(a, p);
            p.sendTitle(Language.MSG.StartTitle.msg(), Language.MSG.StartUnderTitle.msg(), 10, 80, 10);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        }
        if (a.getRoundToSpawnPowerUp().contains(a.getRound())) {
            if (a.isEnablePowerUps())
                a.getRoundFile().spawnPowerUp(a);
        }

        a.getGiveTerracotaTimer().startCountDown(a);
    }

    public void spawnPlayer(final Arena a, final Player p) {
        final Location loc = a.getDanceFloorCuboid().getRandomLocation();
        loc.setY(a.getDanceFloorCuboid().getHighestY() + 1.0);
        p.teleport(loc);
    }
}
