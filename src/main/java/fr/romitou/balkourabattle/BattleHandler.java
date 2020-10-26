package fr.romitou.balkourabattle;

import java.util.HashMap;

public class BattleHandler {

    private static final HashMap<Integer, String> players = new HashMap<>();
    private static int round = 0;

    public static HashMap<Integer, String> getPlayers() {
        return players;
    }

    public static void addPlayer(Integer id, String player) {
        players.putIfAbsent(id, player);
    }

    public static Integer getRound() {
        return round;
    }

    public static void setRound(int newRound) {
        round = newRound;
    }

}
