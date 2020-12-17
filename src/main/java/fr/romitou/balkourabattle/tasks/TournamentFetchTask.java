package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.ChallongeManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TournamentFetchTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            ChallongeManager.setTournament(ChallongeManager.getChallonge()
                    .getTournament(BalkouraBattle.getConfigFile().getString("challonge.tournament"))
            );
        } catch (DataAccessException e) {
            Bukkit.getLogger().severe("The challonge tournament wasn't found. Disabling.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(BalkouraBattle.getInstance());
        }
    }
}
