package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.ChatManager;
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
            ChatManager.modAlert(
                    "Le match " + match.getId() + " n'a pas pu être marqué comme en cours.",
                    "Assurez-vous que celui-ci est bien marqué comme étant en cours sur Challonge."
            );
        }
    }
}
