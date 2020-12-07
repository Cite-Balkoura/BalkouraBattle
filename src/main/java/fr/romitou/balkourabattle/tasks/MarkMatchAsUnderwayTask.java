package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.ChallongeManager;
import org.bukkit.scheduler.BukkitRunnable;

public class MarkMatchAsUnderwayTask extends BukkitRunnable {

    private final Match match;

    public MarkMatchAsUnderwayTask(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().markMatchAsUnderway(match);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
