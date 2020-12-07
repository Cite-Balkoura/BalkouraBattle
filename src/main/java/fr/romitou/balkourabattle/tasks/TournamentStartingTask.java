package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.ChallongeManager;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentStartingTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().startTournament(ChallongeManager.getTournament());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
