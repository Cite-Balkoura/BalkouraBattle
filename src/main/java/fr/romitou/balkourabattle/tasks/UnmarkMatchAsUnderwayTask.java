package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.scheduler.BukkitRunnable;

public class UnmarkMatchAsUnderwayTask extends BukkitRunnable {

    private final Match match;

    public UnmarkMatchAsUnderwayTask(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().unmarkMatchAsUnderway(match);
        } catch (DataAccessException e) {
            e.printStackTrace();
            ChatManager.modAlert(
                    "Le match " + match.getId() + " n'a pas pu être marqué comme libre.",
                    "Assurez-vous que celui-ci est bien marqué comme étant libre sur Challonge."
            );
        }
    }
}
