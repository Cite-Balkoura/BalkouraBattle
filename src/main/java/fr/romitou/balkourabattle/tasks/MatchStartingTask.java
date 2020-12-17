package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.elements.Arena;
import fr.romitou.balkourabattle.elements.ArenaStatus;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class MatchStartingTask extends BukkitRunnable {

    private final Match match;
    private final Arena arena;

    public MatchStartingTask(Match match, Arena arena) {
        this.match = match;
        this.arena = arena;
    }

    @Override
    public void run() {

        // Fetch players of this match.
        List<OfflinePlayer> offlinePlayers = BattleManager.getPlayers(match);
        if (offlinePlayers == null) {
            // TODO: alert
            return;
        }

        // Send match informations.
        offlinePlayers.forEach(player -> BattleManager.sendMatchInfo(player, match.getId()));

        BattleManager.arenas.remove(arena);
        arena.setArenaStatus(ArenaStatus.BUSY);
        BattleManager.arenas.put(arena, match);

        // Run a sync tasks as Bukkit isn't async safe.
        new MatchTeleportingTask(match, arena).runTask(BalkouraBattle.getInstance());
        BukkitTask bukkitTask = new MatchTimerTask(match, 60).runTaskTimer(BalkouraBattle.getInstance(), 0, 20);
        BattleManager.timers.put(match, bukkitTask.getTaskId());

        BattleManager.initPlayers(offlinePlayers);
        BattleManager.freeze.addAll(offlinePlayers);

    }

}
