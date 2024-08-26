// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Utils;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.FileManager;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepeatUtils {

    final static Pattern pattern = Pattern.compile("#([A-Fa-f0-9]{6})");

    public static String format(Arena a, String msg) {
        if (a.getPl().getVersion() > 15) {
            Matcher matcher = pattern.matcher(msg);

            while (matcher.find()) {
                String color = msg.substring(matcher.start(), matcher.end());
                msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                matcher = pattern.matcher(msg);
            }
        }

        return msg;
    }

    public static String repeat(final int count, final String with) {
        return new String(new char[count]).replace("\u0000", with);
    }

    public static String rfcdMsg(final Player p, final Arena a, final int i, String materialName) {
        File file = FileManager.load("ColourLanguage");
        YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
        BPM pl = a.getPl();

        if (materialName.contains("BLACK")) {
            return ChatColor.BLACK + repeat(i, "\u2b1b") + ChatColor.BLACK + "" + ChatColor.BOLD + pl.msg(p, fc.getString("BLACK")) + ChatColor.RESET + ChatColor.BLACK + repeat(i, "\u2b1b");
        }
        if (materialName.contains("LIGHT_BLUE")) {
            return  format(a, "#ADD8E6" + repeat(i, "\u2b1b") + ChatColor.BOLD + pl.msg(p, fc.getString("LIGHT_BLUE")) + ChatColor.RESET + format(a, "#ADD8E6" + repeat(i, "\u2b1b")));
        }
        if (materialName.contains("BLUE")) {
            return ChatColor.BLUE + repeat(i, "\u2b1b") + ChatColor.BLUE + "" + ChatColor.BOLD + pl.msg(p, fc.getString("BLUE")) + ChatColor.RESET + ChatColor.BLUE + repeat(i, "\u2b1b");
        }
        if (materialName.contains("BROWN")) {
            return format(a, "#42280E" + repeat(i, "\u2b1b") + ChatColor.BOLD + pl.msg(p, fc.getString("BROWN")) + ChatColor.RESET + format(a, "#42280E" + repeat(i, "\u2b1b")));
        }
        if (materialName.contains("CYAN")) {
            return format(a, "#00FFFF" + repeat(i, "\u2b1b") + ChatColor.BOLD + pl.msg(p, fc.getString("CYAN")) + ChatColor.RESET + format(a, "#00FFFF" + repeat(i, "\u2b1b")));
        }
        if (materialName.contains("LIGHT_GRAY")) {
            return ChatColor.GRAY + repeat(i, "\u2b1b") + ChatColor.GRAY + "" + ChatColor.BOLD + pl.msg(p, fc.getString("LIGHT_GRAY")) + ChatColor.RESET + ChatColor.GRAY + repeat(i, "\u2b1b");
        }
        if (materialName.contains("GRAY")) {
            return ChatColor.DARK_GRAY + repeat(i, "\u2b1b") + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + pl.msg(p, fc.getString("GRAY")) + ChatColor.RESET + ChatColor.DARK_GRAY + repeat(i, "\u2b1b");
        }
        if (materialName.contains("GREEN")) {
            return ChatColor.DARK_GREEN + repeat(i, "\u2b1b") + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + pl.msg(p, fc.getString("GREEN")) + ChatColor.RESET + ChatColor.DARK_GREEN + repeat(i, "\u2b1b");
        }
        if (materialName.contains("LIME")) {
            return ChatColor.GREEN + repeat(i, "\u2b1b") + ChatColor.GREEN + "" + ChatColor.BOLD + pl.msg(p, fc.getString("LIME")) + ChatColor.RESET + ChatColor.GREEN + repeat(i, "\u2b1b");
        }
        if (materialName.contains("MAGENTA")) {
            return ChatColor.LIGHT_PURPLE + repeat(i, "\u2b1b") + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + pl.msg(p, fc.getString("MAGENTA")) + ChatColor.RESET + ChatColor.LIGHT_PURPLE + repeat(i, "\u2b1b");
        }
        if (materialName.contains("ORANGE")) {
            return format(a, "#FFA500" + repeat(i, "\u2b1b") + ChatColor.BOLD + pl.msg(p, fc.getString("ORANGE")) + ChatColor.RESET + format(a, "#FFA500" + repeat(i, "\u2b1b")));
        }
        if (materialName.contains("PINK")) {
            return format(a, "#FFC0CB" + repeat(i, "\u2b1b") + ChatColor.BOLD + pl.msg(p, fc.getString("PINK")) + ChatColor.RESET + format(a, "#FFC0CB" + repeat(i, "\u2b1b")));
        }
        if (materialName.contains("PURPLE")) {
            return ChatColor.DARK_PURPLE + repeat(i, "\u2b1b") + ChatColor.DARK_PURPLE + "" + pl.msg(p, ChatColor.BOLD + fc.getString("PURPLE")) + ChatColor.RESET + ChatColor.DARK_PURPLE + repeat(i, "\u2b1b");
        }
        if (materialName.contains("RED")) {
            return ChatColor.RED + repeat(i, "\u2b1b") + ChatColor.RED + "" + ChatColor.BOLD + pl.msg(p, fc.getString("RED")) + ChatColor.RESET + ChatColor.RED + repeat(i, "\u2b1b");
        }
        if (materialName.contains("YELLOW")) {
            return ChatColor.YELLOW + repeat(i, "\u2b1b") + ChatColor.YELLOW + "" + ChatColor.BOLD + pl.msg(p, fc.getString("YELLOW")) + ChatColor.RESET + ChatColor.YELLOW + repeat(i, "\u2b1b");
        }
        if (materialName.contains("WHITE")) {
            return ChatColor.WHITE + repeat(i, "\u2b1b") + ChatColor.WHITE + "" + ChatColor.BOLD + pl.msg(p, fc.getString("WHITE")) + ChatColor.RESET + ChatColor.WHITE + repeat(i, "\u2b1b");
        }
        return ChatColor.GOLD + repeat(i, "\u2b1b") + ChatColor.WHITE + "" + ChatColor.BOLD + " " + pl.msg(p, fc.getString("DEFAULT")) + " " + ChatColor.RESET + ChatColor.GOLD + repeat(i, "\u2b1b");
    }

    public static String rfcdBar(final Arena a, final int i, Material actualMaterial) {
        if(a.isNoTitleBar())
            return "";

        if (actualMaterial.toString().contains("BLACK")) {
            return ChatColor.BLACK + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.BLACK + repeat(i, "\u2b1b");
        }
        if (actualMaterial.toString().contains("LIGHT_BLUE")) {
            return format(a, "#ADD8E6" + repeat(i, "\u2b1b")) + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + format(a, "#ADD8E6" + repeat(i, "\u2b1b"));
        }
        if (actualMaterial.toString().contains("BLUE")) {
            return format(a,ChatColor.BLUE + repeat(i, "\u2b1b")) + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + format(a,ChatColor.BLUE + repeat(i, "\u2b1b"));
        }
        if (actualMaterial.toString().contains("BROWN")) {
            return format(a,"#42280E" + repeat(i, "\u2b1b")) + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + format(a,"#42280E" + repeat(i, "\u2b1b"));
        }
        if (actualMaterial.toString().contains("CYAN")) {
            return format(a,"#00FFFF" + repeat(i, "\u2b1b")) + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + format(a,"#00FFFF" + repeat(i, "\u2b1b"));
        }
        if (actualMaterial.toString().contains("LIGHT_GRAY")) {
            return ChatColor.GRAY + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.GRAY + repeat(i, "\u2b1b");
        }
        if (actualMaterial.toString().contains("GRAY")) {
            return ChatColor.DARK_GRAY + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.DARK_GRAY + repeat(i, "\u2b1b");
        }
        if (actualMaterial.toString().contains("GREEN")) {
            return ChatColor.DARK_GREEN + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.DARK_GREEN + repeat(i, "\u2b1b");
        }
        if (actualMaterial.toString().contains("LIME")) {
            return ChatColor.GREEN + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.GREEN + repeat(i, "\u2b1b");
        }
        if (actualMaterial.toString().contains("MAGENTA")) {
            return ChatColor.LIGHT_PURPLE + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.LIGHT_PURPLE + repeat(i, "\u2b1b");
        }
        if (actualMaterial.toString().contains("ORANGE")) {
            return format(a,"#FFA500" + repeat(i, "\u2b1b")) + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + format(a,"#FFA500" + repeat(i, "\u2b1b"));
        }
        if (actualMaterial.toString().contains("PINK")) {
            return format(a,"#FFC0CB" + repeat(i, "\u2b1b")) + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + format(a,"#FFC0CB" + repeat(i, "\u2b1b"));
        }
        if (actualMaterial.toString().contains("PURPLE")) {
            return ChatColor.DARK_PURPLE + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.DARK_PURPLE + repeat(i, "\u2b1b");
        }
        if (actualMaterial.toString().contains("RED")) {
            return ChatColor.RED + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.RED + repeat(i, "\u2b1b");
        }
        if (actualMaterial.toString().contains("WHITE")) {
            return ChatColor.WHITE + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.WHITE + repeat(i, "\u2b1b");
        }
        if (actualMaterial.toString().contains("YELLOW")) {
            return ChatColor.YELLOW + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.YELLOW + repeat(i, "\u2b1b");
        }
        return ChatColor.GOLD + repeat(i, "\u2b1b") + " " + ChatColor.WHITE + "" + ChatColor.BOLD + i + " " + ChatColor.GOLD + repeat(i, "\u2b1b");
    }

}
