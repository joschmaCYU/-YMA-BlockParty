// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Gui;

import com.cryptomorin.xseries.XMaterial;
import fr.joschma.BlockParty.Arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ArenaGui {

    Inventory inv;

    public void openArenaGui(final Player p, final Arena a) {
        this.setUpArenaGui(a);
        p.openInventory(this.inv);
    }

    private void setUpArenaGui(final Arena a) {
        (this.inv = Bukkit.createInventory((InventoryHolder) null, 45, a.getName() + " set up")).setItem(10, this.createIT(XMaterial.NAME_TAG.parseMaterial(), ChatColor.GRAY + "Arena name: " + ChatColor.WHITE + a.getName(), ""));
        for (int i = 0; i < 8; ++i) {
            this.inv.setItem(i, this.createIT(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), " ", ""));
        }

        for (int i = 11; i < 16; i++) {
            inv.setItem(i, this.createIT(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), " ", ""));
        }

        for (int i = 27; i < 32; i++) {
            inv.setItem(i, this.createIT(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), " ", ""));
        }

        inv.setItem(16, this.createIT(XMaterial.DISPENSER.parseMaterial(),
                "Song Provider", ChatColor.GRAY + "The plugin that's provide the song is " + ChatColor.GOLD + a.getSongProvider().toString()));

        for (int i = 8; i < 36; i += 8) {
            this.inv.setItem(i, this.createIT(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), " ", ""));
            ++i;
            this.inv.setItem(i, this.createIT(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), " ", ""));
        }
        for (int i = 36; i < 45; ++i) {
            this.inv.setItem(i, this.createIT(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), " ", ""));
        }

        this.inv.setItem(44, this.createIT(XMaterial.BARRIER.parseMaterial(), ChatColor.RED+ "Delete arena", ""));

        final ItemStack[] its = {this.createIT(XMaterial.IRON_DOOR.parseMaterial(), "Exit Spawn",
                this.isSet(a.getExitSpawn())), this.createIT(XMaterial.OAK_DOOR.parseMaterial(),
                "Lobby Spawn", this.isSet(a.getLobbySpawn())), this.createIT(XMaterial.NOTE_BLOCK.parseMaterial(),
                "Dance Floor Zone", this.isSet(a.getDanceFloorCuboid())),
                this.createIT(XMaterial.JUKEBOX.parseMaterial(), "Arena Zone", this.isSet(a.getArenaCuboid())),
                this.createIT(XMaterial.OAK_SIGN.parseMaterial(), ChatColor.GREEN + "Add" +
                                ChatColor.WHITE + ChatColor.ITALIC + " Sign",
                        this.isSet(a.getSigns().size()), ChatColor.GRAY + "You have link " +
                                ChatColor.GOLD + a.getSigns().size() + ChatColor.GRAY + " to this arenas"),
                this.createIT(XMaterial.BLAZE_ROD.parseMaterial(), ChatColor.GREEN + "Add" +
                                ChatColor.WHITE + ChatColor.ITALIC + " Custom Dance Floor",
                        ChatColor.GRAY + "You have added " + ChatColor.GOLD + a.getCustomDanceFloor().size() +
                                ChatColor.GRAY + " custom dance floors"), this.createIT(XMaterial.PAPER.parseMaterial(),
                "Finished", this.isSet(a.getFinished()))};
        for (int j = 19; j < 26; ++j) {
            this.inv.setItem(j, its[j - 19]);
        }

        inv.setItem(34, this.createIT(XMaterial.MUSIC_DISC_MALL.parseMaterial(),
                "Song Setting", ChatColor.GRAY + "The song setting is set to " + ChatColor.GOLD + a.getSongSetting().toString()));

        inv.setItem(32, createIT(XMaterial.DARK_OAK_SIGN.parseMaterial(), ChatColor.RED + "Remove" +
                ChatColor.WHITE + ChatColor.ITALIC + " Sign", isSet(a.getSigns().size()), ChatColor.GRAY + "You have link "
                + ChatColor.GOLD + a.getSigns().size() + ChatColor.GRAY + " to this arenas"));
        inv.setItem(33, createIT(XMaterial.BLAZE_POWDER.parseMaterial(), ChatColor.RED + "Remove" +
                ChatColor.WHITE + ChatColor.ITALIC + " Custom Dance Floor", ChatColor.GRAY + "You have added " +
                ChatColor.GOLD + a.getCustomDanceFloor().size() + ChatColor.GRAY + " custom dance floors"));
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
