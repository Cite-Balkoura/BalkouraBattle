package fr.romitou.balkourabattle.tasks;

import com.google.api.client.util.ArrayMap;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.utils.ArenaUtils;
import fr.romitou.balkourabattle.utils.JsonRequest;
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
            Player player1 = BattleHandler.getPlayers().get(((BigDecimal) m.get("player1_id")).intValueExact());
            Player player2 = BattleHandler.getPlayers().get(((BigDecimal) m.get("player2_id")).intValueExact());
            assert player1 != null && player2 != null;
            int arena = ArenaUtils.getRandomAvailableArena();
            int matchId = ((BigDecimal) m.get("id")).intValue();
            BattleHandler.addArena(arena, matchId);
            Location[] locations = ArenaUtils.getArenaLocations(arena);
            CompletableFuture.allOf(
                    player1.teleportAsync(locations[0]),
                    player2.teleportAsync(locations[1])
            ).thenRunAsync(new MarkMatchAsUnderway(matchId));
        });
    }
}
