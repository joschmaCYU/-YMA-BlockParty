// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena;

import com.cryptomorin.xseries.XSound;
import fr.joschma.BlockParty.Arena.End.End;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Cuboid.Cuboid;
import fr.joschma.BlockParty.Cuboid.DistributedFiller;
import fr.joschma.BlockParty.Cuboid.WorkloadRunnable;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Round {
    BPM pl;

    public Round(final BPM pl) {
        this.pl = pl;
    }

    public void startRound(final Arena a) {
        if (a.checkWin()) {
            return;
        }

        if (a.getPowerUpLoc() != null) {
            for (final Entity en : a.getPowerUpLoc().getWorld().getNearbyEntities(a.getPowerUpLoc(), 0.1, 0.1, 0.1)) {
                if (en.getType() == EntityType.ARMOR_STAND) {
                    en.remove();
                }
            }
        }

        if (a.getRound() == a.getMaxNumberOfRound()) {
            End.noMoreRoundWin(a);
            return;
        }

        a.setRound(a.getRound() + 1);

        for (final Player p : a.getPlayers()) {
            if (a.isGiveBlock()) {
                for (int i = 0; i < 9; i++) {
                    if (p.getInventory().getItem(i) != null) {
                        Material ma = p.getInventory().getItem(i).getType();
                        if (a.getDanceFloorColourGroupActualMaterials().contains(ma) || a.getDanceFloorActualMaterial() == ma) {
                            p.getInventory().setItem(i, new ItemStack(Material.AIR));
                        }
                    }
                }
            }
            if (a.isResetExp())
                p.setLevel(0);
            if (a.isEnableScoreboard())
                pl.getScoreBoardUtils().addScoreBoard(a, p);
        }

        this.generateDF(a);

        if (a.getRoundToReduceTime().contains(a.getRound())) {
            a.setRemoveFloorTime(a.getRemoveFloorTime() - a.getSecondsToRemoveFromRemoveFloorTime());
            if (a.getRemoveFloorTime() <= 0.0) {
                a.urgentLeaveGame();
                for (final Player p2 : a.getPlayers()) {
                    this.pl.getDebug().sysout(p2, Language.MSG.removeFloorTimeNegative.msg(p2));
                }
            }
        }
        if (a.getRoundToSpawnPowerUp().contains(a.getRound())) {
            this.spawnPowerUp(a);
        }

        a.getPl().getMusicManager().stopStopMusic(a);
        a.getPl().getMusicManager().startMusic(a);

        a.getGiveTerracotaTimer().startCountDown(a);
    }

    public void generate3DDanceFloor(Arena a, DistributedFiller distributedFiller, WorkloadRunnable workloadRunnable) {
        final Cuboid cu = a.getDanceFloorCuboid();

        if (a.getSavedDanceFloorMaterialMap().isEmpty()) {
            for (Block bl : cu.blockList()) {
                if (bl.getType() != Material.AIR) {
                    a.getSavedDanceFloorMaterialMap().computeIfAbsent(bl.getType(), k -> new ArrayList<>()).add(bl.getLocation());
                }
            }
        }

        for (Material ma : a.getSavedDanceFloorMaterialMap().keySet()) {
            Material randMa = randomFloorMa(a);

            for (Location loc : a.getSavedDanceFloorMaterialMap().get(ma)) {
                distributedFiller.fillLocation(loc, randMa, workloadRunnable);
            }
        }
    }

    public void generateCustomDanceFloor(Arena a, int randomPattern, DistributedFiller distributedFiller, WorkloadRunnable workloadRunnable) {
        final String customDanceFloorName = a.getCustomDanceFloor().get(randomPattern - 4);
        final List<Block> bls = a.getDanceFloorCuboid().blockList();
        final List<Material> customBls = pl.getCustomDanceFloorManager().getCustomDanceFloor().get(customDanceFloorName);

        Map<Material, Material> randomMaterialMap = createRandomMaterialMap(a, customBls);

        for (int i = 0; i < Math.min(customBls.size(), bls.size()); i++) {
            Material ma = customBls.get(i);
            Material mappedMaterial = randomMaterialMap.get(ma);
            distributedFiller.fillLocation(bls.get(i).getLocation(), mappedMaterial, workloadRunnable);
        }
    }

    // randomizes the colours
    private Map<Material, Material> createRandomMaterialMap(Arena a, List<Material> customBls) {
        Map<Material, Material> randomMaterialMap = new HashMap<>();
        Set<Material> usedMaterials = new HashSet<>();

        for (Material ma : customBls) {
            if (!randomMaterialMap.containsKey(ma)) {
                if (ma == Material.AIR || !a.isRandomizeCustomFloor() || usedMaterials.size() >= a.getDanceFloorFloorMaterials().size()) {
                    randomMaterialMap.put(ma, ma);
                } else {
                    Material randMa = randomFloorMa(a);

                    while (randomMaterialMap.containsValue(randMa) || usedMaterials.contains(randMa)) {
                        randMa = randomFloorMa(a);
                    }

                    randomMaterialMap.put(ma, randMa);
                    usedMaterials.add(randMa);
                }
            }
        }

        return randomMaterialMap;
    }

    public void generateRandomPattern(Arena a, int randomPattern, DistributedFiller distributedFiller, WorkloadRunnable workloadRunnable) {
        final Random rand = new Random();
        final Cuboid cu = a.getDanceFloorCuboid();
        final Location loc = cu.getPoint1();

        switch (randomPattern) {
            // Random
            case 0: {
                for (Block bl : cu.blockList()) {
                    Material ma = randomFloorMa(a);
                    distributedFiller.fillLocation(bl.getLocation(), ma, workloadRunnable);
                }
                break;
            }
            case 1: {
                // Line
                List<Material> masToPlace = new ArrayList<>();
                for (int y = 0; y < cu.getXWidth(); y++) {
                    masToPlace.add(randomFloorMa(a));
                }

                int randomLine = rand.nextInt(cu.getXWidth());
                masToPlace.set(randomLine, a.getDanceFloorActualMaterial());

                for (int x = loc.getBlockX(); x < loc.getBlockX() + cu.getXWidth(); x++) {
                    for (int z = loc.getBlockZ(); z < loc.getBlockZ() + cu.getZWidth(); z++) {
                        int i = x - loc.getBlockX();
                        Material material = masToPlace.get(i);
                        distributedFiller.fillLocation(loc.getWorld().getBlockAt(x, loc.getBlockY(), z).getLocation(),
                                material, workloadRunnable);
                    }
                }
                break;
            }
            case 2: {
                // Damier ?
                List<Material> masToPlace2 = new ArrayList<>();
                for (int y2 = 0; y2 < cu.getZWidth(); y2++) {
                    masToPlace2.add(randomFloorMa(a));
                }

                int randomLine2 = rand.nextInt(cu.getZWidth());
                masToPlace2.set(randomLine2, a.getDanceFloorActualMaterial());

                for (int z2 = loc.getBlockZ(); z2 < loc.getBlockZ() + cu.getZWidth(); z2++) {
                    for (int x2 = loc.getBlockX(); x2 < loc.getBlockX() + cu.getXWidth(); x2++) {
                        distributedFiller.fillLocation(loc.getWorld().getBlockAt(x2, loc.getBlockY(), z2).getLocation(),
                                randomFloorMa(a), workloadRunnable);
                    }
                }
                break;
            }
            case 3: {
                // Damier ?
                final Location loc1 = cu.getPoint1();
                loc1.add(1.0, 0.0, 1.0);

                for (int z3 = loc1.getBlockZ(); z3 < loc1.getBlockZ() + cu.getZWidth(); z3 += 2) {
                    for (int x3 = loc1.getBlockX(); x3 < loc1.getBlockX() + cu.getXWidth(); x3 += 2) {

                        Block bl2 = loc1.getWorld().getBlockAt(x3, loc1.getBlockY(), z3);
                        for (int x4 = bl2.getLocation().getBlockX() - 1; x4 < bl2.getLocation().getBlockX() + 1; x4++) {
                            for (int z4 = bl2.getLocation().getBlockZ() - 1; z4 < bl2.getLocation().getBlockZ() + 1; z4++) {
                                Location blockLocation = loc1.getWorld().getBlockAt(x4, loc1.getBlockY(), z4).getLocation();
                                if (cu.isIn(blockLocation)) {
                                    distributedFiller.fillLocation(blockLocation, randomFloorMa(a), workloadRunnable);
                                }
                            }
                        }
                    }
                }

                List<Material> masToPlace3 = new ArrayList<>();
                for (int y3 = 0; y3 < cu.getZWidth(); y3++) {
                    masToPlace3.add(randomFloorMa(a));
                }

                int randX = rand.nextInt(cu.getXWidth() / 2);
                int randZ = rand.nextInt(cu.getZWidth() / 2);
                Location location = loc1.clone().add(randX * 2, 0, randZ * 2);

                for (int x4 = location.getBlockX() - 1; x4 < location.getBlockX() + 1; x4++) {
                    for (int z4 = location.getBlockZ() - 1; z4 < location.getBlockZ() + 1; z4++) {
                        distributedFiller.fillLocation(location.getWorld().getBlockAt(x4, location.getBlockY(), z4).getLocation(),
                                randomFloorMa(a), workloadRunnable);
                    }
                }
                break;
            }
        }
    }

    public void generateDF(final Arena a) {
        DistributedFiller distributedFiller = a.getDistributedFiller();
        WorkloadRunnable workloadRunnable = a.getWorkloadRunnable();
        final Random rand = new Random();
        int randomPather = rand.nextInt(4 + a.getCustomDanceFloor().size());

        // 3d
        if (a.getDanceFloorCuboid().getHeight() > 1) {
            generate3DDanceFloor(a, distributedFiller, workloadRunnable);
        } else {// flat
            if (a.isOnlyCustomFloors()) {
                randomPather = rand.nextInt(a.getCustomDanceFloor().size()) + 4;
            }

            // preselected pattern
            if (randomPather > 3) {
                generateCustomDanceFloor(a, randomPather, distributedFiller, workloadRunnable);
            } else {
                // random pattern
                generateRandomPattern(a, randomPather, distributedFiller, workloadRunnable);
            }
        }

        a.findASetDanceFloorActualMaterial();
    }

    public void generateWF(final Arena a) {
        DistributedFiller distributedFiller = a.getDistributedFiller();
        WorkloadRunnable workloadRunnable = a.getWorkloadRunnable();

        final Random rand = new Random();
        final Cuboid cu = a.getDanceFloorCuboid();
        final List<Material> masToPlace1 = new ArrayList<Material>();

        if (cu.getHeight() > 1 || !a.isUseSameColourGroup()) {
            for (int i = 0; i < a.getSavedDanceFloorMaterialMap().keySet().size(); i++) {
                Material ma = (Material) a.getSavedDanceFloorMaterialMap().keySet().toArray()[i];
                if (ma != Material.AIR) {
                    for (Location loc : a.getSavedDanceFloorMaterialMap().get(ma)) {
                        //BlockChanger.setBlock(loc, randomFloorMa(a));
                        distributedFiller.fillLocation(loc, randomFloorMa(a), workloadRunnable);
                    }
                }
            }
        } else {
            for (int y = 0; y < cu.getZWidth(); ++y) {
                masToPlace1.add(this.randomFloorMa(a));
            }
            if (!masToPlace1.contains(a.getDanceFloorActualMaterial())) {
                final int randomLine = rand.nextInt(cu.getZWidth());
                masToPlace1.set(randomLine, a.getDanceFloorActualMaterial());
            }
            final Location loc1 = cu.getPoint1();
            int i1 = 0;
            for (int z = loc1.getBlockZ(); z < loc1.getBlockZ() + cu.getZWidth(); ++z) {
                for (int x = loc1.getBlockX(); x < loc1.getBlockX() + cu.getXWidth(); ++x) {
                    if (masToPlace1.get(i1) != null) {
                        distributedFiller.fillLocation(loc1.getWorld().getBlockAt(x, loc1.getBlockY(), z).getLocation(),
                                masToPlace1.get(i1), workloadRunnable);
                        //BlockChanger.setBlock(loc1.getWorld().getBlockAt(x, loc1.getBlockY(), z).getLocation(), masToPlace1.get(i1));
                    }
                }
                ++i1;
            }
        }
    }

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

    public void spawnPowerUp(final Arena a) {
        final Location loc = a.getDanceFloorCuboid().getRandomLocation();
        loc.setY(a.getDanceFloorCuboid().getHighestY() + 1.0);
        a.setPowerUpLoc(loc);

        if (a.isPowerUpAsBlock()) {
            loc.getBlock().setType(a.getPowerUpHead());
        } else {
            final ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            as.setGravity(false);
            as.setVisible(false);
            as.setCanPickupItems(false);
            as.setCustomName(a.getPowerUpName());
            as.setCustomNameVisible(true);

            if (a.isUseCustomHeadLink()) {
                // TODO to remove the warn
                // https://blog.jeff-media.com/creating-custom-heads-in-spigot-1-18-1/
                as.getEquipment().setHelmet(a.getPl().getCustomHeadUtils().getCustomSkull(a.getCustomHeadLink()));
            } else {
                as.getEquipment().setHelmet(new ItemStack(a.getPowerUpHead()));
            }
        }

        for (final Player p : a.getPlayersAlive()) {
            XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(p.getLocation(), 1.0f, 0.5f);
            String msg = ChatColor.YELLOW + "\u2739 " + Language.MSG.PowerUpSpawned.msg();

            String msg2 = Language.MSG.PowerUpCollect.msg();
            this.pl.getDebug().msg(p, msg);
            this.pl.getDebug().msg(p, msg2);
        }
    }
}
