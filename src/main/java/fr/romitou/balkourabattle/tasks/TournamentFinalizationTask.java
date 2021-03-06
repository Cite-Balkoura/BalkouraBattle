package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentFinalizationTask extends BukkitRunnable {
    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().finalizeTournament(ChallongeManager.getTournament());
        } catch (DataAccessException e) {
            e.printStackTrace();
            ChatManager.modAlert("Le tournois n'a pas pu être marqué comme terminé.");
        }
    }
}
