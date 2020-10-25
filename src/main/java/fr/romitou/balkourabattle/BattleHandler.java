package fr.romitou.balkourabattle;

import java.util.HashMap;

public class BattleHandler {

    private static final HashMap<String, Integer> players = new HashMap<>();

    public static HashMap<String, Integer> getPlayers() {
        return players;
    }

    public static void addPlayer(String player, Integer id) {
        players.putIfAbsent(player, id);
    }
}
