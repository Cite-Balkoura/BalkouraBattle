package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.query.MatchQuery;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.ChatManager;
import fr.romitou.balkourabattle.elements.Arena;
import fr.romitou.balkourabattle.elements.ArenaStatus;
import fr.romitou.balkourabattle.elements.MatchScore;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MatchStoppingTask extends BukkitRunnable {

    private static Boolean isPlayer1;
    private final Match match;

    public MatchStoppingTask(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        try {
            List<OfflinePlayer> players = BattleManager.getPlayers(match);
            if (players == null) {
                return;
            }

            MatchScore matchScore = new MatchScore(match.getScoresCsv());

            // These scores are ex aequo. This can happen when a player disconnect and not reconnect, so we need
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
                ChatManager.modAlert("L'arène du match " + match.getIdentifier() + " n'a pas été trouvé.",
                        "Vous devez insérer ces scores manuellement : " + matchScore.getScoreCsv() + "."
                );
                return;
            }
            List<OfflinePlayer> offlinePlayers = BattleManager.getPlayers(match);
            if (offlinePlayers == null) {
                ChatManager.modAlert("Impossible de récupérer les joueurs du match " + match.getIdentifier() + ".",
                        "Assurez-vous qu'ils soient enregistrés avec /battle register.",
                        "Vous devez insérer ces scores manuellement : " + matchScore.getScoreCsv() + "."
                );
                return;
            }
            OfflinePlayer loser = BattleManager.getPlayer(isPlayer1 ? match.getPlayer2Id() : match.getPlayer1Id());
            if (loser == null) {
                ChatManager.modAlert("Impossible de récupérer le perdant du match " + match.getIdentifier() + ".",
                        "Vous devez insérer ces scores manuellement : " + matchScore.getScoreCsv() + "."
                );
                return;
            }
            OfflinePlayer winner = BattleManager.getPlayer(isPlayer1 ? match.getPlayer1Id() : match.getPlayer2Id());
            if (winner == null) {
                ChatManager.modAlert("Impossible de récupérer le gagnant du match " + match.getIdentifier() + ".",
                        "Vous devez insérer ces scores manuellement : " + matchScore.getScoreCsv() + "."
                );
                return;
            }
            Bukkit.getScheduler().runTask(BalkouraBattle.getInstance(), () -> {
                if (winner.getPlayer() != null) {
                    winner.getPlayer().setGameMode(GameMode.SPECTATOR);
                    winner.getPlayer().sendMessage("§a§lBRAVO! §7Vous avez §agagné§7 ce match. Votre prochain match va démarrer dans quelques instants.");
                    winner.getPlayer().getInventory().clear();
                    winner.getPlayer().getActivePotionEffects()
                            .forEach(potionEffect -> winner.getPlayer().removePotionEffect(potionEffect.getType()));
                    winner.getPlayer().teleport(BattleManager.getSpawn());
                }
                if (loser.getPlayer() != null) {
                    loser.getPlayer().setGameMode(GameMode.SPECTATOR);
                    loser.getPlayer().sendMessage("§c§lPERDU! §7Vous avez §cperdu§7 ce match. Vous êtes désormais spectateur, vous pouvez visiter les différentes arèens.");
                    loser.getPlayer().getInventory().clear();
                    loser.getPlayer().getActivePotionEffects()
                            .forEach(potionEffect -> loser.getPlayer().removePotionEffect(potionEffect.getType()));
                    loser.getPlayer().teleport(BattleManager.getSpawn());
                }
            });
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("§5§lGG! §d" + winner.getName() + "§7 vient de remporter le match contre §d" + loser.getName() + "§7."));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
