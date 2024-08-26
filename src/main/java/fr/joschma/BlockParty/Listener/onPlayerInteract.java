//
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Listener;

import com.cryptomorin.xseries.XMaterial;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.HubArena;
import fr.joschma.BlockParty.Arena.JoinArena.JoinArena;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Cuboid.Cuboid;
import fr.joschma.BlockParty.Manager.ArenaManager;
import fr.joschma.BlockParty.Manager.CreationZoneManager;
import fr.joschma.BlockParty.Manager.FileManager;
import fr.joschma.BlockParty.Messages.Language;
import fr.joschma.BlockParty.Utils.UtilsLoc;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class onPlayerInteract implements Listener {

    BPM pl;
    ArenaManager am;

    public onPlayerInteract(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }

    @EventHandler
    public void onPlayerInteractListener(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        Arena a = this.am.getArenaPlayer(p);

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null && e.getClickedBlock().getState() instanceof Sign) {
                final Sign sign = (Sign) e.getClickedBlock().getState();

                if (sign != null) {
                    if(pl.isHubServer() && pl.getConfig().getBoolean("useBungeeCord")) {
                        String arenaName = sign.getLine(2);
                        HubArena hubArena = pl.getAm().getHubArena(arenaName);
                        if (hubArena.getMaxPlayer() > hubArena.getCurrentPlayer()) {
                            hubArena.setCurrentPlayer(hubArena.getCurrentPlayer() + 1);
                            pl.sendCustomData(p, "join", arenaName);
                        }
                        return;
                    }

                    for (final Arena ar : this.am.getArenas()) {
                        if (ar.getSigns() != null && ar.getSigns().contains(sign)) {
                            if (ar.getPlayers().contains(p))
                                return;

                            if (!ar.joinParty(p)) {
                                JoinArena.joinArena(p, ar);
                                e.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }

        if (a != null) {
            if (e.getItem() != null) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.getItem().getType() == XMaterial.LIME_DYE.parseMaterial() && e.getItem().getItemMeta().getDisplayName()
                            .equals(ChatColor.GRAY + Language.MSG.PlayerHiderText.msg() + " \u27AF " + ChatColor.GREEN + Language.MSG.PlayerHiderHidden.msg())) {
                        p.sendMessage(org.bukkit.ChatColor.RED + Language.MSG.PlayerHiderHiddenText.msg());
                        for (Player players : a.getPlayersAlive()) {
                            // check if player is the same as the player who clicked the item (to avoid hiding himself)
                            if (players != p)
                                p.hidePlayer(pl, players);
                        }


                        p.getInventory().setItem(7, a.getPinkDye());
                        pl.getPlayerHiderManager().getHidden().add(p.getUniqueId());

                    } else if (e.getItem().getType() == XMaterial.PINK_DYE.parseMaterial() && e.getItem().getItemMeta().getDisplayName()
                            .equals(ChatColor.GRAY + Language.MSG.PlayerHiderText.msg() + " \u27AF " + ChatColor.RED + Language.MSG.PlayerHiderVisible.msg())) {
                        p.sendMessage(ChatColor.GREEN + Language.MSG.PlayerHiderVisibleText.msg());
                        for (Player players : a.getPlayersAlive()) {
                            if (players != p)
                                p.showPlayer(pl, players);
                        }

                        p.getInventory().setItem(7, a.getLimeDye());
                        pl.getPlayerHiderManager().getHidden().remove(p.getUniqueId());
                    } else if (e.getItem().getType() == XMaterial.MAGMA_CREAM.parseMaterial() && e.getItem().getItemMeta().getDisplayName()
                            .equals(Language.MSG.ColourRainItemName.msg())) {
                        p.getInventory().remove(XMaterial.MAGMA_CREAM.parseMaterial());
                        for (int i = 0; i < 100; i++) {
                            final Random rand = new Random();
                            final Location loc = a.getDanceFloorCuboid().getRandomLocation().add(0.0, 20, 0.0);
                            final Snowball sb = (Snowball) loc.getWorld().spawnEntity(loc, EntityType.SNOWBALL);
                            sb.setShooter(p);
                        }
                    } else if (e.getItem().getType() == XMaterial.FEATHER.parseMaterial() && e.getItem().getItemMeta().getDisplayName()
                            .equals(Language.MSG.LeapItemName.msg())) {
                        p.getInventory().remove(XMaterial.FEATHER.parseMaterial());
                        p.setVelocity(p.getLocation().getDirection().multiply(1));
                        p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 1.0f);
                    }
                }
            }
        }

        if (e.getItem() != null && e.getItem().getType() == XMaterial.STICK.parseMaterial() &&
                (CreationZoneManager.getPlayerLocation().containsKey(p))) {
            e.setCancelled(true);
            Location loc1 = null;
            Location loc2 = null;
            List<Location> locations = CreationZoneManager.getPlayerLocation().get(p);

            if (!locations.isEmpty())
                loc1 = CreationZoneManager.getPlayerLocation().get(p).get(0);

            if (e.getClickedBlock() == null)
                return;

            if (loc1 == null && loc2 == null) {
                loc1 = e.getClickedBlock().getLocation();
                locations.add(loc1);
                CreationZoneManager.addPlayerLocation(p, locations);
                pl.getDebug().msg(p, Language.MSG.putFirstLoc.msg(p));
                return;
            } else if (loc1 != null && loc2 == null) {
                loc2 = e.getClickedBlock().getLocation();
                locations.add(loc2);
                CreationZoneManager.addPlayerLocation(p, locations);
                pl.getDebug().msg(p, Language.MSG.putSecondLoc.msg(p));
            }

            CreationZoneManager.rmvPlayerLocation(p);
            Cuboid cu = new Cuboid(loc1, loc2);
            e.getPlayer().getInventory().setItem(0, new ItemStack(XMaterial.AIR.parseItem()));

            if (CreationZoneManager.getCreationZone().containsKey(p)) {
                a = CreationZoneManager.getCreationZone().get(p);
                YamlConfiguration fc = YamlConfiguration.loadConfiguration(a.getFile());

                if (a.getArenaCuboid() != null) {
                    if (a.getArenaCuboid().getLowestY() >= cu.getLowestY()) {
                        if (cu.getPoint1().getY() < cu.getPoint2().getY()) {
                            Location point1 = a.getArenaCuboid().getPoint1();
                            point1.setY(a.getArenaCuboid().getLowestY());
                            cu = new Cuboid(point1, cu.getPoint2());
                            loc1 = point1;
                        } else {
                            Location point2 = a.getArenaCuboid().getPoint2();
                            point2.setY(a.getArenaCuboid().getLowestY());
                            cu = new Cuboid(point2, cu.getPoint2());
                            loc2 = point2;
                        }

                        pl.getDebug().error(p, "The dance floor need to be one block lower then the arena zone. Automatically readjusting!");
                    }
                }

                a.setDanceFloorCuboid(cu);
                CreationZoneManager.rmvCreationZone(p);
                fc.set("Cuboid.Dance.Loc1", UtilsLoc.locToString(loc1));
                fc.set("Cuboid.Dance.Loc2", UtilsLoc.locToString(loc2));

                pl.getDebug().msg(p, Language.MSG.CreatedCustomDanceCuboid.msg(p));

                if (cu.getHeight() > 1) {
                    pl.getDebug().msg(p, "You have created a 3D dance floor");
                } else {
                    pl.getDebug().msg(p, "You have created a flat dance floor");
                }

                FileManager.save(a.getFile(), fc);
            } else if (CreationZoneManager.getCreationZoneArena().containsKey(p)) {
                a = CreationZoneManager.getCreationZoneArena().get(p);
                YamlConfiguration fc = YamlConfiguration.loadConfiguration(a.getFile());

                if (a.getDanceFloorCuboid() != null) {
                    if (a.getDanceFloorCuboid().getLowestY() <= cu.getLowestY()) {
                        if (cu.getPoint1().getY() < cu.getPoint2().getY()) {
                            Location point1 = a.getDanceFloorCuboid().getPoint1();
                            point1.setY(a.getDanceFloorCuboid().getLowestY());
                            cu = new Cuboid(point1, cu.getPoint2());
                            loc1 = point1;
                        } else {
                            Location point2 = a.getDanceFloorCuboid().getPoint2();
                            point2.setY(a.getDanceFloorCuboid().getLowestY());
                            cu = new Cuboid(point2, cu.getPoint2());
                            loc2 = point2;
                        }

                        pl.getDebug().error(p, "The dance floor need to be one block lower then the arena zone. Automatically readjusting!");
                    }
                }

                a.setArenaCuboid(cu);
                a.setYLevelToDie((int) cu.getLowestY());
                CreationZoneManager.rmvCreationZoneArena(p);
                fc.set("Cuboid.Arena.Loc1", UtilsLoc.locToString(loc1));
                fc.set("Cuboid.Arena.Loc2", UtilsLoc.locToString(loc2));

                pl.getDebug().msg(p, Language.MSG.createdArenaCuboid.msg(p));
                FileManager.save(a.getFile(), fc);
            } else if (CreationZoneManager.getCreationDanceZone().containsKey(p)) {
                String name = CreationZoneManager.getCreationDanceZone().get(p);
                CreationZoneManager.getCreationDanceZone().remove(p);

                if (cu.getHeight() > 1) {
                    pl.getDebug().error(p, "You can not make a 3D custom floor! Cancelling the new custom floor");
                    return;
                }

                final File fileDanceFloor = new File(pl.getDataFolder() + File.separator + "CustomDanceFloors", name + ".yml");
                final YamlConfiguration fcDanceFloor = YamlConfiguration.loadConfiguration(fileDanceFloor);
                List<Block> bls = new Cuboid(loc1, loc2).blockList();
                List<Material> material = new ArrayList<Material>();
                List<String> materialName = new ArrayList<String>();

                for (Block bl : bls) {
                    materialName.add(bl.getType().toString());
                    material.add(bl.getType());
                }

                fcDanceFloor.set("Material", materialName);
                fcDanceFloor.set("Name", name);

                FileManager.save(fileDanceFloor, fcDanceFloor);

                pl.getCustomDanceFloorManager().getCustomDanceFloor().put(name, material);

                this.pl.getDebug().msg(p, Language.MSG.CreatedCustomDanceCuboid.msg(p));
            }

            loc1 = null;
            loc2 = null;
        }

        if (a != null) {
            e.setCancelled(true);
        } else {
            return;
        }

        if (e.getItem() != null && e.getItem().getItemMeta() != null && e.getItem().getType() == XMaterial.SLIME_BALL.parseMaterial()) {
            //if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
            if (pl.getConfig().getBoolean("useBungeeCord")) {
                pl.sendCustomData(p, "leave", a.getName());
            } else {
                a.leaveGame(p);
            }
        }

        if (e.getItem() != null) {
            ItemStack it = e.getItem();
            if (it.getType() == XMaterial.PAPER.parseMaterial()) {
                if (a.getState() == ArenaState.WATTING || a.getState() == ArenaState.CLEARED) {
                    if (p.hasPermission("BlockParty.ChoseSong")) {
                        pl.getChoseSongGUI().openChoseSongGui(p, a);
                    } else {
                        pl.getDebug().error(p, Language.MSG.noPermission.msg());
                    }
                }
            }
        }
    }
}
