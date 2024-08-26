// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena.Timer;

import com.cryptomorin.xseries.messages.ActionBar;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class RegenerateBlockTimer {

    int time;
    int taskID;
    RadioSongPlayer rsp;

    public void startCountDown(final Arena a) {
        this.time = a.getRegenerateBlockTime();
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        a.getPl().getMusicManager().stopMusic(a);
        a.getPl().getMusicManager().startStopMusic(a);

        this.taskID = scheduler.scheduleSyncRepeatingTask((Plugin) a.getPl(), (Runnable) new Runnable() {
            @Override
            public void run() {
                if (RegenerateBlockTimer.this.time == 0) {
                    a.getRoundFile().startRound(a);
                    if (a.checkWin()) {
                        return;
                    }

                    stopTimer();
                } else if (RegenerateBlockTimer.this.time > 0) {
                    for (final Player p : a.getPlayersAlive()) {
                        ActionBar.sendActionBar(p, ChatColor.RED + "\u274c " + Language.MSG.Stop.msg(p) + " \u274c");
                    }

                    --time;
                }
            }
        }, 0L, 20L);
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(this.taskID);
        this.time = -1;
    }
}
