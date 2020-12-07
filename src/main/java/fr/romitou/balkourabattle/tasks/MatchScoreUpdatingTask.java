package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.query.MatchQuery;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.utils.MatchUtils;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;

public class MatchScoreUpdatingTask extends BukkitRunnable {

    private final Match match;
    private Integer score1;
    private Integer score2;

    public MatchScoreUpdatingTask(Match match, @Nullable Integer score1, @Nullable Integer score2) {
        this.match = match;
        this.score1 = score1;
        this.score2 = score2;
    }

    @Override
    public void run() {
        if (score1 == null || score2 == null) {
            Integer[] scores = MatchUtils.getScores(match);
            score1 = scores[0];
            score2 = scores[1];
        }
        try {
            ChallongeManager.getChallonge().updateMatch(match, MatchQuery.builder().scoresCsv(score1 + "-" + score2).build());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
