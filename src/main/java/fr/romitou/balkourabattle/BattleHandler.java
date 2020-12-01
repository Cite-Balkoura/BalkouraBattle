package fr.romitou.balkourabattle;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import fr.romitou.balkourabattle.tasks.UpdateMatchScore;
import fr.romitou.balkourabattle.utils.ArenaUtils;
import fr.romitou.balkourabattle.utils.ChatUtils;
import fr.romitou.balkourabattle.utils.MatchUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.concurrent.CompletableFuture;

public class BattleHandler {

    private static final BiMap<Integer, String> players = HashBiMap.create();
    private static final BiMap<Integer, Integer> arenas = HashBiMap.create();
    private static Integer round = 0;

    public static BiMap<Integer, String> getPlayers() {
        return players;
    }

    public static Player getPlayer(Integer id) {
        if (!players.containsKey(id))
            return null;
        String player = players.get(id);
        return Bukkit.getPlayerExact(player);
    }

    public static void addPlayer(Integer id, String name) {
        players.putIfAbsent(id, name);
    }

    public static BiMap<Integer, Integer> getArenas() {
        return arenas;
    }

    public static void addArena(Integer id, Integer matchId) {
        arenas.put(id, matchId);
    }

    public static Integer getRound() {
        return round;
    }

    public static void setRound(Integer newRound) {
        round = newRound;
    }

    public static void handleDeath(JSONObject match, Player player1, Player player2, Player attacker) {
        Integer[] scores = MatchUtils.getScores(match);
        if (player1.getName().equals(attacker.getName())) {
            scores[0]++;
        } else {
            assert player2.getName().equals(attacker.getName());
            scores[1]++;
        }
        new UpdateMatchScore(
                MatchUtils.getId(match),
                scores[0],
                scores[1]
        ).runTaskAsynchronously(BalkouraBattle.getInstance());
    }

    /**
     * This method handle the disconnection of a player who is in a match.
     *
     * @param match        The JSONObject of the match.
     * @param disconnected The player who have disconnecting when fighting.
     * @param opponent     The opponent of the player who disconnecting.
     */
    public static void handleDisconnect(JSONObject match, Player disconnected, Player opponent) {
        if (match.get("underway_at") != null)
            ChatUtils.sendMessage(opponent, "Votre adversaire s'est déconnecté. Il peut revenir jusqu'à la fin du match ou vous serez désigné comme vainqueur.");
    }

    /**
     * This method handle the (re)connection of a player who was before in a match.
     * It will allow to renew the match by teleporting each other.
     *
     * @param match   The JSONObject of the match.
     * @param player1 The first player of the match.
     * @param player2 The second player of the match.
     */
    public static void handleConnect(JSONObject match, Player player1, Player player2) {
        if (match.get("underway_at") != null)
            renewMatch(MatchUtils.getId(match), player1, player2);
    }

    /**
     * This method allow to reset, renew a match by teleporting the players in the default spawn locations.
     * It would be useful to restart a match equitably, but not the timer.
     *
     * @param matchId The identifier of the match.
     * @param player1 The first player of the match.
     * @param player2 The second player of the match.
     */
    public static void renewMatch(Integer matchId, Player player1, Player player2) {
        ChatUtils.sendMessage(player1, "Votre adversaire s'est reconnecté, au combat !");
        ChatUtils.sendMessage(player2, "Vous avez été téléporté à votre arène. Le combat continue !");
        Location[] locations = ArenaUtils.getArenaLocations(getArenas().inverse().get(matchId));
        CompletableFuture.allOf(
                player1.teleportAsync(locations[0]),
                player2.teleportAsync(locations[1])
        );
    }

    /**
     * This method handle the end of a match.
     *
     * @param matchId The identifier of the match.
     * @param player1 The first player of the match.
     * @param player2 The second player of the match.
     */
    public static void handleEndMatch(Integer matchId, Player player1, Player player2) {
    }
}
