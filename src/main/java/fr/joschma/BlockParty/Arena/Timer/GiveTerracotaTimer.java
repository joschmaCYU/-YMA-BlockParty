// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena.Timer;

import com.cryptomorin.xseries.messages.ActionBar;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Messages.Language;
import fr.joschma.BlockParty.Utils.RepeatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.joschma.BlockParty.Utils.RepeatUtils.rfcdMsg;

public class GiveTerracotaTimer {
    int time;
    int i;
    int taskID;

    public GiveTerracotaTimer() {
        this.i = 1;
    }

    public void startCountDown(final Arena a) {
        this.time = a.getWaitTimeBeforeGiveColor();
        if (a.getRound() == 1)
            time = a.getWaitTimeBeforeGiveColorFirstRound();
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        this.taskID = scheduler.scheduleSyncRepeatingTask((Plugin) a.getPl(), (Runnable) new Runnable() {
            @Override
            public void run() {
                if (GiveTerracotaTimer.this.time == 0) {
                    a.getDanceFloorColourGroupActualMaterials().clear();
                    if (a.isUseSameColourGroup()) {
                        for (Material material : useSameColourGroup(a, a.getDanceFloorActualMaterial())) {
                            if (material != a.getDanceFloorActualMaterial()) {
                                a.getDanceFloorColourGroupActualMaterials().add(material);
                            }
                        }
                    }
                    a.getRemoveFloorCountDown().startCountDown(a);

                    if (a.isGiveBlock()) {
                        giveBlock(a);
                    } else {
                        for (final Player p : a.getPlayersAlive()) {
                            a.getPl().getDebug().msg(p, "The color is " + a.getDanceFloorActualMaterial());
                        }
                    }

                    GiveTerracotaTimer.this.stopTimer();
                } else if (GiveTerracotaTimer.this.time > 0) {
                    if (GiveTerracotaTimer.this.i >= 4) {
                        GiveTerracotaTimer.this.i = 1;
                    }
                    for (final Player p : a.getPlayersAlive()) {
                        ActionBar.sendActionBar(p, ChatColor.GRAY + Language.MSG.Waiting.msg(p) + RepeatUtils.repeat(GiveTerracotaTimer.this.i, "."));
                    }

                    ++i;
                    --time;
                }
            }
        }, 0L, 20L);
    }

    //.replace("\u2b1b", " ")
    private void giveBlock(Arena a) {
        ItemStack block = new ItemStack(a.getDanceFloorActualMaterial());
        ItemMeta meta = block.getItemMeta();
        List<Integer> bestPlace = new ArrayList<>();
        bestPlace.addAll(Arrays.asList(5, 6));

        for (final Player p : a.getPlayersAlive()) {
            Material ma = a.getDanceFloorActualMaterial();
            String name = ma.name();

            if (a.isUseSameColourGroup()) {
                if (block.getType().name().contains("LIGHT")) {
                    name = name.replace("LIGHT_", "");
                }
                if (block.getType().name().contains("DARK")) {
                    name = name.replace("DARK_", "");
                }

                block.setType(Material.valueOf(name));

                name = a.getPl().getColourUtils().getBlockColourName(ma);
            }

            meta.setDisplayName(rfcdMsg(p, a, (int) Math.floor(time), name) + ChatColor.BOLD + a.getSuffix_colour());
            block.setItemMeta(meta);
            p.getInventory().setItem(4, block);
        }
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(this.taskID);
        this.time = -1;
    }

    private List<Material> useSameColourGroup(Arena a, Material ma) {
        List<Material> sameBlock = new ArrayList<>();
        String colour = a.getPl().getColourUtils().getBlockColourName(ma);

        if (a.getPl().getColourUtils().isAColour(colour, a)) {
            for (Material material : a.getDanceFloorFloorMaterials()) {
                if (material.name().contains(colour)) {
                    sameBlock.add(material);
                }
            }
        }
        return sameBlock;
    }


}
