// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Listener;

import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.XSound;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.ArenaManager;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class onPlayerMove implements Listener {
    BPM pl;
    ArenaManager am;

    public onPlayerMove(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }

    @EventHandler
    public void onPlayerMoveListenerPlayerExit(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final Arena a = this.am.getArenaPlayer(p);
        if (a != null) {
            if (p.getGameMode() == a.getGmOnDeath()) {
                if (!a.getArenaCuboid().isIn(p)) {
                    p.teleport(a.getArenaCuboid().getCenter());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMoveListenerPlayerFall(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final Arena a = this.am.getArenaPlayer(p);
        if (a != null) {
            if (a.getPlayersAlive().contains(p) && p.getLocation().getY() <= a.getYLevelToDie()) {
                if (a.getState() == ArenaState.INGAME) {
                    if (a.isEnableLightnings())
                        p.getWorld().spawnEntity(p.getLocation(), EntityType.LIGHTNING);
                    a.rmvAlivePlayer(p);

                    String msg = ChatColor.YELLOW + e.getPlayer().getDisplayName() + ChatColor.GRAY + " \u27B2 " + Language.MSG.PlayerEliminatedMessage.msg();
                    for (Player players : a.getPlayers()) {
                        this.pl.getDebug().msg(players, msg);
                    }

                    if (p.getGameMode() != GameMode.SPECTATOR) {
                        for (final Player pla : a.getPlayers()) {
                            XSound.ENTITY_LIGHTNING_BOLT_IMPACT.play(pla.getLocation(), 2.0f, 1.0f);
                        }
                        String youHaveFinished = Language.MSG.YouHaveFinishedPlace.msg();
                        if (youHaveFinished.contains("%bpm_place%"))
                            youHaveFinished = youHaveFinished.replace("%bpm_place%", String.valueOf(a.getPlayersAlive().size() + 1));
                        this.pl.getDebug().msg(p, youHaveFinished);
                        p.sendTitle(Language.MSG.YouDiedTitle.msg(), Language.MSG.YouDiedTitleSubtitle.msg(), 10, 60, 10);
                        p.setGameMode(a.getGmOnDeath());

                        if (a.getGmOnDeath() != GameMode.SPECTATOR) {
                            a.leaveGame(p);
                            return;
                        } else {
                            if (a.getArenaCuboid().isIn(a.getLobbySpawn())) {
                                p.teleport(a.getLobbySpawn());
                            } else {
                                p.teleport(a.getArenaCuboid().getCenter());
                            }
                        }

                        a.checkWin();
                    }
                } else if (a.getState() == ArenaState.CLEARING) {
                    if (a.getWinners().contains(p)) {
                        this.pl.getDebug().msg(p, Language.MSG.winnerFallOf.msg(p));
                        a.getStartGame().spawnPlayer(a, p);
                    } else {
                        this.pl.getDebug().msg(p, Language.MSG.playerFallOfInClearing.msg(p));
                        p.teleport(a.getLobbySpawn());
                    }
                }
            }
        }
    }

    public void killPowerUp(Arena a, Player p) {
        final double range = a.getRangeToCatchPowerup();
        for (Entity en : p.getNearbyEntities(range, range, range)) {
            if (en.getType() == EntityType.ARMOR_STAND) {
                if (en.getLocation().equals(a.getPowerUpLoc())) {
                    en.remove();
                }
                /*ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Location loc1 = en.getLocation();
                Bukkit.broadcastMessage("kill");
                Bukkit.dispatchCommand(console, "minecraft:kill @e[type=armor_stand,x=" +
                        loc1.getX() + ",y=" + loc1.getY() + ",z=" + loc1.getZ() + "]");*/
            } else if (a.isPowerUpAsBlock()) {
                a.getPowerUpLoc().getBlock().setType(Material.AIR);
            }

            a.setPowerUpLoc(null);
            return;
        }

    }

    @EventHandler
    public void onPlayerMoveListenerPowerUp(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final Arena a = this.am.getArenaPlayer(p);
        if (a != null) {
            if (p.getGameMode() != GameMode.SPECTATOR && a.getPowerUpLoc() != null) {
                final double range = a.getRangeToCatchPowerup();
                for (Entity en : a.getPowerUpLoc().getWorld().getNearbyEntities(a.getPowerUpLoc(), range, range, range)) {
                    if (en == p) {
                        killPowerUp(a, p);
                        final Random rand = new Random();
                        List<String> powerUps = new ArrayList<String>();
                        powerUps.addAll(a.getPowerUps());

                        if (a.isSoloGame() && powerUps.contains("Player Swap")) {
                            powerUps.remove("Player Swap");
                        }

                        int int_random = rand.nextInt(powerUps.size());
                        String powerUpName = powerUps.get(int_random);

                        if (powerUpName.equalsIgnoreCase("Speed")) {
                            a.clearPotionEffect(p);
                            p.addPotionEffect(new PotionEffect(XPotion.SPEED.getPotionEffectType(), 20 * a.getDurationPowerUp(), a.getLevelOfPowerUp() + 1));
                            Bukkit.getServer().getScheduler().runTaskLater((Plugin) this.pl, (Runnable) new Runnable() {
                                @Override
                                public void run() {
                                    p.addPotionEffect(new PotionEffect(XPotion.SPEED.getPotionEffectType(), Integer.MAX_VALUE, 0));
                                }
                            }, (long) (20 * a.getDurationPowerUp() + 1));
                            powerUpName = Language.MSG.PowerUpSpeed.msg();
                        } else if (powerUpName.equalsIgnoreCase("Jump Boost")) {
                            p.addPotionEffect(new PotionEffect(XPotion.JUMP.getPotionEffectType(), 20 * a.getDurationPowerUp(), a.getLevelOfPowerUp()));
                            powerUpName = Language.MSG.PowerUpJump_Boost.msg();
                        } else if (powerUpName.equalsIgnoreCase("Slow Falling")) {
                            p.addPotionEffect(new PotionEffect(XPotion.SLOW_FALLING.getPotionEffectType(), 20 * a.getDurationPowerUp(), a.getLevelOfPowerUp()));
                            powerUpName = Language.MSG.PowerUpSlow_Falling.msg();
                        } else if (powerUpName.equalsIgnoreCase("Rain")) {
                            ItemStack rainItem = new ItemStack(Material.MAGMA_CREAM);
                            ItemMeta rainMeta = rainItem.getItemMeta();
                            rainMeta.setDisplayName(Language.MSG.ColourRainItemName.msg());
                            rainItem.setItemMeta(rainMeta);
                            p.getInventory().addItem(rainItem);
                            powerUpName = Language.MSG.PowerUpRain.msg();
                        } else if (powerUpName.equalsIgnoreCase("Leap")) {
                            ItemStack leapItem = new ItemStack(Material.FEATHER);
                            ItemMeta leapMeta = leapItem.getItemMeta();
                            leapMeta.setDisplayName(Language.MSG.LeapItemName.msg());
                            leapItem.setItemMeta(leapMeta);
                            p.getInventory().addItem(leapItem);
                            powerUpName = Language.MSG.PowerUpLeap.msg();
                        } else if (powerUpName.equalsIgnoreCase("Random Teleport")) {
                            powerUpName = Language.MSG.PowerUpRandomTP.msg();
                            final Location loc = a.getDanceFloorCuboid().getRandomLocation().add(0, 2, 0);
                            p.teleport(loc);
                        } else if (powerUpName.equalsIgnoreCase("Cow")) {
                            // summon a cow that will follow the player and explode after 5 seconds, setting the blocks around it to ma
                            final Material ma = a.getDanceFloorActualMaterial();

                            // summon a cow next to the player
                            final Location loc = p.getLocation();
                            final Cow cow = (Cow) p.getWorld().spawnEntity(loc, EntityType.COW);
                            cow.setCustomName(Language.MSG.CowName.msg());
                            cow.setCustomNameVisible(true);
                            powerUpName = Language.MSG.CowName.msg();
                            Bukkit.getServer().getScheduler().runTaskLater((Plugin) this.pl, (Runnable) new Runnable() {
                                @Override
                                public void run() {
                                    cow.getWorld().createExplosion(cow.getLocation(), 0.0f, false);
                                    cow.remove();

                                    int cowX = cow.getLocation().getBlockX();
                                    int cowZ = cow.getLocation().getBlockZ();
                                    int cowY = cow.getLocation().getBlockY() - 1;

                                    for (int x = cowX - 2; x <= cowX + 2; x++) {
                                        for (int z = cowZ - 2; z <= cowZ + 2; z++) {
                                            final Location loc = new Location(p.getWorld(), x, cowY, z);
                                            if (loc.getBlock().getType() != Material.AIR) {
                                                if (a.getDanceFloorCuboid().isIn(loc)) {
                                                    loc.getBlock().setType(ma);
                                                }
                                            }
                                        }
                                    }
                                }
                            }, (long) (20 * 2 + 1));
                        } else if (powerUpName.equalsIgnoreCase("Night Vision")) {
                            powerUpName = Language.MSG.PowerUpNightVision.msg();
                            p.addPotionEffect(new PotionEffect(XPotion.NIGHT_VISION.getPotionEffectType(), 20 * a.getDurationPowerUp(), a.getLevelOfPowerUp()));
                        }  else if (powerUpName.equalsIgnoreCase("Player Swap")) {
                            powerUpName = Language.MSG.PowerUpPlayerSwap.msg();
                            Random randPlay = new Random();
                            Player toSwap = a.getPlayers().get(randPlay.nextInt(a.getPlayers().size()));
                            Location locToSwap = toSwap.getLocation();
                            toSwap.teleport(p.getLocation());
                            p.teleport(locToSwap);
                        }

                        final String msg = Language.MSG.gotPowerUp.msg().replace("%bpm_power_up%", ChatColor.GOLD + powerUpName + ChatColor.GRAY);
                        this.pl.getDebug().msg(p, msg);
                        XSound.ENTITY_SILVERFISH_DEATH.play(p.getLocation(), 3.0f, 0.5f);
                        final String msg2 = Language.MSG.PowerUpCollector.msg().replace("%player_name%", p.getName());
                        for (final Player p2 : a.getPlayers()) {
                            if (p2 != p) {
                                this.pl.getDebug().msg(p2, msg2);
                            }
                        }
                    }
                }
            }
        }
    }
}
