package fr.romitou.balkourabattle.tasks;

import fr.romitou.balkourabattle.utils.JsonRequest;
import fr.romitou.balkourabattle.utils.MatchUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.HashMap;

public class UpdateMatchScore extends BukkitRunnable {

    private final Integer matchId;
    private Integer score1;
    private Integer score2;

    public UpdateMatchScore(Integer matchId, @Nullable Integer score1, @Nullable Integer score2) {
        this.matchId = matchId;
        this.score1 = score1;
        this.score2 = score2;
    }

    @Override
    public void run() {
        if (score1 == null || score2 == null) {
            JSONObject match = JsonRequest.getJsonRequest("/matches/" + matchId);
            Integer[] scores = MatchUtils.getScores((JSONObject) match.get("match"));
            score1 = scores[0];
            score2 = scores[1];
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("scores_csv", score1 + "-" + score2);
        JsonRequest.putJsonRequest("/matches/" + matchId, data);
    }
}
