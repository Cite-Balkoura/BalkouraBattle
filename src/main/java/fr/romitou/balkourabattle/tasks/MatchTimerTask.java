package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchTimerTask extends BukkitRunnable {

    private final Match match;
    private final Player player1;
    private final Player player2;
    private int time;

    public MatchTimerTask(Match match, Player player1, Player player2, int time) {
        this.match = match;
        this.player1 = player1;
        this.player2 = player2;
        this.time = time;
    }

    @Override
    public void run() {
        if (time == 0) BattleHandler.handleEndMatch(match);
        player1.sendActionBar(ChatUtils.getFormattedMessage("Plus que " + time + "seconde" + (time < 1 ? "s" : "")));
        player2.sendActionBar(ChatUtils.getFormattedMessage("Plus que " + time + "seconde" + (time < 1 ? "s" : "")));
        time--;
    }

}
