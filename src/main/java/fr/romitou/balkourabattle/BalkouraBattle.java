package fr.romitou.balkourabattle;

import fr.romitou.balkourabattle.commands.EventCommand;
import fr.romitou.balkourabattle.tasks.ChallongeSyncTask;
import org.bukkit.command.PluginCommand;
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

        // -- Tasks and events --
        new ChallongeSyncTask().runTaskTimerAsynchronously(this, 0, 10000);
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // -- Commands --
        PluginCommand battleCommand = this.getCommand("battle");
        assert battleCommand != null;
        battleCommand.setExecutor(new EventCommand());
    }

    @Override
    public void onDisable() {
        assert sql != null;
        sql.disconnect();
    }

    public FileConfiguration getConfigFile() {
        return config;
    }

    public MariaController getSql() {
        return sql;
    }
}
