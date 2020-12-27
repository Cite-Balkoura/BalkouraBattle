package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ParticipantDisconnectionTimerTask extends BukkitRunnable {


    private final Match match;
    private final List<OfflinePlayer> offlinePlayers;
    private int time;

    public ParticipantDisconnectionTimerTask(Match match, int time) {
        this.match = match;
        this.offlinePlayers = BattleManager.getPlayers(match);
        this.time = time;
    }

    @Override
    public void run() {
        if (time <= 0) {
            BattleHandler.handleDisconnectionTimerEnd(match, offlinePlayers);
            this.cancel();
        }
        if (BattleManager.intAnnouncements.contains(time))
            offlinePlayers.stream()
                    .filter(offlinePlayer -> offlinePlayer.getPlayer() != null)
                    .forEach(offlinePlayer -> offlinePlayer.getPlayer().sendMessage(ChatManager.getFormattedMessage(
                            "Victoire dans " + time + " seconde" + (time > 1 ? "s" : "") + "."
                    )));
        time--;
    }
}
