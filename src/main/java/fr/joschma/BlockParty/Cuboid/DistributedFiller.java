package fr.joschma.BlockParty.Cuboid;

import org.bukkit.Location;
import org.bukkit.Material;

public class DistributedFiller {

    public void fillAll(Cuboid cu, Material material, WorkloadRunnable workloadRunnable) {
        Location max = cu.getMaxBlock();
        Location min = cu.getMinBlock();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    PlacableBlock placableBlock = new PlacableBlock(cu.getPoint1().getWorld().getUID(), x, y, z, material);
                    workloadRunnable.addWorkload(placableBlock);
                }
            }
        }
    }

    public void fillLocation(Location loc, Material material, WorkloadRunnable workloadRunnable) {
        if (material == null)
                return;
        workloadRunnable.addWorkload(new PlacableBlock(loc.getWorld().getUID(), loc.getBlockX(), loc.getBlockY(),
                loc.getBlockZ(), material));
    }
}
