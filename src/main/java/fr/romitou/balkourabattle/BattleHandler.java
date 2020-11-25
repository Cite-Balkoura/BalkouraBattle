package fr.romitou.balkourabattle;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import fr.romitou.balkourabattle.utils.ChatUils;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class BattleHandler {

    private static final BiMap<Integer, Player> players = HashBiMap.create();
    private static final HashMap<Integer, Integer> arenas = new HashMap<>();
    private static Integer round = 0;

    public static BiMap<Integer, Player> getPlayers() {
        return players;
    }

    public static void addPlayer(Integer id, Player player) {
        players.putIfAbsent(id, player);
    }

    public static HashMap<Integer, Integer> getArenas() {
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

    public static void handleDisconnect(JSONObject match, Player player) {
        Player opponent = getOpponent(match, player);
        ChatUils.sendMessage(opponent, "Votre adversaire s'est déconnecté. Il peut revenir jusqu'à la fin du match ou vous serez désigné comme vainqueur.");
    }

    public static Player getOpponent(JSONObject match, Player player) {
        Player player1 = getPlayers().get((int) match.get("player1_id"));
        if (!player.getName().equals(player1.getName()))
            return player1;
        return getPlayers().get((int) match.get("player2_id"));
    }

}
