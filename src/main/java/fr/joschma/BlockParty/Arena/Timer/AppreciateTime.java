// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena.Timer;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.Cuboid.DistributedFiller;
import fr.joschma.BlockParty.Cuboid.WorkloadRunnable;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.Random;

public class AppreciateTime {
    int time;
    int taskID;

    public Material randomFloorMa(final Arena a) {
        final Random rand = new Random();
        int int_random = rand.nextInt(a.getDanceFloorFloorMaterials().size());
        Material ma = a.getDanceFloorFloorMaterials().get(int_random);

        while (ma == null) {
            int_random = rand.nextInt(a.getDanceFloorFloorMaterials().size());
            ma = a.getDanceFloorFloorMaterials().get(int_random);
        }

        return ma;
    }

    public void startCountDown(final Arena a) {
        a.getGiveTerracotaTimer().stopTimer();
        a.getRegenerateBlockTimer().stopTimer();
        a.getRemoveFloorCountDown().stopTimer();
        a.getWaitLobbyTimer().stopTimer(a);
        a.getRoundFile().generateWF(a);
        a.getPl().getMusicManager().stopMusic(a);
        a.getPl().getMusicManager().stopStopMusic(a);
        a.setState(ArenaState.CLEARING);
        if (a.isEnableScoreboard()) {
            for (final Player p : a.getPlayers()) {
                a.getPl().getScoreBoardUtils().rmvScoreBoard(p);
            }
        }

        if (!a.getWinDanceFloor().equalsIgnoreCase("none")) {
            // load wait dance floor
            DistributedFiller distributedFiller = a.getDistributedFiller();
            WorkloadRunnable workloadRunnable = a.getWorkloadRunnable();
            List<Block> bls = a.getDanceFloorCuboid().blockList();
            List<Material> customBls = a.getPl().getCustomDanceFloorManager().getCustomDanceFloor().get(a.getWinDanceFloor());

            if (bls.size() != customBls.size()) {
                a.getPl().getDebug().broadcastError("Win dance floor and the arena dance floor are not the same size");
                a.getPl().getDebug().broadcastError("Could not place win dance floor");
            } else {
                for (int i = 0; i < customBls.size(); i++) {
                    distributedFiller.fillLocation(bls.get(i).getLocation(), customBls.get(i), workloadRunnable);
                }
            }
        }

        a.clearPowerUps();

        this.time = a.getAppreciateTime();
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        this.taskID = scheduler.scheduleSyncRepeatingTask((Plugin) a.getPl(), (Runnable) new Runnable() {
            @Override
            public void run() {
                if (AppreciateTime.this.time == 0) {
                    a.clear();
                    AppreciateTime.this.stopTimer();
                } else if (AppreciateTime.this.time > 0) {
                    AppreciateTime.this.spawnConfetti(a);
                    Bukkit.getServer().getScheduler().runTaskLater((Plugin) a.getPl(), (Runnable) new Runnable() {
                        @Override
                        public void run() {
                            if (a.isEnableFireworksOnWin())
                                AppreciateTime.this.spawnConfetti(a);
                        }
                    }, 10L);
                    final AppreciateTime this$0 = AppreciateTime.this;
                    --this$0.time;
                }
            }
        }, 0L, 20L);
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(this.taskID);
        this.time = -1;
    }

    private void spawnConfetti(final Arena a) {
        final Random rand = new Random();
        final int int_random = rand.nextInt(8);
        final Location loc = a.getDanceFloorCuboid().getRandomLocation().add(0.0, (double) int_random, 0.0);
        final Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        final FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))).flicker(true).build());
        fw.setFireworkMeta(fwm);
        fw.detonate();
        final Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        fw2.setFireworkMeta(fwm);
    }
}
