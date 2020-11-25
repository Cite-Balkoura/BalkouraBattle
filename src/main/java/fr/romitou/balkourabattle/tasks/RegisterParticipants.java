package fr.romitou.balkourabattle.tasks;

import com.google.api.client.util.ArrayMap;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.utils.ChatUils;
import fr.romitou.balkourabattle.utils.JsonRequest;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class RegisterParticipants extends BukkitRunnable {

    private final CommandSender sender;

    public RegisterParticipants(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            if (BattleHandler.getPlayers().containsValue(player))
                return;
            Map<String, Object> playerData = new HashMap<>();
            playerData.put("name", player.getName());
            JSONObject data = JsonRequest.postJsonRequest("/participants", playerData);
            assert data != null;
            ArrayMap<?, ?> participant = (ArrayMap<?, ?>) data.get("participant");
            BattleHandler.addPlayer(((BigDecimal) participant.get("id")).intValue(), player);
            try {
                Thread.sleep(1000); // We wait one second in order to not surcharge Challonge's API.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ChatUils.sendMessage(sender, "Les participants suivant ont été inscrits auprès de Challonge :");
        ChatUils.sendMessage(sender, StringUtils.join(BattleHandler.getPlayers().values(), ", "));
    }
}
