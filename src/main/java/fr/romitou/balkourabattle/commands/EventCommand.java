package fr.romitou.balkourabattle.commands;

import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.handlers.BattleHandler;
import fr.romitou.balkourabattle.tasks.FinalizeTournament;
import fr.romitou.balkourabattle.tasks.ResetTournament;
import fr.romitou.balkourabattle.tasks.StartTournament;
import fr.romitou.balkourabattle.utils.ChatUils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import java.util.List;

public class EventCommand implements TabExecutor {

    private final static BalkouraBattle INSTANCE = BalkouraBattle.getInstance();

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if (args.length < 1 || args[0].equals("help")) {
            ChatUils.sendMessage(sender, "Commandes disponibles: §e/battle init§f.");
            return false;
        }
        switch (args[0]) {
            case "init":
                BattleHandler.registerPlayers(sender);
                ChatUils.sendMessage(sender, "Début de l'enregistrement des joueurs. Cela peut prendre un moment ...");
                break;
            case "start":
                new StartTournament().runTaskAsynchronously(INSTANCE);
                ChatUils.sendMessage(sender, "Le tournois a été ouvert et ne peut plus être modifié.");
                break;
            case "matchmaking":
                BattleHandler.startMatchMaking(sender);
                ChatUils.sendMessage(sender, "Récupération des équipes depuis Challonge ...");
                break;
            case "reset":
                new ResetTournament().runTaskAsynchronously(INSTANCE);
                ChatUils.sendMessage(sender, "Le tournois a été réinitialisé.");
                break;
            case "finalize":
                new FinalizeTournament().runTaskAsynchronously(INSTANCE);
                ChatUils.sendMessage(sender, "Le tournois a été marqué comme terminé.");
                break;
            default:
                ChatUils.sendMessage(sender, "Commande inconnue.");
                return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        return null;
    }
}
