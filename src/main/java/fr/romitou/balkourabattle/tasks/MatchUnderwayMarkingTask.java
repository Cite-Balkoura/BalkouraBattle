package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.utils.ChatUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchUnderwayMarkingTask extends BukkitRunnable {

    private final Match match;

    public MatchUnderwayMarkingTask(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().markMatchAsUnderway(match);
        } catch (DataAccessException e) {
            e.printStackTrace();
            ChatUtils.modAlert(e.getMessage());
        }
    }
}
