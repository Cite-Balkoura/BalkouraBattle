package fr.romitou.balkourabattle.tasks;

//import com.google.api.client.util.ArrayMap;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.utils.ArenaUtils;
import fr.romitou.balkourabattle.utils.MatchUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class ChallongeSyncTask extends BukkitRunnable {

    private static final BalkouraBattle INSTANCE = BalkouraBattle.getInstance();

    @Override
    public void run() {
        if (BattleHandler.round < 1) return; // Don't continue if the tournament is not started.
        try {
            ChallongeManager.getChallonge().getMatches(ChallongeManager.getTournament()).forEach(match -> {

                System.out.println("match round:" + match.getRound());

                if (match.getUnderwayAt() != null) return; // The match is already marked as started.
                if (ArenaUtils.getAvailableArenas().size() == 0)
                    return; // Don't continue if there is no arenas available.

                Player[] players = MatchUtils.getPlayers(match);
                System.out.println("players:" + Arrays.toString(players));
                assert players[0] != null && players[1] != null;

                int arena = ArenaUtils.getRandomAvailableArena();
                BattleHandler.arenas.put(arena, match.getId());
                Location[] locations = ArenaUtils.getArenaLocations(arena);

                // Run a sync tasks as Bukkit isn't async safe.
                new ParticipantTeleportingTask(players[0], locations[0]).runTask(INSTANCE);
                new ParticipantTeleportingTask(players[1], locations[1]).runTask(INSTANCE);
                new MatchTimerTask(match, players[0], players[1], 30).runTaskTimer(INSTANCE, 0, 20);

                // Run an async task as this task can be asynchronous executed.
                new MarkMatchAsUnderwayTask(match).runTaskAsynchronously(INSTANCE);
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

}
