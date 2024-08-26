// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Utils;

import fr.joschma.BlockParty.BPM;
import net.md_5.bungee.api.ChatColor;

public class UtilsPrefix
{
    BPM pl;
    
    public UtilsPrefix(final BPM pl) {
        this.pl = pl;
    }
    
    public String getPrefix() {
        final String prefix = this.pl.getConfig().getString("Prefix");
        if (prefix == null) {
            this.setPrefix(ChatColor.GOLD + "[BP]");
        }
        return prefix;
    }
    
    public void setPrefix(final String name) {
        this.pl.getConfig().set("Prefix", (Object)name);
        this.pl.saveConfig();
    }
}
