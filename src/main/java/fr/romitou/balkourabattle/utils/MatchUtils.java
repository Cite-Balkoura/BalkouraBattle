package fr.romitou.balkourabattle.utils;

import com.google.api.client.util.ArrayMap;
import fr.romitou.balkourabattle.BattleHandler;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.lang.reflect.MalformedParametersException;
import java.math.BigDecimal;

public class MatchUtils {

    /**
     * This method is useful to get players of a match, only with an JSONObject of it.
     *
     * @param match The JSONObject of the match.
     * @return An Player array.
     */
    public static Player[] getPlayers(JSONObject match) {
        Integer firstId = ((BigDecimal) match.get("player1_id")).intValueExact();
        Integer secondId = ((BigDecimal) match.get("player2_id")).intValueExact();
        return new Player[]{
                BattleHandler.getPlayer(firstId),
                BattleHandler.getPlayer(secondId)
        };
    }

    /**
     * This method is useful to get players of a match, only with an ArrayMap of it.
     *
     * @param match The ArrayMap of the match.
     * @return An Player array.
     */
    public static Player[] getPlayers(ArrayMap<?, ?> match) {
        Integer firstId = ((BigDecimal) match.get("player1_id")).intValueExact();
        Integer secondId = ((BigDecimal) match.get("player2_id")).intValueExact();
        return new Player[]{
                BattleHandler.getPlayer(firstId),
                BattleHandler.getPlayer(secondId)
        };
    }

    /**
     * This method is useful to retrieve the ID of a match.
     *
     * @param match The JSONObject of the match.
     * @return The identifier integer.
     */
    public static int getId(JSONObject match) {
        if (match.get("id") == null)
            throw new MalformedParametersException();
        return ((BigDecimal) match.get("id")).intValue();
    }

    /**
     * This method is useful to retrieve scores of a match.
     *
     * @param match The JSONObject of the match.
     * @return An Integer array.
     */
    public static Integer[] getScores(JSONObject match) {
        String csvScores = (String) match.get("scores_csv");
        if (csvScores.equals("")) csvScores = "0-0";
        String[] scores = csvScores.split("-");
        return new Integer[]{
                Integer.parseInt(scores[0]),
                Integer.parseInt(scores[1])
        };
    }


}
