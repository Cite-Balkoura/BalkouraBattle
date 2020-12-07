package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.ChallongeManager;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticipantsRandomizationTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().randomizeParticipants(ChallongeManager.getTournament());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
