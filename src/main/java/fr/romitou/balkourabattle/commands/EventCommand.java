package fr.romitou.balkourabattle.commands;

import com.sun.istack.internal.NotNull;
import fr.romitou.balkourabattle.handlers.BattleHandler;
import fr.romitou.balkourabattle.utils.ChatUils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import java.util.List;

public class EventCommand implements TabExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if (args.length < 1 || args[0].equals("help")) {
            ChatUils.sendMessage(sender, "Commandes disponibles: §e/battle init§f.");
            return false;
        }
        switch (args[0]) {
            case "init":
                if (!sender.isOp()) {
                    ChatUils.sendMessage(sender, "Vous n'avez pas les permissions d'exécuter cette commande.");
                    return false;
                }
                BattleHandler.registerPlayers(sender);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        return null;
    }
}
