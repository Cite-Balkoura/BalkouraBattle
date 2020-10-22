package fr.romitou.balkourabattle;

import fr.romitou.balkourabattle.tasks.ChallongeSyncTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BalkouraBattle extends JavaPlugin {

    private static BalkouraBattle instance;
    private FileConfiguration config;
    private MariaController sql;

    public static BalkouraBattle getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // --- Configuration ---
        this.saveDefaultConfig();
        config = this.getConfig();

        // --- SQL ---
        sql = new MariaController("jdbc:mysql://",
                config.getString("sql.hostname"),
                config.getString("sql.database"),
                config.getString("sql.user"),
                config.getString("sql.password"));
        sql.connect();

        // -- Tasks --
        new ChallongeSyncTask().runTaskTimerAsynchronously(this, 0, 2000);
    }

    @Override
    public void onDisable() {
        sql.disconnect();
    }

    public FileConfiguration getConfigFile() {
        return config;
    }

    public MariaController getSql() {
        return sql;
    }
}
