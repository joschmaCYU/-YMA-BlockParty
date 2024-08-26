// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena.Timer;

import com.cryptomorin.xseries.messages.ActionBar;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Cuboid.DistributedFiller;
import fr.joschma.BlockParty.Cuboid.WorkloadRunnable;
import fr.joschma.BlockParty.Utils.RepeatUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.ThreadLocalRandom;

import static fr.joschma.BlockParty.Utils.RepeatUtils.rfcdMsg;

public class RemoveFloorCountDown {

    double time;
    int taskID;
    double secondtracker;
    boolean hasToStop;
    DistributedFiller distributedFiller;
    WorkloadRunnable workloadRunnable;

    public void startCountDown(final Arena a) {
        distributedFiller = a.getDistributedFiller();
        workloadRunnable = a.getWorkloadRunnable();
        this.time = a.getRemoveFloorTime();
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        hasToStop = false;
        this.taskID = scheduler.scheduleSyncRepeatingTask((Plugin) a.getPl(), (Runnable) new Runnable() {
            @Override
            public void run() {
                if (hasToStop) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    scheduler.cancelTask(taskID);
                } else if (time <= 0.0) {
                    hasToStop = true;

                    for (final Block bl : a.getDanceFloorCuboid().blockList()) {
                        if (bl.getType() != a.getDanceFloorActualMaterial() && !a.getDanceFloorColourGroupActualMaterials().contains(bl.getType())) {
                            distributedFiller.fillLocation(bl.getLocation(), Material.AIR, workloadRunnable);

                            // make particles
                            if (a.isUseParticles()) {
                                spawnParticles(a, bl);
                            }
                        }
                    }

                    a.getRegenerateBlockTimer().startCountDown(a);
                    stopTimer();
                } else if (time > 0.0) {
                    if (secondtracker >= 1.0) {
                        for (final Player p : a.getPlayersAlive()) {
                            if (!a.isGiveBlock() || a.isShowColorNameInBarTitle()) {
                                ActionBar.sendActionBar(p, rfcdMsg(p, a, (int) Math.floor(time),
                                        a.getDanceFloorActualMaterial().name()));
                            } else {
                                ActionBar.sendActionBar(p, RepeatUtils.rfcdBar(a, (int) Math.floor(time),
                                        a.getDanceFloorActualMaterial()));
                            }

                            if (time < 4 & time >= 3) {
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2.0f, 1.0f);
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 1.0f);
                            } else if (time < 3 & time >= 2) {
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2.0f, 0.8f);
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 0.8f);
                            } else if (time < 2 & time >= 1) {
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2.0f, 0.6f);
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0f, 0.6f);
                            } else if (time < 1 & time >= 0) {
                                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 2.0f, 1.5f);
                            }
                        }
                        secondtracker = 0.1;
                    } else {
                        secondtracker += 0.1;
                    }

                    time -= 0.1;
                }
            }
        }, 0L, 2L);
    }

    private void spawnParticles(Arena a, Block bl) {
        if (a.getParticle() == Particle.REDSTONE) {
            Particle.DustOptions dustOptions = null;
            Location particleLocation = bl.getLocation().clone().add(0.5, 0, 0.5);

            switch (a.getParticleColourSetting()) {
                case RANDOM:
                    dustOptions = new Particle.DustOptions(randomColor(), a.getParticleSize().floatValue());
                    break;
                case REMOVEBLOCKCOLOUR:
                    dustOptions = new Particle.DustOptions(a.getPl().getColourUtils().getBlockColour(a, bl.getType()),
                            a.getParticleSize().floatValue());
                    break;
                case INHANDBLOCKCOLOUR:
                    dustOptions = new Particle.DustOptions(a.getPl().getColourUtils().getBlockColour(a,
                            a.getDanceFloorActualMaterial()), a.getParticleSize().floatValue());
                    break;
            }

            bl.getLocation().getWorld().spawnParticle(a.getParticle(), particleLocation, a.getParticleCount(), dustOptions);

        } else {
            bl.getLocation().getWorld().spawnParticle(a.getParticle(), bl.getLocation().clone().
                    add(0.5, 0, 0.5), a.getParticleCount());
        }
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
        this.time = (int) -1.0;
    }

    public static Color randomColor() {
        ThreadLocalRandom gen = ThreadLocalRandom.current();
        int randR = gen.nextInt(0, 256);
        int randG = gen.nextInt(0, 256);
        int randB = gen.nextInt(0, 256);

        return Color.fromRGB(randR, randG, randB);
    }
}