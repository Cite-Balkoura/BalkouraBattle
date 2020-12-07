package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.ChallongeManager;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentResettingTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            BattleHandler.players.clear();
            ChallongeManager.getChallonge().resetTournament(ChallongeManager.getTournament());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

}
