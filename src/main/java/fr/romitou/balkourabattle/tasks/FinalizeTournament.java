package fr.romitou.balkourabattle.tasks;

import fr.romitou.balkourabattle.utils.JsonRequest;
import org.bukkit.scheduler.BukkitRunnable;

public class FinalizeTournament extends BukkitRunnable {
    @Override
    public void run() {
        JsonRequest.postJsonRequest("/finalize");
    }
}
