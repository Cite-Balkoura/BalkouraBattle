package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.ChatManager;
import fr.romitou.balkourabattle.elements.Arena;
import fr.romitou.balkourabattle.elements.ArenaStatus;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
            ChatManager.modAlert("Impossible de récupérer les joueurs du match " + match.getIdentifier() + ".",
                    "Assurez-vous qu'ils soient enregistrés avec /battle register."
            );
            return;
        }

        BattleManager.arenas.remove(arena);
        arena.setArenaStatus(ArenaStatus.BUSY);
        BattleManager.arenas.put(arena, match);

        System.out.println("Arena " + arena.getId() + " marked as busy.");

        List<Player> onlinePlayers = offlinePlayers.stream()
                .filter(player -> player.getPlayer() != null && player.getPlayer().isOnline())
                .map(OfflinePlayer::getPlayer)
                .collect(Collectors.toList());
        Bukkit.getScheduler().runTask(BalkouraBattle.getInstance(), () -> {
            onlinePlayers.forEach(player -> player.setGameMode(GameMode.SURVIVAL));
        });
        if (onlinePlayers.size() != 2) {
            if (onlinePlayers.size() == 1) {
                BattleHandler.handleDisconnectWhileFighting(match);
            } else if (onlinePlayers.size() == 0) {
                BattleManager.getOnlineModerators().forEach(player -> ChatManager.sendMessage(player, "Aucun joueur en ligne lors du démarrage du match " + match.getIdentifier() + ". Les joueurs ont été disqualifiés."));
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
        BattleManager.combat.addAll(offlinePlayers);

        BattleManager.getOnlineModerators().forEach(player -> ChatManager.sendMessage(player, "Le match " + match.getIdentifier() + " vient de démarrer."));
    }

}
