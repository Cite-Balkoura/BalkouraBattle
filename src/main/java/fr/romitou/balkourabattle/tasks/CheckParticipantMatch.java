package fr.romitou.balkourabattle.tasks;

import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.utils.JsonRequest;
import fr.romitou.balkourabattle.utils.ParticipantCheckType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

public class CheckParticipantMatch extends BukkitRunnable {

    private final Player player;
    private final ParticipantCheckType checkType;

    public CheckParticipantMatch(Player player, ParticipantCheckType checkType) {
        this.player = player;
        this.checkType = checkType;
    }

    @Override
    public void run() {
        Integer playerId = BattleHandler.getPlayers().inverse().get(this.player);
        assert playerId != null;
        JSONObject participant = JsonRequest.getJsonRequest("/participants/" + playerId);
        Integer matchId = (Integer) participant.get("id");
        assert matchId != null;
        JSONObject match = JsonRequest.getJsonRequest("/matches/" + matchId);
        if (this.checkType == ParticipantCheckType.DISCONNECTED) BattleHandler.handleDisconnect(match, player);
    }
}
