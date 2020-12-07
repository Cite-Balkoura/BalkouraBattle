package fr.romitou.balkourabattle.utils;

import java.util.HashMap;
import java.util.Map;

public class JavaUtils {

    public static <K, V> K getKeyByValue(HashMap<K, V> map, V value) {
        return map.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == value)
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
    }

}
