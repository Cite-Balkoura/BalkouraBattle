package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentStartingTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().startTournament(ChallongeManager.getTournament());
            // Update the state of the tournament.
            new TournamentFetchTask().runTaskAsynchronously(BalkouraBattle.getInstance());
        } catch (DataAccessException e) {
            e.printStackTrace();
            ChatManager.modAlert("Le tournois n'a pas pu être démarré.");
        }
    }
}
