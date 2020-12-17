package fr.romitou.balkourabattle.elements;

import java.lang.reflect.MalformedParametersException;

public class MatchSet {

    private final Integer[] scores;

    public MatchSet(Integer[] scores) {
        this.scores = scores;
    }

    public int getScore(int score) {
        if (score > scores.length)
            throw new MalformedParametersException();
        return scores[score];
    }

}

