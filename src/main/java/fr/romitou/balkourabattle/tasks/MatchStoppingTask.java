package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.query.MatchQuery;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.elements.Arena;
import fr.romitou.balkourabattle.elements.ArenaStatus;
import fr.romitou.balkourabattle.elements.MatchScore;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class MatchStoppingTask extends BukkitRunnable {

    private final Match match;
    private static Boolean isPlayer1;

    public MatchStoppingTask(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        try {
            MatchScore matchScore = new MatchScore(match.getScoresCsv());

            // There scores are ex aequo. This can happen when a player disconnect and not reconnect, so we need
            // to take the last set winner (as he doesn't disconnected).
            if (matchScore.getWinSets(true) == matchScore.getWinSets(false))
                isPlayer1 = matchScore.getSet(matchScore.getCurrentRound()).getScore(0) == 1;

            isPlayer1 = matchScore.getWinSets(true) > matchScore.getWinSets(false);
            ChallongeManager.getChallonge().updateMatch(
                    match,
                    MatchQuery.builder()
                            .scoresCsv(match.getScoresCsv())
                            .winnerId(isPlayer1 ? match.getPlayer1Id() : match.getPlayer2Id())
                            .build()
            );
            ChallongeManager.getChallonge().unmarkMatchAsUnderway(match);
            Arena arena = BattleManager.getArenaByMatchId(match.getId());
            if (arena != null) {
                BattleManager.arenas.remove(arena);
                arena.setArenaStatus(ArenaStatus.FREE);
                BattleManager.arenas.put(arena, null);
            } else {
                // TODO: alert
            }
            List<OfflinePlayer> offlinePlayers = BattleManager.getPlayers(match);
            if (offlinePlayers == null) {
                // TODO: alert
                return;
            }
            OfflinePlayer loser = BattleManager.getPlayer(isPlayer1 ? match.getPlayer2Id() : match.getPlayer1Id());
            if (loser == null) {
                // TODO: alert
                return;
            }
            offlinePlayers.stream()
                    .filter(player -> player.getPlayer() != null)
                    .forEach(player -> ChatManager.sendMessage(player.getPlayer(),
                            Objects.equals(loser.getName(), player.getName())
                                    ? "Vous avez §cperdu§f ce match. Vous êtes spectateur."
                                    : "Vous avez §agagné§f ce match. Préparez-vous pour le prochain !"
                    ));
            if (loser.getPlayer() != null)
                BattleManager.makeSpectator(loser.getPlayer());
            // TODO: send messages
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
