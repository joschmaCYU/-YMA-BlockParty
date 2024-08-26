// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Manager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager
{
    Map<Player, ItemStack[]> contentMap;
    Map<Player, ItemStack[]> armorMap;
    Map<Player, Integer> lvlMap;
    Map<Player, Float> expMap;
    
    public InventoryManager() {
        this.contentMap = new HashMap<Player, ItemStack[]>();
        this.armorMap = new HashMap<Player, ItemStack[]>();
        this.lvlMap = new HashMap<Player, Integer>();
        this.expMap = new HashMap<Player, Float>();
    }
    
    public void saveInventory(final Player p) {
        this.contentMap.put(p, p.getInventory().getContents());
        this.armorMap.put(p, p.getInventory().getArmorContents());
        this.lvlMap.put(p, p.getLevel());
        this.expMap.put(p, p.getExp());
    }
    
    public void loadInventory(final Player p) {
        p.getInventory().setContents((ItemStack[])this.contentMap.get(p));
        p.getInventory().setArmorContents((ItemStack[])this.armorMap.get(p));
        p.setLevel(lvlMap.get(p));
        p.setExp(expMap.get(p));
    }
}
