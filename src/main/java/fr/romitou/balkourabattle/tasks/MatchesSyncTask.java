package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.ChallongeManager;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchesSyncTask extends BukkitRunnable {

    // Get all future matches to sync them with local datas.
    @Override
    public void run() {
        try {
            BattleManager.waitingMatches.clear();
            BattleManager.waitingMatches.addAll(
                    ChallongeManager.getChallonge().getMatches(ChallongeManager.getTournament())
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            // Don't force retry this task as it's running periodically.
        }
    }

}
