package fr.romitou.balkourabattle.tasks;

import fr.romitou.balkourabattle.utils.JsonRequest;
import org.bukkit.scheduler.BukkitRunnable;

public class MarkMatchAsUnderway extends BukkitRunnable {

    private final int matchId;

    public MarkMatchAsUnderway(Integer matchId) {
        this.matchId = matchId;
    }

    @Override
    public void run() {
        JsonRequest.postJsonRequest("/matches/" + matchId + "/mark_as_underway");
    }
}
