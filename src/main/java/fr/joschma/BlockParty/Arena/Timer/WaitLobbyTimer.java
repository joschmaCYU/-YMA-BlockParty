// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena.Timer;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.State.SongSetting;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class WaitLobbyTimer {
    Integer[] t;
    int time;
    int taskID;
    Arena a;

    public WaitLobbyTimer(final Arena a) {
        this.t = new Integer[]{180, 120, 90, 60, 30, 15, 10, 5, 4, 3, 2, 1};
        this.a = a;
    }

    public void startCountDown() {
        this.time = this.a.getLobbyWaitTime();
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        this.taskID = scheduler.scheduleSyncRepeatingTask((Plugin) this.a.getPl(), (Runnable) new Runnable() {
            @Override
            public void run() {
                if (time == 0) {
                    a.getStartGame().startGame(a);
                    stopTimer(a);
                } else if (time > 0) {
                    for (final Player p : a.getPlayers()) {
                        if(a.isResetExp())
                            p.setLevel(time);
                        if(a.isChangeTime())
                            p.getWorld().setTime(20000 + (time * 400L));
                        if (time == 5) {
                            p.sendTitle(ChatColor.YELLOW + "\u277A", "", 10, 20, 10);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 0.6f);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.6f);
                        } else if (time == 4) {
                            p.sendTitle(ChatColor.YELLOW + "\u2779", "", 10, 20, 10);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 0.7f);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.7f);
                        } else if (time == 3) {
                            p.sendTitle(ChatColor.GOLD + "\u2778", "", 10, 20, 10);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 0.8f);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.8f);
                        } else if (time == 2) {
                            p.sendTitle(ChatColor.RED + "\u2777", "", 10, 20, 10);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 0.9f);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.9f);
                        } else if (time == 1) {
                            p.sendTitle(ChatColor.DARK_RED + "\u2776", "", 10, 20, 10);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1.0f);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.0f);
                        }

                        for (final int y : t) {
                            if (y == time) {
                                String waitLobbyTimerString = Language.MSG.WaitLobbyTimer.msg(p);
                                if(waitLobbyTimerString.contains("%bpm_wait_lobby_timer%")) {
                                    waitLobbyTimerString = waitLobbyTimerString.replace("%bpm_wait_lobby_timer%", String.valueOf(time));
                                }
                                a.getPl().getDebug().msg(p, waitLobbyTimerString);
                            }
                        }
                    }
                    final WaitLobbyTimer this$0 = WaitLobbyTimer.this;
                    --this$0.time;
                }
            }
        }, 0L, 20L);
    }

    public void stopTimer(final Arena a) {
        Bukkit.getScheduler().cancelTask(this.taskID);
        this.time = -1;
        for (final Player p : a.getPlayers()) {
            if(a.isResetExp())
                p.setLevel(0);
        }
    }

    public int getTime() {
        return time;
    }
}
