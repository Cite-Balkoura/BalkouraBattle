package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.elements.Arena;
import fr.romitou.balkourabattle.elements.ArenaStatus;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MatchesUpdateTask extends BukkitRunnable {

    // Get all matches which are open and assignable to an arena.
    @Override
    public void run() {
        List<Arena> arenas = BattleManager.getAvailableArenas();
        List<Match> matches = BattleManager.getWaitingMatches();

        // Stop the assignation when there's no arenas or matches left.
        for (int i = 0; i < arenas.size() && i < matches.size(); i++) {
            Arena arena = arenas.get(i);
            Match match = matches.get(i);

            // Modify the status of the arena.
            BattleManager.arenas.remove(arena);
            arena.setArenaStatus(ArenaStatus.VALIDATING);
            BattleManager.arenas.put(arena, match);

            // Make the match as underway.
            new MatchUnderwayMarkingTask(match).runTaskAsynchronously(BalkouraBattle.getInstance());

            // Remove this match from the waiting list.
            BattleManager.waitingMatches.remove(match);
        }
    }

}
