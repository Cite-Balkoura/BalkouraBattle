package fr.romitou.balkourabattle.commands;

import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.tasks.MatchRequestCallbackTask;
import fr.romitou.balkourabattle.tasks.ParticipantsRegistrationTask;
import fr.romitou.balkourabattle.tasks.TournamentResettingTask;
import fr.romitou.balkourabattle.tasks.TournamentStartingTask;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class EventCommand implements TabExecutor {

    private final static BalkouraBattle INSTANCE = BalkouraBattle.getInstance();

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if (args.length < 1) {
            ChatManager.sendMessage(sender, "Commande invalide.");
            return false;
        }
        switch (args[0]) {
            case "register":
            case "init":
                ChatManager.sendMessage(sender, "Début de l'enregistrement des joueurs. Cela peut prendre un moment ...");
                new ParticipantsRegistrationTask((Player) sender).runTaskAsynchronously(INSTANCE);
                break;
            case "start":
                new TournamentStartingTask().runTaskAsynchronously(INSTANCE);
                ChatManager.sendMessage(sender, "Le tournois a été ouvert et ne peut plus être modifié.");
                break;
            case "reset":
                new TournamentResettingTask().runTaskAsynchronously(INSTANCE);
                ChatManager.sendMessage(sender, "Le tournois a été réinitialisé.");
                break;
            case "info":
            case "status":
                if (args.length == 1) {
                    BattleManager.sendParticipantMatchesInfo((Player) sender);
                    break;
                }
                int matchId = Integer.parseInt(args[1]);
                BattleManager.sendMatchInfo((OfflinePlayer) sender, matchId);
                break;
            case "arenas":
                BattleManager.sendArenaInfos((Player) sender);
                break;
            case "debug":
                sender.sendMessage(BattleManager.arenas.toString());
                sender.sendMessage(BattleManager.waitingMatches.toString());
                sender.sendMessage(BattleManager.registeredParticipants.toString());
                break;
            case "players":
                BattleManager.sendParticipantInfos((Player) sender);
                break;
            case "accept":
                if (args.length >= 2 && args[1] == null)
                    break;
                long id = Integer.parseInt(args[1]);
                new MatchRequestCallbackTask((Player) sender, id).runTaskAsynchronously(INSTANCE);
                break;
            case "test":
                break;
            default:
                ChatManager.sendMessage(sender, "Commande inconnue.");
                return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        return List.of(
                "register",
                "start",
                "announce",
                "finalize"
        );
    }
}
