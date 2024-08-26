package fr.joschma.BlockParty.Manager;

import com.cryptomorin.xseries.XMaterial;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PlayerHiderManager {

    BPM pl;
    ArenaManager am;

    public PlayerHiderManager(BPM pl) {
        this.pl = pl;
        am = pl.getAm();
    }

    public List<UUID> hidden = new ArrayList<UUID>();

    public List<UUID> getHidden() {
        return hidden;
    }
}

