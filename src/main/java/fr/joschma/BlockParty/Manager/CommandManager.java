// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Manager;

import com.cryptomorin.xseries.XMaterial;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.CreateArena;
import fr.joschma.BlockParty.Arena.HubArena;
import fr.joschma.BlockParty.Arena.JoinArena.JoinArena;
import fr.joschma.BlockParty.Arena.JoinArena.JoinArenaCheck;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.Arena.State.SongProvider;
import fr.joschma.BlockParty.Arena.State.SongSetting;
import fr.joschma.BlockParty.BPM;
import fr.joschma.BlockParty.Messages.Language;
import fr.joschma.BlockParty.Utils.UtilsLoc;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommandManager implements CommandExecutor {

    BPM pl;
    ArenaManager am;
    final CreateArena createArena;

    public CommandManager(final BPM pl) {
        this.pl = pl;
        am = pl.getAm();
        createArena = new CreateArena(pl);
    }

    boolean t;

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player p = (Player) sender;

            if (args.length == 0) {
                p.performCommand(cmd.getName() + " help");
                return false;
            }

            if (pl.isHubServer()) {
                if (args[0].equalsIgnoreCase("help")) {
                    pl.getDebug().msg(p, "<arena> sign: set the sign linked to the arena to join it (you can set multiples)");
                }

                if (args.length == 2) {
                    if (pl.getAm().getHubArenaNames().contains(args[0])) {
                        if (args[1].equalsIgnoreCase("sign")) {
                            pl.getDebug().error(p, "/bp " + args[0] + " sign [add/remove]");
                        }
                    }
                }

                if (args.length == 3) {
                    HubArena a = pl.getAm().getHubArena(args[0]);
                    if (a != null) {
                        YamlConfiguration fc = YamlConfiguration.loadConfiguration(a.getFile());
                        if (args[1].equalsIgnoreCase("sign")) {
                            if (args[2].equalsIgnoreCase("add")) {
                                int i = 10;
                                if (p.getTargetBlockExact(i) == null) {
                                    pl.getDebug().error(p, Language.MSG.noBlockFoundInLookingDirection.msg(p));
                                } else if (p.getTargetBlockExact(i).getState() instanceof Sign) {
                                    final Sign sign = (Sign) p.getTargetBlockExact(i).getState();
                                    if (a.getSigns() != null) {
                                        for (final Sign arenaSign : a.getSigns()) {
                                            if (sign.getLocation().equals((Object) arenaSign.getLocation())) {
                                                pl.getDebug().msg(p, Language.MSG.signAlreadyAdded.msg(p));
                                                return false;
                                            }
                                        }
                                    }

                                    a.getSigns().add(sign);

                                    final List<String> signs = (List<String>) fc.getStringList("Signs");
                                    signs.add(UtilsLoc.locToString(sign.getLocation()));
                                    fc.set("Signs", (Object) signs);
                                    FileManager.save(a.getFile(), fc);

                                    a.updateSign();

                                    pl.getDebug().msg(p, Language.MSG.succesfullySetSign.msg(p));
                                } else {
                                    ++i;
                                }
                            } else if (args[2].equalsIgnoreCase("remove")) {
                                List<Sign> signs = a.getSigns();

                                if (signs.size() > 0) {
                                    signs.remove(signs.size() - 1);
                                    a.setSigns(signs);

                                    List<String> signsString = new ArrayList<String>();
                                    for (Sign sign1 : signs) {
                                        signsString.add(UtilsLoc.locToString(sign1.getLocation()));
                                    }

                                    fc.set("Arena.Sign.Loc", signsString);
                                    FileManager.save(a.getFile(), fc);
                                    pl.getDebug().msg(p, "You have deleted the last sign");
                                } else {
                                    pl.getDebug().error(p, "No more sign to remove");
                                }
                            }
                        }
                    }
                }
            } else {
                if (args[0].equalsIgnoreCase("join")) {
                    if (args.length == 2) {
                        if (am.arenaNames.contains(args[1])) {
                            final Arena ar = am.getArena(args[1]);
                            if (!ar.joinParty(p)) {
                                if (pl.getConfig().getBoolean("useBungeeCord")) {
                                    pl.sendCustomData(p, "join", ar.getName());
                                } else {
                                    JoinArena.joinArena(p, ar);
                                }
                            }
                        } else if (args[1].equalsIgnoreCase("random")) {
                            List<Arena> arenas = new ArrayList<>();
                            arenas.addAll(am.getArenas());
                            arenas.removeIf(ar -> (!JoinArenaCheck.canJoin(p, ar)));

                            if (arenas.size() > 0) {
                                final Random rand = new Random();
                                int randNum = rand.nextInt(arenas.size());
                                Arena ar = am.getArenas().get(randNum);

                                if (ar.isSmartRandomJoin()) {
                                    Arena finalAr = ar;
                                    arenas.removeIf(arena -> (arena.getPlayers().size() <
                                            finalAr.getPlayers().size()));

                                    randNum = rand.nextInt(arenas.size());
                                    ar = am.getArenas().get(randNum);

                                    Arena finalAr1 = ar;
                                    arenas.removeIf(arena -> (arena.getWaitLobbyTimer().getTime() <
                                            finalAr1.getWaitLobbyTimer().getTime()));

                                    randNum = rand.nextInt(arenas.size());
                                    ar = am.getArenas().get(randNum);
                                }

                                JoinArena.joinArena(p, ar);
                            } else {
                                pl.getDebug().msg(p, Language.MSG.noGameFound.msg(p));
                            }
                        } else {
                            pl.getDebug().msg(p, Language.MSG.noGameFound.msg(p));
                        }
                    } else if (args.length == 1) {
                        pl.getJoinArenaGui().openArenaGui(p);
                    }
                }

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("leave")) {
                        final Arena a2 = am.getArenaPlayer(p);
                        if (a2 != null) {
                            if (pl.getConfig().getBoolean("useBungeeCord")) {
                                pl.sendCustomData(p, "leave", a2.getName());
                            } else {
                                a2.leaveGame(p);
                                pl.getDebug().msg(p, Language.MSG.YouHaveLeft.msg(p));
                            }
                        } else {
                            pl.getDebug().msg(p, Language.MSG.youHaveTobeInAGameToExecuteThisCommand.msg(p));
                        }
                    }
                }

                if (p.hasPermission("BlockParty.ForceStart")) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("forcestart")) {
                            final Arena a2 = am.getArenaPlayer(p);
                            if (a2 != null) {
                                if (a2.getState() == ArenaState.WATTING) {
                                    a2.getWaitLobbyTimer().stopTimer(a2);
                                    a2.getStartGame().startGame(a2);
                                }
                            } else {
                                pl.getDebug().msg(p, Language.MSG.youHaveTobeInAGameToExecuteThisCommand.msg(p));
                            }
                        }
                    }
                }

                if (p.hasPermission("BlockParty.admin") || p.hasPermission("BlockParty.Admin")) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            pl.getDebug().msg(p, "Reloading the plugin...");
                            pl.reloadConfig();
                            FileManager.reload(FileManager.load("Language"));

                            for (final Arena a : am.getArenas()) {
                                FileManager.reload(a.getFile());
                            }

                            FileManager.reload(FileManager.load("ColourLanguage"));

                            am.setArenas(new ArrayList<Arena>());
                            pl.getInitializeArena().initializeArena();
                            pl.getDebug().msg(p, "Plugin reloaded");
                        } else if (args[0].equalsIgnoreCase("forcestart")) {
                            final Arena a2 = am.getArenaPlayer(p);
                            if (a2 != null) {
                                if (a2.getState() == ArenaState.WATTING) {
                                    a2.getWaitLobbyTimer().stopTimer(a2);
                                    a2.getStartGame().startGame(a2);
                                }
                            } else {
                                pl.getDebug().msg(p, Language.MSG.youHaveTobeInAGameToExecuteThisCommand.msg(p));
                            }
                        } else if (args[0].equalsIgnoreCase("help")) {
                            pl.getDebug().msg(p, "help [commands, permission, config]");
                        } else if (args[0].equalsIgnoreCase("pause")) {
                            final Arena a2 = am.getArenaPlayer(p);
                            if (a2 != null) {
                                if (a2.getState() == ArenaState.INGAME) {
                                    a2.getGiveTerracotaTimer().stopTimer();
                                    a2.getRegenerateBlockTimer().stopTimer();
                                    a2.getRemoveFloorCountDown().stopTimer();
                                    a2.setState(ArenaState.PAUSED);

                                    for (Player p2 : a2.getPlayersAlive()) {
                                        pl.getDebug().msg(p, Language.MSG.PausedGame.msg(p2));
                                    }
                                }
                                if (a2.getState() == ArenaState.PAUSED) {
                                    a2.getRegenerateBlockTimer().startCountDown(a2);
                                    a2.setState(ArenaState.INGAME);
                                    // TODO send message game resume
                                }
                            } else {
                                pl.getDebug().msg(p, Language.MSG.youHaveTobeInAGameToExecuteThisCommand.msg(p));
                            }
                        } else if (am.getArenaNames().contains(args[0])) {
                            pl.getArenaGui().openArenaGui(p, am.getArena(args[0]));
                        }
                    } else if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("create")) {
                            if (pl.isNumeric(args[1])) {
                                pl.getDebug().error(p, Language.MSG.NumericsAreNotAllowed.msg(p));
                                return false;
                            } else if (args[0].equalsIgnoreCase("random")) {
                                pl.getDebug().error(p, Language.MSG.NotValidName.msg(p));
                                return false;
                            }
                            if (!am.arenaNames.contains(args[1])) {
                                createArena.createArena(args[1]);
                                pl.getDebug().msg(p, Language.MSG.createdArena.msg(p));
                            } else {
                                pl.getDebug().error(p, Language.MSG.arenaAlreadyExists.msg(p));
                            }
                        } else if (args[0].equalsIgnoreCase("delete")) {
                            final Arena a2 = am.getArena(args[1]);
                            if (a2 != null) {
                                am.rmvArenaListNFile(a2);
                                pl.getDebug().msg(p, Language.MSG.deletedArena.msg(p));
                            } else {
                                pl.getDebug().msg(p, Language.MSG.noGameFound.msg(p));
                            }
                        } else if (am.getArenaNames().contains(args[0])) {
                            final Arena a2 = am.getArena(args[0]);
                            YamlConfiguration fc = YamlConfiguration.loadConfiguration(a2.getFile());
                            if (a2 != null) {
                                if (args[1].equalsIgnoreCase("finish")) {
                                    final boolean finished = !a2.getFinished();
                                    a2.setFinished(finished);
                                    fc.set("Settings.Finished", (Object) finished);
                                    pl.getDebug().msg(p, Language.MSG.finished.msg(p) + " " + finished);
                                } else if (args[1].equalsIgnoreCase("dancefloorzone")) {
                                    CreationZoneManager.addCreationZone(p, a2);
                                    CreationZoneManager.addPlayerLocation(p, new ArrayList<Location>());
                                    p.getInventory().setItem(0, new ItemStack(XMaterial.STICK.parseMaterial()));
                                } else if (args[1].equalsIgnoreCase("arenazone")) {
                                    if (a2.getDanceFloorCuboid() == null) {
                                        pl.getDebug().error(p, "Set first the dance floor zone!");
                                    }
                                    CreationZoneManager.addCreationZoneArena(p, a2);
                                    CreationZoneManager.addPlayerLocation(p, new ArrayList<Location>());
                                    p.getInventory().setItem(0, new ItemStack(XMaterial.STICK.parseMaterial()));
                                } else if (args[1].equalsIgnoreCase("lobbySpawn")) {
                                    a2.setLobbySpawn(p.getLocation());
                                    fc.set("Spawn.LobbySpawn", (Object) UtilsLoc.locToString(p.getLocation()));
                                    pl.getDebug().msg(p, Language.MSG.lobbySpawn.msg(p));
                                } else if (args[1].equalsIgnoreCase("exitspawn")) {
                                    a2.setExitSpawn(p.getLocation());
                                    pl.getDebug().msg(p, Language.MSG.setExitSpawn.msg(p));
                                    fc.set("Spawn.ExitSpawn", (Object) UtilsLoc.locToString(p.getLocation()));
                                } else if (args[1].equalsIgnoreCase("sign")) {
                                    pl.getDebug().error(p, "/bp " + args[0] + " sign [add/remove]");
                                } else if (args[1].equalsIgnoreCase("gui")) {
                                    pl.getArenaGui().openArenaGui(p, am.getArena(args[0]));
                                } else if (args[1].equalsIgnoreCase("songsetting")) {
                                    if (a2.getSongSetting() == SongSetting.CHOOSE) {
                                        a2.setSongSetting(SongSetting.RANDOM);
                                        fc.set("Settings.SongSetting", SongSetting.RANDOM.toString());
                                        pl.getDebug().msg(p, "You have set the song setting at " + ChatColor.GOLD + "RANDOM");
                                    } else {
                                        a2.setSongSetting(SongSetting.CHOOSE);
                                        fc.set("Settings.SongSetting", SongSetting.CHOOSE.toString());
                                        pl.getDebug().msg(p, "You have set the song setting at " + ChatColor.GOLD + "CHOOSE");
                                    }
                                } else if (args[1].equalsIgnoreCase("songprovider")) {
                                    if (a2.getSongProvider() == SongProvider.NoteBlock) {
                                        a2.setSongProvider(SongProvider.MCJukebox);
                                        pl.getDebug().msg(p, "You have set the song provider at " + ChatColor.GOLD + "MCJukebox");
                                    } else if (a2.getSongProvider() == SongProvider.MCJukebox) {
                                        a2.setSongProvider(SongProvider.OpenAudioMC);
                                        pl.getDebug().msg(p, "You have set the song provider at " + ChatColor.GOLD + "OpenAudioMC");
                                    } else if (a2.getSongProvider() == SongProvider.OpenAudioMC) {
                                        a2.setSongProvider(SongProvider.NoteBlock);
                                        pl.getDebug().msg(p, "You have set the song provider at " + ChatColor.GOLD + "NoteBlock");
                                    }

                                    fc.set("Settings.SongProvider", a2.getSongProvider().toString());
                                } else if (args[1].equalsIgnoreCase("spectator")) {
                                    if (a2.getState() == ArenaState.WATTING) {
                                        p.setGameMode(GameMode.SPECTATOR);
                                    }
                                }

                                FileManager.save(a2.getFile(), fc);
                            }
                        } else if (args[0].equalsIgnoreCase("help")) {
                            if (args[1].equalsIgnoreCase("commands")) {
                                pl.getDebug().msg(p, "------------------------------------");
                                pl.getDebug().msg(p, "Reload : will reload the configs");
                                pl.getDebug().msg(p, "Join : lets you join an arena");
                                pl.getDebug().msg(p, "------------------------------------");
                                pl.getDebug().msg(p, "forcestart, will start the game your in");
                                pl.getDebug().msg(p, "Create <arena>: make a new arena");
                                pl.getDebug().msg(p, "Delete <arena>: remove an arena");
                                pl.getDebug().msg(p, "------------------------------------");
                                pl.getDebug().msg(p, "<arena> finished: lets people play in the arena");
                                pl.getDebug().msg(p, "<arena> dancezone: where the dance floor is generate");
                                pl.getDebug().msg(p, "<arena> dancefloorzone: zone where you can move, make it big enough so that spectators can watch the game");
                                pl.getDebug().msg(p, "<arena> lobbySpawn: where player will wait before the game starts");
                                pl.getDebug().msg(p, "<arena> exitspawn: where player will be teleported at the end of the game");
                                pl.getDebug().msg(p, "<arena> sign: set the sign linked to the arena to join it (you can set multiples)");
                                pl.getDebug().msg(p, "<arena> songsetting: can be RANDOM which means that the players will not be able to chose the song. Or it can be CHOOSE which means that the players can chose what song will be played");
                                pl.getDebug().msg(p, "<arena> songprovider: the plugin that will play the songs");
                                pl.getDebug().msg(p, "<arena> customDanceFloor [add/remove] <name> : to add or remove an already created custom dance floor to an arena");
                                pl.getDebug().msg(p, "<arena> spectator : set your self as a spectator");
                                pl.getDebug().msg(p, "------------------------------------");
                                pl.getDebug().msg(p, "CustomDanceFloor [create/delete] <name> : you can create or delete custom 2d dance floor");
                                pl.getDebug().msg(p, "------------------------------------");
                            } else if (args[1].equalsIgnoreCase("permission")) {
                                pl.getDebug().msg(p, "The permissions for this plugin are : [BlockParty.admin, BlockParty.VIP, BlockParty.Player, BlockParty.ChoseSong, BlockParty.ForceStart]");
                            }
                        }
                    } else if (args.length == 3) {
                        Arena a = pl.getAm().getArena(args[0]);
                        if (a != null) {
                            YamlConfiguration fc = YamlConfiguration.loadConfiguration(a.getFile());
                            if (args[1].equalsIgnoreCase("sign")) {
                                if (args[2].equalsIgnoreCase("add")) {
                                    int i = 10;
                                    if (p.getTargetBlockExact(i) == null) {
                                        pl.getDebug().error(p, Language.MSG.noBlockFoundInLookingDirection.msg(p));
                                    } else if (p.getTargetBlockExact(i).getState() instanceof Sign) {
                                        final Sign sign = (Sign) p.getTargetBlockExact(i).getState();
                                        if (a.getSigns() != null) {
                                            for (final Sign arenaSign : a.getSigns()) {
                                                if (sign.getLocation().equals((Object) arenaSign.getLocation())) {
                                                    pl.getDebug().msg(p, Language.MSG.signAlreadyAdded.msg(p));
                                                    return false;
                                                }
                                            }
                                        }

                                        a.getSigns().add(sign);

                                        final List<String> signs = (List<String>) fc.getStringList("Signs");
                                        signs.add(UtilsLoc.locToString(sign.getLocation()));
                                        fc.set("Signs", (Object) signs);
                                        FileManager.save(a.getFile(), fc);

                                        a.updateSign();

                                        pl.getDebug().msg(p, Language.MSG.succesfullySetSign.msg(p));
                                    } else {
                                        ++i;
                                    }
                                } else if (args[2].equalsIgnoreCase("remove")) {
                                    List<Sign> signs = a.getSigns();

                                    if (signs.size() > 0) {
                                        signs.remove(signs.size() - 1);
                                        a.setSigns(signs);

                                        List<String> signsString = new ArrayList<String>();
                                        for (Sign sign1 : signs) {
                                            signsString.add(UtilsLoc.locToString(sign1.getLocation()));
                                        }

                                        fc.set("Arena.Sign.Loc", signsString);
                                        FileManager.save(a.getFile(), fc);
                                        pl.getDebug().msg(p, "You have deleted the last sign");
                                    } else {
                                        pl.getDebug().error(p, "No more sign to remove");
                                    }
                                }
                            }
                        }

                        if (args[0].equalsIgnoreCase("customDanceFloor")) {
                            String name = args[2];
                            if (args[1].equalsIgnoreCase("create")) {
                                if (pl.isNumeric(name)) {
                                    pl.getDebug().error(p, Language.MSG.NumericsAreNotAllowed.msg(p));
                                    return false;
                                }
                                if (!CreationZoneManager.getCreationDanceZone().containsKey(p)) {
                                    if (!new File(pl.getDataFolder() + File.separator + "CustomDanceFloors", name + ".yml").exists()) {
                                        FileManager.createDanceFloorFile(name);
                                        p.getInventory().setItem(0, new ItemStack(Material.STICK));
                                        CreationZoneManager.addCreationDanceZone(p, name);
                                        CreationZoneManager.addPlayerLocation(p, new ArrayList<Location>());
                                    } else {
                                        pl.getDebug().error(p, Language.MSG.DanceFloorAlreadyExist.msg());
                                        return false;
                                    }
                                } else {
                                    pl.getDebug().error(p, Language.MSG.AlreadyCreatingDanceZone.msg());
                                }
                            } else if (args[1].equalsIgnoreCase("delete")) {
                                try {
                                    new File(pl.getDataFolder() + File.separator + "CustomDanceFloors", name + ".yml").delete();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return false;
                                }

                                for (Arena ar : pl.getAm().getArenas()) {
                                    if (ar.getCustomDanceFloor().contains(name))
                                        ar.getCustomDanceFloor().remove(name);
                                }

                                pl.getDebug().msg(p, Language.MSG.SuccessfullyDeletedCustomDanceFloor.msg());
                            }
                        } else if (args[0].equalsIgnoreCase("help")) {
                            if (args[1].equalsIgnoreCase("config")) {
                                pl.getDebug().msg(p, "---------------IMPORTANT---------------");
                                pl.getDebug().msg(p, "After you have done all the changes you should do /blockparty ad");
                                if (args[2].equalsIgnoreCase("PowerUp")) {
                                    pl.getDebug().msg(p, "---------------PowerUp---------------");
                                    pl.getDebug().msg(p, "Name: The name displayed above the power up");
                                    pl.getDebug().msg(p, "Duration: The duration of the effect given (in seconds)");
                                    pl.getDebug().msg(p, "LevelOf: The level of the effect given (It begins at 0)");
                                    pl.getDebug().msg(p, "RangeToCatch: the radius were the player can catch the power up");
                                    pl.getDebug().msg(p, "Head: The item put on the head of the power up");
                                    pl.getDebug().msg(p, "ActivePowerUp: All the power ups you see in this section are activated. " +
                                            "Remove one if you don't want it in this arena !");
                                } else if (args[2].equalsIgnoreCase("Settings")) {
                                    pl.getDebug().msg(p, "---------------Settings--------------");
                                    pl.getDebug().msg(p, "Finished: is the arena opened to play");
                                    pl.getDebug().msg(p, "SongProvider: The plugin that will play the songs");
                                    pl.getDebug().msg(p, "SongSetting: can be RANDOM which means that the players will not be able to chose the song. Or it can be CHOOSE which means that the players can chose what song will be played");
                                    pl.getDebug().msg(p, "ChangeTime: the Minecraft time is going to change depending on the wait lobby timer");
                                    pl.getDebug().msg(p, "SaveInventory: should the plugin save the inventory of the player and give it back at the end of the game");
                                } else if (args[2].equalsIgnoreCase("Game")) {
                                    pl.getDebug().msg(p, "----------------Game-----------------");
                                    pl.getDebug().msg(p, "GameModeOnDeath: when the player lose in which game mode should he be put in");
                                    pl.getDebug().msg(p, "Collision: should there be collisions between players");
                                    pl.getDebug().msg(p, "CommandsOnGameEnd: All the commands that should be executed at the end of the game");
                                    pl.getDebug().msg(p, "MaxPlayer: The maximum number of player that can play");
                                    pl.getDebug().msg(p, "MinPlayer: The minimum number of player to start a game");
                                    pl.getDebug().msg(p, "LobbyWaitTime: The time to wait before starting a game (in seconds)");
                                    pl.getDebug().msg(p, "AppreciateTime: The time to wait after a player won a game and before being teleported out of the game");
                                    pl.getDebug().msg(p, "RegenerateBlockTime: The time were all the other blocks have disappeared");
                                    pl.getDebug().msg(p, "WaitTimeBeforeGiveColorFirstRound: The time before giving the first color to stand on the first round");
                                    pl.getDebug().msg(p, "WaitTimeBeforeGiveColor: The time before giving the first color to stand on all the other rounds");
                                    pl.getDebug().msg(p, "TimeToRemoveFromRemoveFloorTime: The number of time (can be decimal) to reduce between the time the player has to go on a block before all other disappear after been given it");
                                    pl.getDebug().msg(p, "BaseRemoveFloorTime: The initial time between the time the player has to go on a block before all other disappear after been given it");
                                    pl.getDebug().msg(p, "MaxNumberOfRound: The maximum number of round that can be players");
                                    pl.getDebug().msg(p, "DanceFloorFloorMaterials: All the blocks that can be put as a dance floor block");
                                    pl.getDebug().msg(p, "RoundToSpawnPowerUp: Which round power ups should spawn");
                                    pl.getDebug().msg(p, "RoundToReduceTime: Which round reduce the RemoveFloorTime");
                                } else if (args[2].equalsIgnoreCase("Sound")) {
                                    pl.getDebug().msg(p, "----------------Sound----------------");
                                    pl.getDebug().msg(p, "Refer to the section 'Song Provider' on the spigot page: https://www.spigotmc.org/resources/yma-block-party-with-music-1-13-1-18.98473/");
                                    pl.getDebug().msg(p, "If you use Note Block API take a look at this tutorial: https://www.youtube.com/watch?v=5sMkaskw5Y8");
                                    pl.getDebug().msg(p, "If you use MC Jucke Box you can host musics by folowing this tutorial https://gist.github.com/Moutard3/b925b090ab1d6d20a5d20f054bae7bca");
                                    pl.getDebug().msg(p, "[Path/Link]ToMusic: Is the musics that are going to be played in the game. The music can be chosen by the player");
                                    pl.getDebug().msg(p, "[Path/Link]ToStopMusic: The music that will be played when the blocks disappear");
                                } else if (args[2].equalsIgnoreCase("Other")) {
                                    pl.getDebug().msg(p, "------------Cuboid & Spawn-----------");
                                    pl.getDebug().msg(p, "These parameters should not be touched unless you know what you are doing");
                                    pl.getDebug().msg(p, "-----------------Other---------------");
                                    pl.getDebug().msg(p, "AllowedCommands: The command that can be executed will playing");
                                } else if (args[2].equalsIgnoreCase("ScoreBoard")) {
                                    pl.getDebug().msg(p, "--------------ScoreBoard-------------");
                                    pl.getDebug().msg(p, "ScoreBoard: You just have to add or remove a the line you want to change. " +
                                            "You can use every placeholder");
                                }
                            }
                        }

                    } else if (args.length == 4) {
                        final Arena a = am.getArena(args[0]);
                        if (a != null) {
                            if (args[1].equalsIgnoreCase("customDanceFloor")) {
                                String name = args[3];
                                File fileCustomDanceFloor = new File(pl.getDataFolder() + File.separator + "CustomDanceFloors", name + ".yml");
                                YamlConfiguration fc = YamlConfiguration.loadConfiguration(fileCustomDanceFloor);
                                if (args[2].equalsIgnoreCase("add")) {
                                    if (fileCustomDanceFloor.exists()) {
                                        a.getCustomDanceFloor().add(name);
                                        fc.set("Cuboid.CustomDanceFloorName", a.getCustomDanceFloor());
                                        FileManager.save(fileCustomDanceFloor, fc);
                                        pl.getDebug().msg(p, Language.MSG.SuccessfullyAddedCustomDanceFloor.msg());
                                    } else {
                                        pl.getDebug().error(p, Language.MSG.DanceFloorNotExist.msg());
                                    }
                                } else if (args[2].equalsIgnoreCase("remove")) {
                                    if (!a.getCustomDanceFloor().contains(name)) {
                                        pl.getDebug().error(p, Language.MSG.couldNotRemoveDanceFloor.msg());
                                        return false;
                                    }

                                    a.getCustomDanceFloor().remove(name);
                                    fc.set("Cuboid.CustomDanceFloorName", a.getCustomDanceFloor());
                                    FileManager.save(fileCustomDanceFloor, fc);
                                    pl.getDebug().msg(p, Language.MSG.SuccessfullyRemovedCustomDanceFloor.msg());
                                }
                            }
                        }
                    }
                } else {
                    pl.getDebug().error(p, Language.MSG.noPermission.msg());
                }
            }
        }
        return false;
    }
}
