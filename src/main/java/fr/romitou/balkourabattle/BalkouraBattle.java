package fr.romitou.balkourabattle;

import fr.romitou.balkourabattle.commands.EventCommand;
import fr.romitou.balkourabattle.tasks.MatchesRequestTask;
import fr.romitou.balkourabattle.tasks.MatchesSyncTask;
import fr.romitou.balkourabattle.tasks.MatchesUpdateTask;
import fr.romitou.balkourabattle.tasks.TournamentFetchTask;
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
        new MatchesSyncTask().runTaskTimerAsynchronously(this, 300, 300);
        new MatchesUpdateTask().runTaskTimerAsynchronously(this, 400, 300);
        new MatchesRequestTask().runTaskTimerAsynchronously(this, 500, 300);
        new TournamentFetchTask().runTaskAsynchronously(this);
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // -- Commands --
        PluginCommand battleCommand = this.getCommand("battle");
        assert battleCommand != null;
        battleCommand.setExecutor(new EventCommand());

        // -- Initialize arenas --
        BattleManager.registerArenasFromConfig();

    }
}
