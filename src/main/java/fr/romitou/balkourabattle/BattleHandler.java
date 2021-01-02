package fr.romitou.balkourabattle;

import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.Participant;
import fr.romitou.balkourabattle.elements.Arena;
import fr.romitou.balkourabattle.elements.ArenaStatus;
import fr.romitou.balkourabattle.elements.MatchScore;
import fr.romitou.balkourabattle.tasks.MatchScoreUpdatingTask;
import fr.romitou.balkourabattle.tasks.MatchStartingTask;
import fr.romitou.balkourabattle.tasks.MatchStoppingTask;
import fr.romitou.balkourabattle.tasks.ParticipantDisconnectionTimerTask;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;

import java.util.List;

public class BattleHandler {

    private static final ViaAPI<?> VIA_API = Via.getAPI();

    public static void handleEndRound(Match match, long loserId, List<OfflinePlayer> offlinePlayers) {
        MatchScore matchScore = new MatchScore(match.getScoresCsv());

        // Update scores for the current set and the match's scores.
        matchScore.setWinnerSet(matchScore.getCurrentRound(), !(match.getPlayer1Id().equals(loserId)));
        matchScore.addRound();
        match.setScoresCsv(matchScore.getScoreCsv());

        if ((matchScore.getCurrentRound() >= 2
                && matchScore.getWinSets(true) != matchScore.getWinSets(false))
                || matchScore.getCurrentRound() >= 3) {
            new MatchStoppingTask(match).runTaskAsynchronously(BalkouraBattle.getInstance());
            return;
        }

        // Send them to Challonge for a live update.
        new MatchScoreUpdatingTask(match, false).runTaskAsynchronously(BalkouraBattle.getInstance());

        Arena arena = BattleManager.getArenaByMatchId(match.getId());
        if (arena == null) {
            ChatManager.modAlert("L'arène du match " + match.getIdentifier() + " n'a pas pu être trouvé."
                    + "Le match est donc toujours en cours et doit être stoppé."
            );
            return;
        }

        offlinePlayers.stream()
                .filter(offlinePlayer -> offlinePlayer.getPlayer() != null)
                .forEach(player -> player.getPlayer().sendMessage(BattleManager.matchInfo(match).toArray(new String[0])));

        // Renewing the match as the score requirements are not meet.
        new MatchStartingTask(match, arena).runTaskAsynchronously(BalkouraBattle.getInstance());
    }

    public static void handleDeath(Player player) {
        if (!BattleManager.containsName(player.getName())) return;
        Participant participant = BattleManager.getParticipant(player.getUniqueId());
        if (participant == null) return;
        Match match = BattleManager.getCurrentMatchByPlayerId(participant.getId());
        if (match == null) {
            ChatManager.modAlert("Le match du joueur " + player.getName() + " n'a pas été trouvé."
                    + "Le match est donc toujours en cours et doit être stoppé."
            );
            return;
        }
        List<OfflinePlayer> offlinePlayers = BattleManager.getPlayers(match);
        if (offlinePlayers == null) {
            ChatManager.modAlert("Les joueurs du match " + match.getIdentifier() + " n'ont pas pu être trouvés."
                    + "Le match est donc toujours en cours et doit être stoppé."
            );
            return;
        }

        // Something went wrong as there's no two players. Someone probably disconnected. Abort.
        if (!(offlinePlayers.stream().filter(offlinePlayer -> offlinePlayer.getPlayer() != null).count() == 2)) {
            ChatManager.modAlert("Un joueur s'est probablement déconnecté du match " + match.getIdentifier() + "."
                    + "Le match est donc toujours en cours et doit être stoppé."
            );
            return;
        }

        BattleManager.stopTimer(match);
        handleEndRound(match, participant.getId(), offlinePlayers);
    }

    public static void handleJoin(Player player) {
        Participant participant = BattleManager.getParticipant(player.getUniqueId());
        if (participant == null) {
            if (BattleManager.isTournamentStarted()) {
                ChatManager.sendMessage(player, "Le tournois a déjà commencé ! Vous êtes spectateur.");
            } else {
                ChatManager.sendMessage(player, "Bienvenue, le tournois va commencer dans quelques instants.");
            }
        } else {
            Match match = BattleManager.getCurrentMatchByPlayerId(participant.getId());
            if (match != null)
                handleConnectWhileFighting(match, player);
        }

        player.teleport(BattleManager.getSpawn());
        player.setGameMode(GameMode.SPECTATOR);

        if (VIA_API.getPlayerVersion(player.getUniqueId()) >= 48)
            player.sendMessage("§c§LATTENTION! §7Il vous est fortement recommandé vous connecter avec le client Minecraft officiel en 1.8 afin de bénéficier du PvP 1.8 et d'éviter quelques déconnexions imprévues lors de vos combats.");
    }

    public static void handleDisconnect(Player player) {
        System.out.println("Handle disconnect for " + player.getName());
        Participant participant = BattleManager.getParticipant(player.getUniqueId());
        if (participant == null) {
            ChatManager.modAlert("Le participant pour le joueur " + player.getName() + " n'a pas été trouvé."
                    + "Le match est donc toujours en cours et doit être stoppé."
            );
            return;
        }
        Match match = BattleManager.getCurrentMatchByPlayerId(participant.getId());
        if (match == null) {
            ChatManager.modAlert("Le match du joueur " + participant.getName() + " n'a pas été trouvé." +
                    "Le match est donc toujours en cours et doit être stoppé.");
            return;
        }
        handleDisconnectWhileFighting(match);
    }

    public static void handleConnectWhileFighting(Match match, Player player) {
        BattleManager.stopDisconnectionTimer(match);
        List<OfflinePlayer> offlinePlayers = BattleManager.getPlayers(match);
        if (offlinePlayers == null) {
            ChatManager.modAlert("Les joueurs du match " + match.getIdentifier() + " n'ont pas été trouvé."
                    + "Le match n'a donc pas redémarré."
            );
            return;
        }
        Arena arena = BattleManager.getArenaByMatchId(match.getId());
        if (arena == null || arena.getArenaStatus() != ArenaStatus.BUSY) {
            ChatManager.modAlert("Une erreur avec le match " + match.getIdentifier() + " est survenue."
                    + "Le match n'a donc pas redémarré."
            );
            return;
        }
        offlinePlayers.stream()
                .filter(offlinePlayer -> offlinePlayer.getPlayer() != null)
                .forEach(offlinePlayer -> ChatManager.sendMessage(
                        offlinePlayer.getPlayer(),
                        offlinePlayer.getPlayer().equals(player)
                                ? "Ouf, vous revoilà. Reprise de votre match dans quelques instants, préparez-vous ! " +
                                "Attention, une seconde déconnexion résultera de la victoire instantanée de votre " +
                                "adversaire."
                                : "Votre adversaire s'est reconnecté ! Préparez-vous à la reprise de votre match ..."
                ));
        new MatchStartingTask(match, arena).runTaskLaterAsynchronously(BalkouraBattle.getInstance(), 100);
    }

    public static void handleDisconnectWhileFighting(Match match) {
        BattleManager.stopTimer(match);
        Arena arena = BattleManager.getArenaByMatchId(match.getId());
        if (arena == null || arena.getArenaStatus() != ArenaStatus.BUSY) {
            ChatManager.modAlert("Une erreur avec le match " + match.getIdentifier() + " est survenue."
                    + "Le match n'a donc pas été arrêté."
            );
            return;
        }
        List<OfflinePlayer> offlinePlayers = BattleManager.getPlayers(match);
        if (offlinePlayers == null) {
            ChatManager.modAlert("Les joueurs du match " + match.getIdentifier() + " n'ont pas été trouvé."
                    + "Le match n'a donc pas été arrêté."
            );
            return;
        }
        OfflinePlayer disconnectedPlayer = offlinePlayers.stream()
                .filter(offlinePlayer -> offlinePlayer.getPlayer() == null)
                .findFirst()
                .orElse(null);
        if (disconnectedPlayer == null) {
            ChatManager.modAlert("Les joueur déconnecté du match " + match.getIdentifier() + " n'a pas été trouvé."
                    + "Le match n'a donc pas été arrêté."
            );
            return;
        }
        offlinePlayers.stream()
                .filter(offlinePlayer -> offlinePlayer.getPlayer() != null)
                .forEach(offlinePlayer -> offlinePlayer.getPlayer().sendMessage(
                        "Votre adversaire s'est déconnecté. Celui-ci doit se reconnecter dans la minute, " +
                                "ou vous serez désigné comme vainqueur de cette manche. Merci de ne pas vous déconnecter."
                ));
        BukkitTask bukkitTask = new ParticipantDisconnectionTimerTask(match, 60)
                .runTaskTimerAsynchronously(BalkouraBattle.getInstance(), 0, 20);
        BattleManager.disconnections.put(match, bukkitTask.getTaskId());
        int disconnections = BattleManager.playerDisconnections.getOrDefault(disconnectedPlayer, 0);
        disconnections++;
        BattleManager.playerDisconnections.put(disconnectedPlayer, disconnections);
    }

    public static void handleDisconnectionTimerEnd(Match match, List<OfflinePlayer> offlinePlayers) {
        OfflinePlayer disconnectedPlayer = offlinePlayers.stream()
                .filter(offlinePlayer -> offlinePlayer.getPlayer() == null)
                .findFirst()
                .orElse(null);
        if (disconnectedPlayer == null) {
            ChatManager.modAlert(
                    "Le joueur déconnecté du match " + match.getIdentifier() + " n'a pas pu être récupéré.",
                    "Le match n'a pas été continué. Merci de redémarrer le match via Challonge."
            );
            return;
        }
        int disconnectionCount = BattleManager.playerDisconnections.getOrDefault(disconnectedPlayer, 1);
        Participant participant = BattleManager.getParticipant(disconnectedPlayer.getUniqueId());
        if (participant == null) {
            ChatManager.modAlert(
                    "Le participant associé au joueur déconnecté du match " + match.getIdentifier(),
                    "n'a pas pu être récupéré, le match n'a pas été continué. Merci de le redémarrer via Challonge."
            );
            return;
        }
        if (disconnectionCount == 1)
            handleEndRound(match, participant.getId(), offlinePlayers);
        else if (disconnectionCount >= 2)
            new MatchStoppingTask(match).runTaskAsynchronously(BalkouraBattle.getInstance());
    }

}
