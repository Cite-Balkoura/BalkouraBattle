import fr.romitou.balkourabattle.elements.MatchScore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MatchScoresTests {

    @Test
    @DisplayName("Match Scores")
    void matchScore() {
        MatchScore matchScore = new MatchScore("0-0,0-0");
        Assertions.assertEquals(matchScore.getCurrentRound(), 1);
        matchScore.setWinnerSet(0, true);
        Assertions.assertEquals(matchScore.getScoreCsv(1), "1-0");
        matchScore.setWinnerSet(1, false);
        matchScore.setWinnerSet(2, false);
        Assertions.assertEquals(matchScore.getSet(0).getScore(0), 1);
        Assertions.assertEquals(matchScore.getScoreCsv(2), "1-0,0-1");
        Assertions.assertEquals(matchScore.getCurrentRound(), 2);
        matchScore = new MatchScore(matchScore.getScoreCsv());
        Assertions.assertEquals(matchScore.getScoreCsv(3), "1-0,0-1,0-1");
        Assertions.assertEquals(matchScore.getWinSets(true), 1);
        Assertions.assertEquals(matchScore.getWinSets(false), 2);
        matchScore.addRound();
        Assertions.assertEquals(matchScore.getScoreCsv(matchScore.getNextRound()), "1-0,0-1,0-1,0-0");
        matchScore.setWinnerSet(4, true);
        Assertions.assertEquals(matchScore.getScoreCsv(matchScore.getNextRound()), "1-0,0-1,0-1,0-0,1-0");
    }

}
