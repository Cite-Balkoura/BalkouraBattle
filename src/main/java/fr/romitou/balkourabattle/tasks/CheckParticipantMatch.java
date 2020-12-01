package fr.romitou.balkourabattle.tasks;

import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.utils.JsonRequest;
import fr.romitou.balkourabattle.utils.MatchUtils;
import fr.romitou.balkourabattle.utils.ParticipantCheckType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.lang.reflect.MalformedParametersException;

public class CheckParticipantMatch extends BukkitRunnable {

    private final Player player;
    private final ParticipantCheckType checkType;

    public CheckParticipantMatch(Player player, ParticipantCheckType checkType) {
        this.player = player;
        this.checkType = checkType;
    }

    @Override
    public void run() {
        Integer playerId = BattleHandler.getPlayers().inverse().get(player.getName());
        assert playerId != null;
        JSONObject participant = JsonRequest.getJsonRequest("/participants/" + playerId);
        Integer matchId = (Integer) participant.get("id");
        assert matchId != null;
        JSONObject match = JsonRequest.getJsonRequest("/matches/" + matchId);
        Player[] players = MatchUtils.getPlayers(match);
        switch (checkType) {
            case DISCONNECTED:
                if (players[0].getName().equals(player.getName()))
                    BattleHandler.handleDisconnect(match, players[0], players[1]);
                BattleHandler.handleDisconnect(match, players[1], players[0]);
                break;
            case CONNECTED:
                BattleHandler.handleConnect(match, players[0], players[1]);
                break;
            case DEATH:
                BattleHandler.handleDeath(match, players[0], players[1], player);
                break;
            default:
                throw new MalformedParametersException();
        }
    }
}
