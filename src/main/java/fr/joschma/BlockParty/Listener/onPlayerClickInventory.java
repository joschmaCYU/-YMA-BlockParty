// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Listener;

import com.cryptomorin.xseries.XMaterial;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.JoinArena.JoinArena;
import fr.joschma.BlockParty.Arena.State.SongProvider;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Gui.AreYouSureGUI;
import fr.joschma.BlockParty.Manager.ArenaManager;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

public class onPlayerClickInventory implements Listener {
    BPM pl;
    ArenaManager am;

    public onPlayerClickInventory(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }

    @EventHandler
    public void onPlayerClickInventoryListener(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            final Player p = (Player) e.getWhoClicked();
            final Arena ar = this.am.getArenaPlayer(p);
            final ItemStack it = e.getCurrentItem();

            if (ar != null) {
                e.setCancelled(true);
                if (it != null) {
                    if (e.getCurrentItem().hasItemMeta()) {
                        if(e.getClickedInventory().getSize() == 45) {
                            String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                            final String title = e.getView().getTitle();


                            if (title.equals(Language.MSG.ChoseSongGUI.msg())) {
                                e.setCancelled(true);
                                p.closeInventory();
                                Map<String, Integer> numberOfVote = ar.getSongManager().getNumberOfVoteMap();
                                Map<Player, String> playerVote = ar.getSongManager().getPlayerVote();

                                if (name.equals("RANDOM")) {
                                    Random rand = new Random();
                                    if (ar.getSongProvider() == SongProvider.MCJukebox) {
                                        name = String.valueOf(ar.getLinkToMusic().keySet().toArray()[rand.nextInt(ar.getLinkToMusic().size())]);
                                    } else if (ar.getSongProvider() == SongProvider.NoteBlock) {
                                        name = pl.getChoseSongGUI().getMusicFromPath(ar, rand.nextInt(ar.getPathToMusic().size()));
                                    } else if (ar.getSongProvider() == SongProvider.OpenAudioMC) {
                                        name = String.valueOf(ar.getOpenAudioMusic().keySet().toArray()[rand.nextInt(ar.getOpenAudioMusic().size())]);
                                    }
                                }

                                // player has voted rmv last vote
                                if (playerVote.keySet().contains(p)) {
                                    String lastVotedSong = playerVote.get(p);
                                    numberOfVote.put(lastVotedSong, numberOfVote.get(lastVotedSong) - 1);
                                }

                                int timesVoted = 1;
                                if (numberOfVote.get(name) != null)
                                    timesVoted = numberOfVote.get(name) + 1;
                                numberOfVote.put(name, timesVoted);
                                playerVote.put(p, name);
                                ar.getSongManager().setPlayerSongMap(numberOfVote);
                                ar.getSongManager().setPlayerVote(playerVote);
                                String youHave = Language.MSG.YouHaveVotedForTheSong.msg();

                                if (youHave.contains("%bpm_song_name%")) {
                                    youHave = youHave.replace("%bpm_song_name%", name);
                                }

                                pl.getDebug().msg(p, youHave);
                            }
                        }
                    }
                }
            } else {
                final String title = e.getView().getTitle();
                if (e.getCurrentItem() == null) {
                    return;
                }

                if (e.getCurrentItem().hasItemMeta()) {
                    final String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

                    if(title.equals("Arena Chose GUI")) {
                        e.setCancelled(true);
                        if (e.getCurrentItem().getType() == Material.BOOK) {
                            Random rand = new Random();
                            int randNumb = rand.nextInt(pl.getAm().getArenas().size());
                            JoinArena.joinArena(p, pl.getAm().getArenas().get(randNumb));
                        } else {
                            JoinArena.joinArena(p, pl.getAm().getArena(name));
                        }
                    } else if (e.getClickedInventory().getSize() == 9) {
                        if (title.split(" ").length > 2) {
                            if (am.getArenaNames().contains(title.split(" ")[2])) {
                                e.setCancelled(true);
                                p.closeInventory();
                                if (it.getType() == XMaterial.RED_TERRACOTTA.parseMaterial()) {
                                    p.performCommand("bp delete " + e.getView().getTitle().split(" ")[2]);
                                } else if (it.getType() == XMaterial.GREEN_TERRACOTTA.parseMaterial()) {
                                    p.performCommand("bp " + e.getView().getTitle().split(" ")[2]);
                                }
                                return;
                            }
                        }
                    }

                    final Arena a = this.am.getArena(title.split(" ")[0]);

                    if (a == null) {
                        return;
                    }


                    if (title.equals(a.getName() + " set up")) {
                        e.setCancelled(true);
                        p.closeInventory();

                        if (e.getCurrentItem().getType() == XMaterial.IRON_DOOR.parseMaterial()) {
                            p.performCommand("blockparty:blockparty " + a.getName() + " exitspawn");
                        } else if (e.getCurrentItem().getType() == XMaterial.OAK_DOOR.parseMaterial()) {
                            p.performCommand("blockparty:blockparty " + a.getName() + " lobbySpawn");
                        } else if (e.getCurrentItem().getType() == XMaterial.NOTE_BLOCK.parseMaterial()) {
                            p.performCommand("blockparty:blockparty " + a.getName() + " dancefloorzone");
                        } else if (e.getCurrentItem().getType() == XMaterial.JUKEBOX.parseMaterial()) {
                            p.performCommand("blockparty:blockparty " + a.getName() + " arenazone");
                        } else if (e.getCurrentItem().getType() == XMaterial.OAK_SIGN.parseMaterial()) {
                            p.performCommand("blockparty:blockparty " + a.getName() + " sign add");
                        } else if (e.getCurrentItem().getType() == XMaterial.DARK_OAK_SIGN.parseMaterial()) {
                            p.performCommand("blockparty:blockparty " + a.getName() + " sign remove");
                        } else if (e.getCurrentItem().getType() == XMaterial.PAPER.parseMaterial()) {
                            p.performCommand("blockparty:blockparty " + a.getName() + " finish");
                        } else if (e.getCurrentItem().getType() == XMaterial.MUSIC_DISC_MALL.parseMaterial()) {
                            p.performCommand("blockparty:blockparty " + a.getName() + " songsetting");
                        } else if (e.getCurrentItem().getType() == XMaterial.DISPENSER.parseMaterial()) {
                            p.performCommand("blockparty:blockparty " + a.getName() + " songprovider");
                        } else if (e.getCurrentItem().getType() == XMaterial.BLAZE_ROD.parseMaterial()) {
                            pl.getDebug().msg(p, Language.MSG.TypeCustomDanceFloorName.msg());
                            pl.getDebug().msg(p, "The different custom dance zone are: " + pl.getCustomDanceFloorManager().getCustomDanceFloor().keySet());
                            pl.getTextToAddToCommandManager().getTextToAddToCommand().put(p,
                                    "blockparty:blockparty " + a.getName() + " customDanceFloor add ");
                        } else if (e.getCurrentItem().getType() == XMaterial.BLAZE_POWDER.parseMaterial()) {
                            pl.getDebug().msg(p, Language.MSG.TypeCustomDanceFloorName.msg());
                            pl.getDebug().msg(p, "The different custom dance zone are: " + a.getCustomDanceFloor());
                            pl.getTextToAddToCommandManager().getTextToAddToCommand().put(p,
                                    "blockparty:blockparty " + a.getName() + " customDanceFloor remove ");
                        } else if (e.getCurrentItem().getType() == XMaterial.BARRIER.parseMaterial()) {
                            new AreYouSureGUI().openArenaGui(p, a);
                        }
                    }
                }
            }
        }
    }
}
