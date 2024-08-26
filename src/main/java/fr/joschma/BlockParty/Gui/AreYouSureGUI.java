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

public class AreYouSureGUI {
    Inventory inv;

    public void openArenaGui(final Player p, final Arena a) {
        setUpArenaGui(a);
        p.openInventory(inv);
    }

    private void setUpArenaGui(final Arena a) {
        inv = Bukkit.createInventory((InventoryHolder) null, 9,"Confirm deleting " + a.getName());
        for (int i = 0; i < 3; ++i) {
            inv.setItem(i, createIT(XMaterial.GREEN_STAINED_GLASS_PANE.parseMaterial(), " ", ""));
        }

        inv.setItem(4, createIT(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), ChatColor.RED + " ", ""));

        for (int i = 6; i < 9; ++i) {
            inv.setItem(i, createIT(XMaterial.RED_STAINED_GLASS_PANE.parseMaterial(), " ", ""));
        }

        inv.setItem(3, createIT(XMaterial.RED_TERRACOTTA.parseMaterial(), ChatColor.RED + "YES", ChatColor.WHITE + "I want to delete the arena"));
        inv.setItem(5, createIT(XMaterial.GREEN_TERRACOTTA.parseMaterial(), ChatColor.GREEN + "NO", ChatColor.WHITE + "I DON'T want to delete the arena"));
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
