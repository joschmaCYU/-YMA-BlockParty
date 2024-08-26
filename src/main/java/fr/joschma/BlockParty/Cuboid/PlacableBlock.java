package fr.joschma.BlockParty.Cuboid;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.UUID;

public class PlacableBlock {

    UUID worldID;
    int blockX;
    int blockY;
    int blockZ;
    Material material;

    public PlacableBlock(UUID worldID, int blockX, int blockY, int blockZ, Material material) {
        this.worldID = worldID;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.material = material;
    }

    public void compute() {
        World world = Bukkit.getWorld(worldID);
        world.getBlockAt(blockX, blockY, blockZ).setType(material);
    }
}
