package fr.romitou.balkourabattle.elements;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MatchScore {

    private final ArrayList<Integer[]> scores = new ArrayList<>();

    public MatchScore(String scoreCsv) {
        if (scoreCsv.equals("")) scoreCsv = "0-0";
        String[] splitScore = scoreCsv.split(",");
        String[] firstSet = splitScore[0].split("-");
        String[] secondSet = new String[0];
        String[] thirdSet = new String[0];
        if (splitScore.length >= 2)
            secondSet = splitScore[1].split("-");
        if (splitScore.length >= 3)
            thirdSet = splitScore[2].split("-");
        this.scores.add(0, new Integer[]{
                Integer.parseInt(firstSet[0]),
                Integer.parseInt(firstSet[1])
        });
        if (secondSet.length != 0)
            this.scores.add(1, new Integer[]{
                    Integer.parseInt(secondSet[0]),
                    Integer.parseInt(secondSet[1]),
            });
        if (thirdSet.length != 0)
            this.scores.add(2, new Integer[]{
                    Integer.parseInt(thirdSet[0]),
                    Integer.parseInt(thirdSet[1]),
            });
    }

    public void setWinnerSet(int set, boolean isPlayer1) {
        if (set >= scores.size())
            addRound();
        Integer[] setScores = scores.get(set);
        setScores[isPlayer1 ? 0 : 1] = 1;
        scores.set(set, setScores);
    }

    public MatchSet getSet(int set) {
        return new MatchSet(scores.get(set));
    }

    public int getCurrentRound() {
        return scores.size() - 1;
    }

    public int getNextRound() {
        return scores.size();
    }

    public void addRound() {
        scores.add(new Integer[]{0, 0});
    }

    public long getWinSets(boolean isPlayer1) {
        return scores.stream().filter(scores -> scores[isPlayer1 ? 0 : 1] == 1).count();
    }

    public String getScoreCsv(int maxSet) {
        List<String> list = new LinkedList<>();
        for (int i = 0; i < maxSet; i++) {
            list.add((i < scores.size()) ? (getSet(i).getScore(0) + "-" + getSet(i).getScore(1)) : ("0-0"));
        }
        return StringUtils.join(list, ",");
    }

    public String getScoreCsv() {
        List<String> list = new LinkedList<>();
        scores.forEach(set -> list.add(set[0] + "-" + set[1]));
        return StringUtils.join(list, ",");
    }

}
