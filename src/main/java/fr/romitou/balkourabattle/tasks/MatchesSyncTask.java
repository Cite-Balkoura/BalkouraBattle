package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchesSyncTask extends BukkitRunnable {

    // Get all future matches to sync them with local datas.
    @Override
    public void run() {
        try {
            BattleManager.waitingMatches.clear();
            BattleManager.waitingMatches.addAll(
                    ChallongeManager.getChallonge().getMatches(ChallongeManager.getTournament())
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            ChatManager.modAlert(
                    "Les matchs n'ont pas pu être synchronisé avec Challonge.",
                    "Cette tâche est récurrente et sera exécutée à nouveau dans 10 secondes."
            );
        }
    }

}
