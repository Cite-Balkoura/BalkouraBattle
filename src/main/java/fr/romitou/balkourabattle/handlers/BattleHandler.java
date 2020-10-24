package fr.romitou.balkourabattle.handlers;

import fr.romitou.balkourabattle.utils.ChatUils;
import fr.romitou.balkourabattle.utils.JsonRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class BattleHandler {

    public static void registerPlayers(CommandSender sender) {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            Map<String, Object> playerData = new HashMap<>();
            playerData.put("name", player.getName());
            JsonRequest.postJsonRequest("tournaments/balkoura_1vs1_test/participants", playerData);
        });
        ChatUils.sendMessage(sender, "L'enregistrement des joueurs est terminé.");
    }
}