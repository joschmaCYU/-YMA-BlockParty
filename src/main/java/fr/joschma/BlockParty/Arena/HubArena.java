package fr.joschma.BlockParty.Arena;

import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.FileManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HubArena {

    BPM pl;
    String name;
    int maxPlayer;
    int currentPlayer;
    List<Sign> signs = new ArrayList<>();
    File file;
    ArenaState state;
    List<String> signDisplay = new ArrayList<>();

    public HubArena(BPM pl, String name, int maxPlayer, ArenaState state) {
        this.pl = pl;
        this.name = name;
        this.maxPlayer = maxPlayer;
        this.state = state;
        file = new File(pl.getDataFolder(), "HubArena" + File.separator + name + ".yml");

        signDisplay.add("§a[BlockParty]");
        signDisplay.add(name);
        signDisplay.add(currentPlayer + " / " + maxPlayer);
        setupFile();
    }

    private void setupFile() {
        if (!file.exists()) {
            FileManager.createFile(file);

            YamlConfiguration fc = FileManager.load(file);
            fc.set("SignDisplay", signDisplay);
            fc.set("Settings.MaxPlayer", maxPlayer);
            FileManager.save(file, fc);
        }
    }

    public void updateSign() {
        if (this.state != ArenaState.INGAME) {
            for (final Sign sign : signs) {
                int y = 0;

                for (String str : signDisplay) {
                    sign.setLine(y, msg(str));
                    /*if (str.contains("%")) {
                        String[] sentence = str.split("%");

                        for (String stri : sentence) {
                            String replaced = pl.getExpansionManager().arenaPlaceHolder(stri, this);
                            if (replaced != null) {
                                str = str.replace("%" + stri + "%", replaced);
                            }
                        }

                        sign.setLine(y, msg(str));
                    }

                    if (currentPlayer > 0)
                        sign.setLine(y, msg(players.get(0), str));*/
                    y++;
                }

                sign.update();
            }
        } else {
            for (final Sign sign : signs) {
                sign.setLine(2, ChatColor.RED + "In game !");
                sign.update();
            }
        }
    }

    /*public String msg(Player p, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (BPM.getPl().isPlaceholderIsEnable())
            return PlaceholderAPI.setPlaceholders(p, message);
        return message;
    }*/

    public String msg(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public List<Sign> getSigns() {
        return signs;
    }

    public BPM getPl() {
        return pl;
    }

    public File getFile() {
        return file;
    }

    public void setSigns(List<Sign> signs) {
        this.signs = signs;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
