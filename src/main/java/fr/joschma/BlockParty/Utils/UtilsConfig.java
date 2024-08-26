package fr.joschma.BlockParty.Utils;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.CreateArena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.ArenaManager;
import fr.joschma.BlockParty.Manager.FileManager;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UtilsConfig {

    CreateArena createArena;
    BPM pl;

    public UtilsConfig(BPM pl) {
        this.pl = pl;
        createArena = new CreateArena(pl);
    }

    public void update() {
        updateArenas();
        updateLanguage();

        pl.getConfig().set("LastVersion", Double.valueOf(pl.getDescription().getVersion()));
        pl.saveConfig();
    }

    private void updateLanguage() {
        Map<Language.MSG, String> languageMap = new HashMap<>();

        for(Language.MSG c : Language.MSG.values()) {
            if (c != null) {
                languageMap.put(c, c.msg());
            }
        }

        pl.saveResource("Language.yml", true);

        File file = new File(pl.getDataFolder(), "Language.yml");
        YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
        for (Language.MSG msg : languageMap.keySet()) {
            fc.set(msg.toString(), languageMap.get(msg));
        }

        FileManager.save(file, fc);
    }

    private void updateArenas() {
        pl.getDebug().broadcastError("Updating the plugin!");

        Arena arenaTemp = createArena.createArena("TemporaryArenaToNeededToRectifyAPluginIssue");
        File file = arenaTemp.getFile();
        FileManager.createArenaFile(arenaTemp.getName());

        YamlConfiguration fcTemp = YamlConfiguration.loadConfiguration(file);

        for (Arena a : pl.getAm().getArenas()) {
            if (a.getName() == "TemporaryArenaToNeededToRectifyAPluginIssue")
                continue;
            YamlConfiguration fc = YamlConfiguration.loadConfiguration(a.getFile());

            for (String key : fcTemp.getConfigurationSection("").getKeys(true)) {

                if (fc.get(key) == null) {
                    fc.set(key, fcTemp.get(key));
                }
            }
            FileManager.save(a.getFile(), fc);
        }

        pl.getAm().rmvArenaListNFile(arenaTemp);
    }
}
