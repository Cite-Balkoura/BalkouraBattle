package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.elements.Arena;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.List;

public class MatchTeleportingTask extends BukkitRunnable {

    private final Match match;
    private Arena arena;

    public MatchTeleportingTask(Match match, @Nullable Arena arena) {
        this.match = match;
        this.arena = arena;
    }

    @Override
    public void run() {
        if (arena == null) arena = BattleManager.getArenaByMatchId(match.getId());
        List<OfflinePlayer> offlinePlayers = BattleManager.getPlayers(match);
        if (offlinePlayers == null) {
            // TODO: alert
            return;
        }
        Location[] locations = arena.getLocations();
        for (int i = 0; i < locations.length && i < offlinePlayers.size(); i++) {
            OfflinePlayer offlinePlayer = offlinePlayers.get(i);
            if (offlinePlayer.getPlayer() != null)
                offlinePlayer.getPlayer().teleportAsync(locations[i]);
        }
    }
}
