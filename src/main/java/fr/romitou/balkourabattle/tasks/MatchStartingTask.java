package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.elements.Arena;
import fr.romitou.balkourabattle.elements.ArenaStatus;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.stream.Collectors;

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

        System.out.println("Arena " + arena.getId() + " marked as busy.");

        List<Player> onlinePlayers = offlinePlayers.stream()
                .filter(player -> player.getPlayer() != null && player.getPlayer().isOnline())
                .map(OfflinePlayer::getPlayer)
                .collect(Collectors.toList());
        if (onlinePlayers.size() != 2) {
            if (onlinePlayers.size() == 1) {
                System.out.println("One player when starting match " + match.getId() + ". Handle disconnection.");
                BattleHandler.handleDisconnectWhileFighting(match);
            }
            else if (onlinePlayers.size() == 0) {
                System.out.println("No player when starting match " + match.getId() + ". Disqualification.");
                new ParticipantInactiveTask(BattleManager.getParticipant(offlinePlayers.get(0).getUniqueId()))
                        .runTaskAsynchronously(BalkouraBattle.getInstance());
                new ParticipantInactiveTask(BattleManager.getParticipant(offlinePlayers.get(1).getUniqueId()))
                        .runTaskAsynchronously(BalkouraBattle.getInstance());
                BattleManager.arenas.remove(arena);
                arena.setArenaStatus(ArenaStatus.FREE);
                BattleManager.arenas.put(arena, null);
            }
            return;
        }

        // Run a sync tasks as Bukkit isn't async safe.
        new MatchTeleportingTask(match, arena).runTask(BalkouraBattle.getInstance());
        BukkitTask bukkitTask = new MatchTimerTask(match, 60).runTaskTimer(BalkouraBattle.getInstance(), 0, 20);
        BattleManager.timers.put(match, bukkitTask.getTaskId());

        BattleManager.initPlayers(offlinePlayers);
        BattleManager.freeze.addAll(offlinePlayers);
        System.out.println("Match " + match.getId() + " started.");

    }

}
