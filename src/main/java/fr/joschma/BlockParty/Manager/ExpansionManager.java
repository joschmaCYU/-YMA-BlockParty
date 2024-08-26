package fr.joschma.BlockParty.Manager;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Messages.Language;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class ExpansionManager extends PlaceholderExpansion {

    private final BPM pl;

    public ExpansionManager(BPM plugin) {
        this.pl = plugin;
    }

    @Override
    public String getAuthor() {
        return "joschma";
    }

    @Override
    public String getIdentifier() {
        return "BPM";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (!params.contains("bpm_"))
            params = "bpm_" + params;

        if (params.contains("_player_name_"))
            params = params.replace("_player_name_", "_" + player.getName() + "_");

        if (params.contains("_me_")) {
            params = params.replace("_me_", "_" + player.getName() + "_");
        }

        String[] parse = params.split("_");

        if (parse.length == 4) {
            if (parse[0].equalsIgnoreCase("bpm")) {
                LeaderboardManager ldm = pl.getLeaderboardManager();
                if (pl.isNumeric(parse[1])) {
                    if (parse[2].equalsIgnoreCase("win")) {
                        if (parse[3].equalsIgnoreCase("name")) {
                            return ldm.getName(Integer.valueOf(parse[1]), ldm.getWinMap());
                        } else if (parse[3].equalsIgnoreCase("value")) {
                            return String.valueOf(ldm.getNumber(Integer.valueOf(parse[1]), ldm.getWinMap()));
                        }
                    } else if (parse[2].equalsIgnoreCase("loose")) {
                        if (parse[3].equalsIgnoreCase("name")) {
                            return ldm.getName(Integer.valueOf(parse[1]), ldm.getLooseMap());
                        } else if (parse[3].equalsIgnoreCase("value")) {
                            return String.valueOf(ldm.getNumber(Integer.valueOf(parse[1]), ldm.getLooseMap()));
                        }
                    } else if (parse[2].equalsIgnoreCase("game")) {
                        if (parse[3].equalsIgnoreCase("name")) {
                            return ldm.getName(Integer.valueOf(parse[1]), ldm.getGameMap());
                        } else if (parse[3].equalsIgnoreCase("value")) {
                            return String.valueOf(ldm.getNumber(Integer.valueOf(parse[1]), ldm.getGameMap()));
                        }
                    }
                } else {
                    if (parse[3].equalsIgnoreCase("value")) {
                        String player_name = parse[1];
                        if (parse[2].equalsIgnoreCase("win")) {
                            return String.valueOf(ldm.getNumber(player_name, ldm.getWinMap()));
                        } else if (parse[2].equalsIgnoreCase("loose")) {
                            return String.valueOf(ldm.getNumber(player_name, ldm.getLooseMap()));
                        } else if (parse[2].equalsIgnoreCase("game")) {
                            return String.valueOf(ldm.getNumber(player_name, ldm.getGameMap()));
                        }
                    }
                }
            }
        }

        Player p = Bukkit.getPlayer(player.getUniqueId());

        if (p == null) {
            return "PLAYER NOT FOUND";
        }

        Arena a = null;

        if (pl.getAm().getArenaPlayer(p) != null) {
            a = pl.getAm().getArenaPlayer(p);
        } else {
            for (Arena arena : pl.getAm().getArenas()) {
                if (params.contains(arena.getName()))
                    a = arena;
            }
        }

        if (a == null) {
            if (params.contains("bpm_arena_state")) {
                switch (a.getState()) {
                    case CLEARED:
                        return Language.MSG.state_cleared.msg();
                    case WATTING:
                        return Language.MSG.state_waiting.msg();
                    case INGAME:
                        return Language.MSG.state_in_game.msg();
                    case CLEARING:
                        return Language.MSG.state_clearing.msg();
                    case PAUSED:
                        return Language.MSG.state_paused.msg();
                }
            }
        }
        return arenaPlaceHolder(params, a);
    }

    public String arenaPlaceHolder(String params, Arena a) {

        if (a == null) {
            return "ARENA NOT FOUND/RELOAD PLACEHOLDER_API";
        }

        if ("bpm_color".contains(params)) {
            String[] nameList = a.getDanceFloorActualMaterial().toString().split("_");
            String name = "";

            name = nameList[0];
            if (nameList[0].contains("LIGHT")) {
                name = nameList[1];
            }
            return name;
        } else if (params.contains("bpm_number_of_player_dead")) {
            return String.valueOf((a.getPlayers().size() - a.getPlayersAlive().size()));
        } else if (params.contains("bpm_number_of_player")) {
            return String.valueOf(a.getPlayers().size());
        } else if (params.contains("bpm_number_of_player_minus")) {
            return String.valueOf(a.getPlayers().size() - 1);
        } else if (params.contains("bpm_round_length")) {
            return String.valueOf(new DecimalFormat("#.##").format(a.getRemoveFloorTime()));
        } else if (params.contains("bpm_place")) {
            return String.valueOf(a.getPlayersAlive().size() + 1);
        } else if (params.contains("bpm_number_of_player_playing")) {
            return String.valueOf(a.getPlayersAlive().size());
        } else if (params.contains("bpm_round")) {
            return String.valueOf(a.getRound());
        } else if (params.contains("bpm_minimum_player")) {
            return String.valueOf(a.getMinPlayer());
        } else if (params.contains("bpm_maximum_player")) {
            return String.valueOf(a.getMaxPlayer());
        } else if (params.contains("bpm_power_up_name")) {
            return a.getPowerUpName();
        } else if (params.contains("bpm_maximum_number_of_round")) {
            return String.valueOf(a.getMaxNumberOfRound());
        } else if (params.contains("bpm_name")) {
            return a.getName();
        } else if (params.contains("bpm_song_name")) {
            return String.valueOf(a.getChosenSong().getTitle());
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
