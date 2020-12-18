package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.elements.Arena;
import fr.romitou.balkourabattle.elements.ArenaStatus;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchRequestCallbackTask extends BukkitRunnable {

    private final Player player;
    private final long matchId;

    public MatchRequestCallbackTask(Player player, long matchId) {
        this.player = player;
        this.matchId = matchId;
    }

    @Override
    public void run() {
        Match match = BattleManager.getHandledMatch(matchId);
        if (match == null) {
            ChatManager.sendMessage(player, "Ce match est introuvable (" + matchId + ").");
            return;
        }
        if (match.getUnderwayAt() != null) {
            ChatManager.sendMessage(player, "Ce match a déjà été validé par un autre modérateur.");
            return;
        }
        Arena arena = BattleManager.getArenaByMatchId(match.getId());
        if (arena == null || arena.getArenaStatus() != ArenaStatus.VALIDATING) {
            ChatManager.sendMessage(player, "L'arène associée à ce match ne peut plus être utilisée.");
            BattleManager.arenas.remove(arena);
            BattleManager.arenas.put(arena, null);
            return;
        }
        new MatchStartingTask(match, arena).runTaskAsynchronously(BalkouraBattle.getInstance());
    }
}
