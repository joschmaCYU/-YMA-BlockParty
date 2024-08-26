// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import fr.joschma.BlockParty.BPM;

public class Debugger
{
    BPM pl;
    
    public Debugger(final BPM pl) {
        this.pl = pl;
    }
    
    public void sysout(final String msg) {
        System.out.println(this.pl.getUtilsPrefix().getPrefix() + " " + ChatColor.RED + msg);
    }
    
    public void sysout(final Player p, final String msg) {
        p.sendMessage(this.pl.getUtilsPrefix().getPrefix() + " " + ChatColor.RED + msg);
        System.out.println(this.pl.getUtilsPrefix().getPrefix() + " " + ChatColor.RED + msg);
    }
    
    public void error(final Player p, final String msg) {
        p.sendMessage(this.pl.getUtilsPrefix().getPrefix() + " " + ChatColor.RED + msg);
    }

    public void broadcastError(final String msg) {
        Bukkit.broadcastMessage(this.pl.getUtilsPrefix().getPrefix() + " " + ChatColor.RED + msg);
    }
    
    public void msg(final Player p, final String msg) {
        p.sendMessage(this.pl.getUtilsPrefix().getPrefix() + " " + ChatColor.GRAY + msg);
    }
}
