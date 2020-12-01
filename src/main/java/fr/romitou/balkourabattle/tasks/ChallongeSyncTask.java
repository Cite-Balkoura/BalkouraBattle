package fr.romitou.balkourabattle.tasks;

import com.google.api.client.util.ArrayMap;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.utils.ArenaUtils;
import fr.romitou.balkourabattle.utils.JsonRequest;
import fr.romitou.balkourabattle.utils.MatchUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class ChallongeSyncTask extends BukkitRunnable {

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        if (BattleHandler.getRound() < 1) return;
        JSONArray matches = JsonRequest.getJsonRequest("/matches", true);
        assert matches != null;
        matches.forEach(match -> {
            ArrayMap<?, ?> m = (ArrayMap<?, ?>) ((ArrayMap<?, ?>) match).get("match");
            if (m.get("round") != BattleHandler.getRound()) return;
            if (ArenaUtils.getAvailableArenas().size() == 0) return;
            Player[] players = MatchUtils.getPlayers(m);
            assert players[0] != null && players[1] != null;
            int arena = ArenaUtils.getRandomAvailableArena();
            int matchId = ((BigDecimal) m.get("id")).intValue();
            BattleHandler.addArena(arena, matchId);
            Location[] locations = ArenaUtils.getArenaLocations(arena);
            CompletableFuture.allOf(
                    players[0].teleportAsync(locations[0]),
                    players[1].teleportAsync(locations[1])
            );
            new MarkMatchAsUnderway(matchId).runTaskAsynchronously(BalkouraBattle.getInstance());
            new EndMatchTimer(matchId, players[0], players[1], 30).runTaskTimerAsynchronously(BalkouraBattle.getInstance(), 0, 1000);
        });
    }
}
