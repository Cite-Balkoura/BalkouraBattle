package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.utils.ChatUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentResettingTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().resetTournament(ChallongeManager.getTournament());
            // Update the state of the tournament.
            new TournamentFetchTask().runTaskAsynchronously(BalkouraBattle.getInstance());
        } catch (DataAccessException e) {
            e.printStackTrace();
            ChatUtils.modAlert(e.getMessage());
        }
    }

}
