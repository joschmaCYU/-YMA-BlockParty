// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Arena.JoinArena;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.State.ArenaState;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.entity.Player;

public class JoinArenaCheck {
    public static boolean canJoin(final Player p, final Arena a) {
        if (a != null) {
            if (a.getFinished()) {
                if (a.getAppreciateTime() > 0) {
                    if (a.getBaseRemoveFloorTime() > 0.0) {
                        if (a.getDanceFloorCuboid() != null) {
                            if (a.getDanceFloorFloorMaterials() != null) {
                                if (a.getDurationPowerUp() >= 0) {
                                    if (a.getFile() != null) {
                                        if (a.getLevelOfPowerUp() >= 0) {
                                            if (a.getLobbySpawn() != null) {
                                                if (a.getMaxNumberOfRound() >= 0) {
                                                    if (a.getMaxPlayer() >= a.getMinPlayer()) {
                                                        if (a.getMinPlayer() > 0) {
                                                            if (a.getPowerUpHead() != null) {
                                                                if (a.getPowerUpName() != null) {
                                                                    if (a.getRangeToCatchPowerup() > 0.0) {
                                                                        if (a.getRegenerateBlockTime() > 0) {
                                                                            if (a.getRemoveFloorTime() > 0.0) {
                                                                                if (a.getPlayers().size() < a.getMaxPlayer() || p.hasPermission("BlockParty.Vip")) {
                                                                                    if (a.getState() == ArenaState.CLEARED || a.getState() == ArenaState.WATTING) {
                                                                                        if (a.getExitSpawn() != null) {
                                                                                            if (a.getArenaCuboid() != null) {
                                                                                                return true;
                                                                                            }
                                                                                            a.getPl().getDebug().error(p, Language.MSG.noArenaCuboid.msg(p));
                                                                                        } else {
                                                                                            a.getPl().getDebug().error(p, Language.MSG.noExitSpawn.msg(p));
                                                                                        }
                                                                                    } else {
                                                                                        a.getPl().getDebug().error(p, Language.MSG.gameAlreadyStarted.msg(p));
                                                                                    }
                                                                                } else {
                                                                                    a.getPl().getDebug().error(p, Language.MSG.toManyPlayers.msg(p));
                                                                                }
                                                                            } else {
                                                                                a.getPl().getDebug().error(p, Language.MSG.removeFloorTimeNegative.msg(p));
                                                                            }
                                                                        } else {
                                                                            a.getPl().getDebug().error(p, Language.MSG.regenerateBlockTimeNegative.msg(p));
                                                                        }
                                                                    } else {
                                                                        a.getPl().getDebug().error(p, Language.MSG.rangeToCatchPowerupNegative.msg(p));
                                                                    }
                                                                } else {
                                                                    a.getPl().getDebug().error(p, Language.MSG.powerUpNameNull.msg(p));
                                                                }
                                                            } else {
                                                                a.getPl().getDebug().error(p, Language.MSG.powerUpHeadNull.msg(p));
                                                            }
                                                        } else {
                                                            a.getPl().getDebug().error(p, Language.MSG.minPlayerNegative.msg(p));
                                                        }
                                                    } else {
                                                        a.getPl().getDebug().error(p, Language.MSG.maxPlayerLowerThanMinPlayer.msg(p));
                                                    }
                                                } else {
                                                    a.getPl().getDebug().error(p, Language.MSG.maxNumberOfRoundNegative.msg(p));
                                                }
                                            } else {
                                                a.getPl().getDebug().error(p, Language.MSG.lobbySpawnNull.msg(p));
                                            }
                                        } else {
                                            a.getPl().getDebug().error(p, Language.MSG.levelOfPowerUpNegative.msg(p));
                                        }
                                    } else {
                                        a.getPl().getDebug().error(p, Language.MSG.fileIsNull.msg(p));
                                    }
                                } else {
                                    a.getPl().getDebug().error(p, Language.MSG.durationPowerUpNegative.msg(p));
                                }
                            } else {
                                a.getPl().getDebug().error(p, Language.MSG.danceFloorFloorMaterialsIsNull.msg(p));
                            }
                        } else {
                            a.getPl().getDebug().error(p, Language.MSG.danceFloorCuboidIsNull.msg(p));
                        }
                    } else {
                        a.getPl().getDebug().error(p, Language.MSG.baseRemoveFloorTimeNegative.msg(p));
                    }
                } else {
                    a.getPl().getDebug().error(p, Language.MSG.appreciateTimeNegative.msg(p));
                }
            } else {
                a.getPl().getDebug().error(p, Language.MSG.notFinished.msg(p));
            }
        }
        return false;
    }
}
