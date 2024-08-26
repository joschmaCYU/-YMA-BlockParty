package fr.joschma.BlockParty.Gui;

import com.cryptomorin.xseries.XMaterial;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class JoinArenaGui {

    Inventory inv;
    BPM pl;

    public JoinArenaGui(BPM pl) {
        this.pl = pl;
    }

    public void openArenaGui(final Player p) {
        this.setUpArenaGui(p);
        p.openInventory(this.inv);
    }

    private void setUpArenaGui(final Player p) {
        inv = Bukkit.createInventory(null, 54, "Arena Chose GUI");
        inv.setItem(0, createIT(XMaterial.BOOK.parseMaterial(), ChatColor.GRAY + "Random",
                ChatColor.GOLD + "Join randomly an arena"));

        int arSize = pl.getAm().getArenas().size();

        if (arSize >= 55) {
            pl.getDebug().error(p, "To many arenas! Can not load the arena GUI");
            return;
        }

        int y = 689;
        for (int i = 0; i < arSize; i++) {
            if (y == 701)
                y = 689;
            inv.setItem(i+1, createIT(XMaterial.values()[y].parseMaterial(), pl.getAm().getArenas().get(i).getName()));
        }
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
