package fr.joschma.BlockParty.Utils;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class CustomHeadUtils {
//http://textures.minecraft.net/texture/ac4970ea91ab06ece59d45fce7604d255431f2e03a737b226082c4cce1aca1c4
    public ItemStack getCustomSkull(String url) {
        Field profileField = null;

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), "null");
        byte[] data = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(data)));

        try {
            if (profileField == null) {
                profileField = meta.getClass().getDeclaredField("profile");
            }

            profileField.setAccessible(true);
            profileField.set(meta, profile);

            item.setItemMeta(meta);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }
}
