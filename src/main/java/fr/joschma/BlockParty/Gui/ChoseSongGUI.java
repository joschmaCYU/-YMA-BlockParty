package fr.joschma.BlockParty.Gui;

import com.cryptomorin.xseries.XMaterial;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.State.SongProvider;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChoseSongGUI {

    Inventory inv;
    BPM pl;

    public ChoseSongGUI(BPM pl) {
        this.pl = pl;
    }

    public void openChoseSongGui(final Player p, final Arena a) {
        this.setUpChoseSongGui(a);
        p.openInventory(this.inv);
    }

    public String getMusicFromPath(Arena a, int i) {
        String pathToMusic = a.getPathToMusic().get(i);
        String[] parsed = null;

        if (pathToMusic.contains("/")) {
            parsed = a.getPathToMusic().get(i).split("/");
        } else if (pathToMusic.contains("\\")) {
            parsed = a.getPathToMusic().get(i).split("\\\\\\\\");
        }

        pathToMusic = parsed[parsed.length - 1].split("\\.")[0];

        return pathToMusic;
    }

    private void setUpChoseSongGui(final Arena a) {
        this.inv = Bukkit.createInventory((InventoryHolder) null, 45, Language.MSG.ChoseSongGUI.msg());

        inv.setItem(0, this.createIT(XMaterial.MUSIC_DISC_13.parseMaterial(), "RANDOM", ""));

        int y = 689;
        if (a.getSongProvider() == SongProvider.NoteBlock) {
            if (a.getPathToMusic().size() < 45) {
                for (int i = 1; i <= a.getPathToMusic().size(); i++) {
                    if (y == 701)
                        y = 689;

                    if (XMaterial.values()[y].parseMaterial() != null) {
                        String musicName = getMusicFromPath(a, i - 1);
                        int timesVoted = 0;

                        if (a.getSongManager().getNumberOfVoteMap().get(musicName) != null) {
                            timesVoted = a.getSongManager().getNumberOfVoteMap().get(musicName);
                        }

                        inv.setItem(i, createIT(XMaterial.values()[y].parseMaterial(), musicName, ChatColor.GOLD + "Votes: " + ChatColor.GRAY + String.valueOf(timesVoted)));
                    } else {
                        i--;
                    }

                    y++;
                }
            } else {
                for (Player p : a.getPlayers()) {
                    a.getPl().getDebug().error(p, "To many musics ! Stopping");
                }

                a.urgentLeaveGame();
            }
        } else if (a.getSongProvider() == SongProvider.MCJukebox) {
            if (a.getLinkToMusic().size() < 53) {
                int i = 1;

                for (String musicName : a.getLinkToMusic().keySet()) {
                    if (y == 701)
                        y = 689;

                    if (XMaterial.values()[y].parseMaterial() != null) {
                        int timesVoted = 0;

                        if (a.getSongManager().getNumberOfVoteMap().get(musicName) != null) {
                            timesVoted = a.getSongManager().getNumberOfVoteMap().get(musicName);
                        }

                        inv.setItem(i, createIT(XMaterial.values()[y].parseMaterial(), musicName, ChatColor.GRAY + String.valueOf(timesVoted)));
                    }

                    i++;
                    y++;
                }
            } else {
                for (Player p : a.getPlayers()) {
                    a.getPl().getDebug().error(p, "To many musics ! Stopping");
                }

                a.urgentLeaveGame();
            }
        } else if (a.getSongProvider() == SongProvider.OpenAudioMC) {
            if (a.getOpenAudioMusic().size() < 53) {
                int i = 1;

                for (String musicName : a.getOpenAudioMusic().keySet()) {
                    if (y == 701)
                        y = 689;

                    if (XMaterial.values()[y].parseMaterial() != null) {
                        int timesVoted = 0;

                        if (a.getSongManager().getNumberOfVoteMap().get(musicName) != null) {
                            timesVoted = a.getSongManager().getNumberOfVoteMap().get(musicName);
                        }

                        inv.setItem(i, createIT(XMaterial.values()[y].parseMaterial(), musicName, ChatColor.GRAY + String.valueOf(timesVoted)));
                    }

                    i++;
                    y++;
                }
            } else {
                for (Player p : a.getPlayers()) {
                    a.getPl().getDebug().error(p, "To many musics ! Stopping");
                }

                a.urgentLeaveGame();
            }
        }
    }

    public String isSet(final int i) {
        if (i > 0) {
            return ChatColor.GREEN + "Is set";
        }
        return ChatColor.RED + "Not set";
    }

    public String isSet(final boolean bool) {
        if (bool) {
            return ChatColor.GREEN + "Is finished";
        }
        return ChatColor.RED + "Not finished";
    }

    public String isSet(final Object obj) {
        if (obj != null) {
            return ChatColor.GREEN + "Is set";
        }
        return ChatColor.RED + "Not set";
    }

    public ItemStack createIT(final Material ma, final String displayName, final String... lore) {
        final ItemStack it = new ItemStack(ma);
        final ItemMeta im = it.getItemMeta();
        if (displayName != null) {
            im.setDisplayName(displayName);
        }
        if (lore != null) {
            im.setLore((List) Arrays.asList(lore));
        }
        it.setItemMeta(im);
        return it;
    }
}
