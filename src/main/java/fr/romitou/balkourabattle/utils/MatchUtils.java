package fr.romitou.balkourabattle.utils;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BattleHandler;
import org.bukkit.entity.Player;

public class MatchUtils {

    /**
     * This method is useful to get players of a match, only with an JSONObject of it.
     *
     * @param match The JSONObject of the match.
     * @return An Player array.
     */
    public static Player[] getPlayers(Match match) {
        long firstId = match.getPlayer1Id();
        long secondId = match.getPlayer2Id();
        return new Player[]{
                BattleHandler.getPlayer(firstId),
                BattleHandler.getPlayer(secondId)
        };
    }

    /**
     * This method is useful to retrieve scores of a match.
     *
     * @param match The JSONObject of the match.
     * @return An Integer array.
     */
    public static Integer[] getScores(Match match) {
        String csvScores = match.getScoresCsv();
        if (csvScores.equals("")) csvScores = "0-0";
        String[] scores = csvScores.split("-");
        return new Integer[]{
                Integer.parseInt(scores[0]),
                Integer.parseInt(scores[1])
        };
    }


}
