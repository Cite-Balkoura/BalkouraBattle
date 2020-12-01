package fr.romitou.balkourabattle.tasks;

import fr.romitou.balkourabattle.BattleHandler;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EndMatchTimer extends BukkitRunnable {

    private final Integer matchId;
    private final Player player1;
    private final Player player2;
    private int time;

    public EndMatchTimer(Integer matchId, Player player1, Player player2, int time) {
        this.matchId = matchId;
        this.player1 = player1;
        this.player2 = player2;
        this.time = time;
    }

    @Override
    public void run() {
        if (time == 0) BattleHandler.handleEndMatch(matchId, player1, player2);
        player1.sendActionBar("a");
        player2.sendActionBar("a");
        time--;
    }

}
