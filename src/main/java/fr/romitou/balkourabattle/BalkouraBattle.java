package fr.romitou.balkourabattle;

import fr.romitou.balkourabattle.commands.EventCommand;
import fr.romitou.balkourabattle.tasks.ChallongeSyncTask;
import fr.romitou.balkourabattle.utils.ArenaUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BalkouraBattle extends JavaPlugin {

    private static BalkouraBattle instance;
    private FileConfiguration config;

    public static BalkouraBattle getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // --- Configuration ---
        this.saveDefaultConfig();
        config = this.getConfig();

        // -- Tasks and events --
        new ChallongeSyncTask().runTaskTimerAsynchronously(this, 0, 10000);
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // -- Commands --
        PluginCommand battleCommand = this.getCommand("battle");
        assert battleCommand != null;
        battleCommand.setExecutor(new EventCommand());

        // -- Initialize BiMaps --
        ArenaUtils.init();

    }

    public FileConfiguration getConfigFile() {
        return config;
    }
}
