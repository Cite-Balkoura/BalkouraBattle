package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MatchTimerTask extends BukkitRunnable {

    private final Match match;
    private final List<OfflinePlayer> offlinePlayers;
    private int fightTime, idleTime, endTime;

    public MatchTimerTask(Match match, int fightTime) {
        this.match = match;
        this.offlinePlayers = BattleManager.getPlayers(match);
        this.fightTime = fightTime;
        this.idleTime = 10;
        this.endTime = 30;
    }

    @Override
    public void run() {
        if (idleTime <= 0) {
            if (fightTime <= 0) {
                BattleManager.deathMatch(offlinePlayers
                        .stream()
                        .map(OfflinePlayer::getPlayer)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            } else {
                if (fightTime == 60)
                    BattleManager.freeze.removeAll(offlinePlayers);
                offlinePlayers.stream()
                        .filter(player -> player.getPlayer() != null)
                        .forEach(player -> player.getPlayer().sendActionBar(ChatManager.getFormattedMessage(
                                "§cDeath match §fdans " + fightTime + " seconde" + (fightTime > 1 ? "s" : "")) + ".")
                        );
                fightTime--;
            }
        } else {
            offlinePlayers.stream()
                    .filter(player -> player.getPlayer() != null)
                    .forEach(player -> player.getPlayer().sendActionBar(ChatManager.getFormattedMessage(
                            "Début du combat dans " + idleTime + " seconde" + (fightTime > 1 ? "s" : "")) + ".")
                    );
            idleTime--;
        }
    }

}
