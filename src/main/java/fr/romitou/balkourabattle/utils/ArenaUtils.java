package fr.romitou.balkourabattle.utils;

import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleHandler;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Random;

public class ArenaUtils {

    private final static Random RANDOM = new Random();

    @SuppressWarnings("unchecked")
    public static HashMap<Integer, Integer> getAvailableArenas() {
        return (HashMap<Integer, Integer>) BattleHandler.getArenas()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() == null);
    }

    public static Integer getRandomAvailableArena() {
        Object[] arenas = getAvailableArenas().values().toArray();
        return (Integer) arenas[RANDOM.nextInt(arenas.length)];
    }

    public static Location[] getArenaLocations(Integer id) {
        return new Location[] {
                BalkouraBattle.getInstance().getConfigFile().getLocation("arenas." + id + ".location.1"),
                BalkouraBattle.getInstance().getConfigFile().getLocation("arenas." + id + ".location.2")
        };
    }

}
