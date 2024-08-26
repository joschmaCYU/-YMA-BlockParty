// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Utils;

import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Messages.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class ScoreBoardUtils {

    final Map<UUID, Scoreboard> boards = new HashMap<>();

    public void addScoreBoard(final Arena a, final Player p) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj;
        Team team;

        if (!board.getEntries().isEmpty() && board.getObjective(Language.MSG.ScoreboardTitle.msg(p)) != null) {
            team = board.getTeam("Dancer");

            if (team != null) {
                if (!team.getEntries().contains(p.getName()))
                    team.addEntry(p.getName());
            }

            obj = board.getObjective(DisplaySlot.SIDEBAR);
        } else {
            board = Bukkit.getScoreboardManager().getNewScoreboard();
            obj = board.registerNewObjective(Language.MSG.ScoreboardTitle.msg(), "dummy", Language.MSG.ScoreboardTitle.msg(p));
            team = board.registerNewTeam("Dancer");
            if (!a.isPlayerColision())
                team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            team.addEntry(p.getName());
        }

        boards.put(p.getUniqueId(), board);

        int i = a.getScoreBoard().size();
        for (String text : a.getScoreBoard()) {
            Score score = obj.getScore(a.msg(p, text));
            score.setScore(i);
            i--;
        }

        p.setScoreboard(board);
    }

    public void rmvScoreBoard(final Player p) {
        if (p.getScoreboard().getTeam("Dancer") != null) {
            p.getScoreboard().getTeam("Dancer").removeEntry(p.getName());
        }
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        Scoreboard board = boards.remove(p.getUniqueId());
    }
}
