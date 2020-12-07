package fr.romitou.balkourabattle.utils;

import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleHandler;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ArenaUtils {

    private final static Random RANDOM = new Random();

    public static Integer getArenaIdByMatchId(Long matchId) {
        return JavaUtils.getKeyByValue(BattleHandler.arenas, matchId);
    }

    /**
     * This method is useful to find arenas which are available to host a match.
     *
     * @return A BiMap.
     */
    public static List<Integer> getAvailableArenas() {
        List<Integer> list = new LinkedList<>();
        BattleHandler.arenas.forEach((key, value) -> {
            if (value == null) list.add(key);
        });
        return list;
    }

    /**
     * This method is useful to get a random available arena.
     *
     * @return An arena ID.
     */
    public static Integer getRandomAvailableArena() {
        List<Integer> arenas = getAvailableArenas();

        return arenas.get(RANDOM.nextInt(arenas.size()));
    }

    /**
     * This method will initialize the arenas IDs.
     * This step is very important, otherwise the plugin won't work.
     */
    public static void init() {
        ConfigurationSection config = BalkouraBattle.getConfigFile().getConfigurationSection("arenas");
        assert config != null;
        config.getKeys(false).forEach(key -> {
            System.out.println("config:" + key);
            BattleHandler.arenas.put(Integer.valueOf(key), null);
        });
    }

    /**
     * This method allow to get the locations of an arena.
     *
     * @param arenaId The identifier of the arena.
     * @return A Location array
     */
    public static Location[] getArenaLocations(Integer arenaId) {
        return new Location[]{
                BalkouraBattle
                        .getConfigFile()
                        .getLocation("arenas." + arenaId + ".location.1"),
                BalkouraBattle
                        .getConfigFile()
                        .getLocation("arenas." + arenaId + ".location.2")
        };
    }

    /**
     * This method allow to set a location of an arena.
     *
     * @param arenaId  The identifier of an arena.
     * @param pos      The position of the location (1 or 2).
     * @param location The location of this position.
     */
    public static void setLocation(int arenaId, int pos, Location location) {
        ConfigurationSection arenas = BalkouraBattle
                .getConfigFile()
                .getConfigurationSection("arenas");
        assert arenas != null;
        arenas.set(arenaId + ".location." + pos, location);
        BalkouraBattle.getInstance().saveConfig();
    }

}
