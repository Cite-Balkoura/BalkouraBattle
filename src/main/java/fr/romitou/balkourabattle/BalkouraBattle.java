package fr.romitou.balkourabattle;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.commands.EventCommand;
import fr.romitou.balkourabattle.tasks.ChallongeSyncTask;
import fr.romitou.balkourabattle.utils.ArenaUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BalkouraBattle extends JavaPlugin {

    private static BalkouraBattle instance;
    private static FileConfiguration config;

    public static BalkouraBattle getInstance() {
        return instance;
    }

    public static FileConfiguration getConfigFile() {
        return config;
    }

    @Override
    public void onEnable() {
        instance = this;

        // --- Configuration ---
        this.saveDefaultConfig();
        config = this.getConfig();

        // -- Tasks and events --
        new ChallongeSyncTask().runTaskTimerAsynchronously(this, 0, 200);
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // -- Commands --
        PluginCommand battleCommand = this.getCommand("battle");
        assert battleCommand != null;
        battleCommand.setExecutor(new EventCommand());

        // -- Initialize BiMaps --
        ArenaUtils.init();

        try {
            ChallongeManager.setTournament(ChallongeManager
                    .getChallonge()
                    .getTournament(config.getString("challonge.tournament"))
            );
        } catch (DataAccessException e) {
            getLogger().severe("The challonge tournament wasn't found. Disabling.");
            e.printStackTrace();
            getPluginLoader().disablePlugin(this);
        }
    }
}
