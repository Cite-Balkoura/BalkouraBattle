package fr.romitou.balkourabattle.commands;

import fr.romitou.balkourabattle.BalkouraBattle;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.ChatManager;
import fr.romitou.balkourabattle.tasks.MatchRequestCallbackTask;
import fr.romitou.balkourabattle.tasks.ParticipantsRegistrationTask;
import fr.romitou.balkourabattle.tasks.TournamentResettingTask;
import fr.romitou.balkourabattle.tasks.TournamentStartingTask;
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
            ChatManager.sendMessage(sender, "Commande invalide, essayez plutôt §e/battle info§f.");
            return false;
        }
        switch (args[0]) {
            case "speed":
                if (!BattleManager.hasPermission(sender, "modo.event")) break;
                if (!(sender instanceof Player)) break;
                if (args.length == 1) {
                    ChatManager.sendMessage(sender, "Précisez un nombre valdie");
                    break;
                }
                float speed;
                try {
                    speed = Float.parseFloat(args[1]);
                } catch (NumberFormatException ex) {
                    ChatManager.sendMessage(sender, "Nombre invalide");
                    break;
                }
                Player player = (Player) sender;
                if (player.isFlying())
                    player.setFlySpeed(speed);
                else
                    player.setWalkSpeed(speed);
                ChatManager.sendMessage(sender, "c'est bon");
                break;
            case "register":
            case "init":
                if (!BattleManager.hasPermission(sender, "modo.event")) break;
                ChatManager.sendMessage(sender, "Début de l'enregistrement des joueurs. Cela peut prendre un moment ...");
                new ParticipantsRegistrationTask((Player) sender).runTaskAsynchronously(INSTANCE);
                break;
            case "start":
                if (!BattleManager.hasPermission(sender, "modo.event")) break;
                new TournamentStartingTask().runTaskAsynchronously(INSTANCE);
                ChatManager.sendMessage(sender, "Le tournois a été ouvert et ne peut plus être modifié.");
                break;
            case "reset":
                if (!BattleManager.hasPermission(sender, "modo.event")) break;
                new TournamentResettingTask().runTaskAsynchronously(INSTANCE);
                ChatManager.sendMessage(sender, "Le tournois a été réinitialisé.");
                break;
            case "info":
            case "status":
                if (args.length == 1) {
                    BattleManager.sendParticipantMatchesInfo((Player) sender);
                    break;
                }
                int matchId;
                try {
                    matchId = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    ChatManager.sendMessage(sender, "Merci de préciser un identifiant de match valide.");
                    break;
                }
                BattleManager.sendMatchInfo((OfflinePlayer) sender, matchId);
                break;
            case "arenas":
                if (!BattleManager.hasPermission(sender, "modo.event")) break;
                BattleManager.sendArenaInfos((Player) sender);
                break;
            case "debug":
                if (!BattleManager.hasPermission(sender, "modo.event")) break;
                sender.sendMessage(BattleManager.arenas.toString());
                sender.sendMessage(BattleManager.waitingMatches.toString());
                sender.sendMessage(BattleManager.registeredParticipants.toString());
                break;
            case "players":
                if (!BattleManager.hasPermission(sender, "modo.event")) break;
                BattleManager.sendParticipantInfos((Player) sender);
                break;
            case "accept":
                if (!BattleManager.hasPermission(sender, "modo.event")) break;
                if (args.length >= 2 && args[1] == null)
                    break;
                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    ChatManager.sendMessage(sender, "Merci de préciser un identifiant de match valide.");
                    break;
                }
                new MatchRequestCallbackTask((Player) sender, id).runTaskAsynchronously(INSTANCE);
                break;
            case "setup":
                if (!BattleManager.hasPermission(sender, "modo.event")) break;
                BalkouraBattle.getConfigFile().set("arenas." + args[1] + "." + (Integer.parseInt(args[2]) == 1 ? "firstLocation" : "secondLocation") + ".x", ((Player) sender).getLocation().getX());
                BalkouraBattle.getConfigFile().set("arenas." + args[1] + "." + (Integer.parseInt(args[2]) == 1 ? "firstLocation" : "secondLocation") + ".y", ((Player) sender).getLocation().getY());
                BalkouraBattle.getConfigFile().set("arenas." + args[1] + "." + (Integer.parseInt(args[2]) == 1 ? "firstLocation" : "secondLocation") + ".z", ((Player) sender).getLocation().getZ());
                BalkouraBattle.getInstance().saveConfig();
                ChatManager.sendMessage(sender, "Position définie.");
                break;
            default:
                ChatManager.sendMessage(sender, "Commande inconnue, essayez plutôt §e/battle info§f.");
                return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (sender.hasPermission("modo.event"))
            return List.of(
                    "register",
                    "start",
                    "info",
                    "arenas",
                    "debug",
                    "players",
                    "accept",
                    "setup"
            );
        return List.of(
                "info"
        );
    }
}
