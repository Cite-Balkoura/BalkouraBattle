package fr.romitou.balkourabattle.tasks;

import fr.romitou.balkourabattle.utils.JsonRequest;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ResetTournament extends BukkitRunnable {
    @Override
    public void run() {
        JsonRequest.postJsonRequest("tournaments/balkoura_1vs1_test/reset", new HashMap<>());
    }
}
