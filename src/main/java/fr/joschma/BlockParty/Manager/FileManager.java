// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Manager;

import fr.joschma.BlockParty.BPM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    static BPM pl;

    public static void reload(final File file) {
        save(file, load(file));
    }

    /*public static void reload(final SimpleConfig file) {
        save(file);
    }*/

    public static YamlConfiguration load(final File file) {
        final YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
        return fc;
    }

    public static File load(final String name) {
        final File file = new File(FileManager.pl.getDataFolder(), name + ".yml");
        return file;
    }

    public static File loadArenaFile(final String name) {
        final File file = new File(FileManager.pl.getDataFolder() + File.separator + "Arenas", name + ".yml");
        return file;
    }

    public static void save(final File file, final YamlConfiguration fc) {
        //saveComments(file);
        try {
            fc.save(file);
        } catch (IOException e) {
            FileManager.pl.getDebug().sysout(ChatColor.RED + "Couldn't save " + file.getName());
            e.printStackTrace();
        }
    }

    public static File createFile(final String name) {
        final File file = new File(FileManager.pl.getDataFolder(), name + ".yml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            FileManager.pl.getDebug().sysout(ChatColor.RED + "Coudn't create " + file.getName());
            e.printStackTrace();
        }
        return file;
    }

    public static File createFile(final File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            FileManager.pl.getDebug().sysout(ChatColor.RED + "Coudn't create " + file.getName());
            e.printStackTrace();
        }

        return file;
    }

    public static File createArenaFile(final String name) {
        final File file = new File(FileManager.pl.getDataFolder() + File.separator + "Arenas", name + ".yml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                FileManager.pl.getDebug().sysout(ChatColor.RED + "Coudn't create " + file.getName());
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File createDanceFloorFile(final String name) {
        final File file = new File(FileManager.pl.getDataFolder() + File.separator + "CustomDanceFloors", name + ".yml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                FileManager.pl.getDebug().sysout(ChatColor.RED + "Coudn't create " + file.getName());
                e.printStackTrace();
            }
        }
        return file;
    }

    static {
        FileManager.pl = BPM.getPl();
    }
}
