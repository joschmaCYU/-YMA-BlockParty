// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Utils;

import fr.joschma.BlockParty.BPM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class UtilsLoc {
    public static Location stringToLoc(final String stringLoc, BPM pl) {
        if (stringLoc != null && stringLoc.contains("/")) {
            final String[] locs = stringLoc.split("/");
            Location loc = null;
            World world = Bukkit.getServer().getWorld(locs[0]);
            // Wait for world to load ?
            if (world == null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                    }
                }.runTaskLater(pl, 20);
            }
            
            if (world != null) {
                loc = new Location(world, 
                        Double.parseDouble(locs[1]), Double.parseDouble(locs[2]), Double.parseDouble(locs[3]), 
                        Float.parseFloat(locs[4]), Float.parseFloat(locs[5]));
            } else {
                pl.getDebug().sysout(ChatColor.RED + "Error while loading World: " + locs[0] + ". Try to install Multiverse-Core to fix the issue");
            }
            return loc;
        }
        return null;
    }

    public static String locToString(final Location loc) {
        String str = null;

        if (loc != null) {
            str = loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ() + "/" + loc.getYaw() + "/" + loc.getPitch();
        }

        return str;
    }
}
