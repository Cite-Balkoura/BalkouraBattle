package fr.romitou.balkourabattle.tasks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticipantTeleportingTask extends BukkitRunnable {

    private final Player player;
    private final Location location;

    public ParticipantTeleportingTask(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

    @Override
    public void run() {
        player.teleportAsync(location);
    }
}
