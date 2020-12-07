package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchesAnnouncementTask extends BukkitRunnable {
    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().getMatches(ChallongeManager.getTournament()).forEach(match -> {
                Player player1 = BattleHandler.getPlayer(match.getPlayer1Id());
                Player player2 = BattleHandler.getPlayer(match.getPlayer2Id());
                System.out.println(match.getPlayer1Id() + " - " + match.getPlayer2Id());
                System.out.println(BattleHandler.players.toString());
                assert player1 != null && player2 != null;
                ChatUtils.sendMessage(player1, "Votre prochain match sera contre §e" + player2.getDisplayName() + "§f, préparez-vous !");
                ChatUtils.sendMessage(player2, "Votre prochain match sera contre §e" + player1.getDisplayName() + "§f, préparez-vous !");
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
