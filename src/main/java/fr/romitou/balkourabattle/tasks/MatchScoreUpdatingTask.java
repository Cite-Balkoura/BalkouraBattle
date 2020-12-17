package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.query.MatchQuery;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.elements.Arena;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchScoreUpdatingTask extends BukkitRunnable {

    private final Match match;
    private final boolean endMatch;

    public MatchScoreUpdatingTask(Match match, boolean endMatch) {
        this.match = match;
        this.endMatch = endMatch;
    }

    @Override
    public void run() {
        // TODO: endMatch scores
        try {
            Match updateMatch = ChallongeManager.getChallonge().updateMatch(
                    match,
                    MatchQuery.builder()
                            .scoresCsv(match.getScoresCsv())
                            .build()
            );
            Arena arena = BattleManager.getArenaByMatchId(updateMatch.getId());
            if (arena != null)
                BattleManager.arenas.replace(arena, updateMatch);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
