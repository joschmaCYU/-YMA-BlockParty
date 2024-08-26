// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.TabFinisher;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Manager.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

public class TabCompletor implements TabCompleter {

    BPM pl;
    ArenaManager am;
    List<String> list = new ArrayList<String>();

    public TabCompletor(final BPM pl) {
        this.pl = pl;
        this.am = pl.getAm();
    }

    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        try {
            list.clear();
            if (sender instanceof Player) {
                final Player p = (Player) sender;

                if (pl.isHubServer()) {
                    if (args.length == 1) {
                        if ((p.hasPermission("BlockParty.admin") || p.isOp())) {
                            list.add("help");

                            for (final String name : this.am.getHubArenaNames()) {
                                list.add("" + name);
                            }
                        }
                    } else if (args.length == 2) {
                        if (pl.getAm().getHubArenaNames().contains(args[0])) {
                            if (p.hasPermission("BlockParty.admin") || p.isOp()) {
                                list.add("sign");
                            }
                        }
                    } else if (args.length == 3) {
                        if (args[1].equalsIgnoreCase("sign") || args[1].equalsIgnoreCase("customDanceFloor")) {
                            list.add("add");
                            list.add("remove");
                        }
                    }
                } else {
                    if (args.length == 1) {
                        if ((p.hasPermission("BlockParty.admin") || p.isOp())) {
                            list.add("create");
                            list.add("delete");
                            list.add("reload");
                            list.add("forcestart");
                            list.add("spectator");
                            list.add("pause");
                            list.add("help");
                            list.add("customDanceFloor");

                            for (final String name : this.am.getArenaNames()) {
                                list.add("" + name);
                            }
                        }

                        list.add("leave");
                        list.add("join");
                    } else if (args.length == 2) {
                        if (pl.getAm().getArenaNames().contains(args[0])) {
                            if (p.hasPermission("BlockParty.admin") || p.isOp()) {
                                list.add("gui");
                                list.add("finish");
                                list.add("arenaZone");
                                list.add("danceFloorZone");
                                list.add("lobbySpawn");
                                list.add("exitSpawn");
                                list.add("sign");
                                list.add("songSetting");
                                list.add("songProvider");
                                list.add("customDanceFloor");
                            }
                        }

                        if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("join")) {
                            list.add("random");
                            for (final String name2 : this.am.getArenaNames()) {
                                list.add(name2);
                            }
                        } else if (args[0].equalsIgnoreCase("create")) {
                            list.add("<ArenaName>");
                        } else if (args[0].equalsIgnoreCase("help") && (p.hasPermission("BlockParty.admin") || p.isOp())) {
                            list.add("commands");
                            list.add("config");
                            list.add("permission");
                        } else if (args[0].equalsIgnoreCase("customDanceFloor")) {
                            list.add("create");
                            list.add("delete");
                        }
                    } else if (args.length == 3) {
                        if (args[1].equalsIgnoreCase("sign") || args[1].equalsIgnoreCase("customDanceFloor")) {
                            list.add("add");
                            list.add("remove");
                        } else if (args[0].equalsIgnoreCase("customDanceFloor")) {
                            if (args[1].equalsIgnoreCase("create")) {
                                list.add("<Arena Name>");
                            } else if (args[1].equalsIgnoreCase("delete")) {
                                for (String name : pl.getCustomDanceFloorManager().getCustomDanceFloor().keySet()) {
                                    list.add(name);
                                }
                            }
                        } else if (args[1].equalsIgnoreCase("config") && (p.hasPermission("BlockParty.admin") || p.isOp())) {
                            list.add("PowerUp");
                            list.add("Settings");
                            list.add("Game");
                            list.add("Sound");
                            list.add("Other");
                            list.add("ScoreBoard");
                        }
                    } else if (args.length == 4) {
                        if (args[1].equalsIgnoreCase("customDanceFloor")) {
                            final Arena a = am.getArena(args[0]);
                            if (a != null) {
                                if (args[2].equalsIgnoreCase("add")) {
                                    if (a.getPl().getCustomDanceFloorManager().getCustomDanceFloor().isEmpty()) {
                                        list.add("No custom dance floor!");
                                    } else {
                                        for (String name : pl.getCustomDanceFloorManager().getCustomDanceFloor().keySet()) {
                                            if (!a.getCustomDanceFloor().contains(name))
                                                list.add(name);
                                        }
                                    }
                                } else if (args[2].equalsIgnoreCase("remove")) {
                                    if (a.getPl().getCustomDanceFloorManager().getCustomDanceFloor().isEmpty()) {
                                        list.add("No custom dance floor!");
                                    } else {
                                        for (String name : a.getCustomDanceFloor()) {
                                            list.add(name);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                final List<String> result = new ArrayList<String>();
                for (String a : list) {
                    if (a.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                        result.add(a);
                    }
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
