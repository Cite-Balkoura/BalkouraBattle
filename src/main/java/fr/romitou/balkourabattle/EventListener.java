package fr.romitou.balkourabattle;

import fr.romitou.balkourabattle.tasks.ParticipantMatchCheckTask;
import fr.romitou.balkourabattle.utils.ChatUtils;
import fr.romitou.balkourabattle.utils.ParticipantMatchCheckType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    public void playerConnect(PlayerJoinEvent event) {
        ChatUtils.broadcastConnection(event.getPlayer());
        if (BattleHandler.players.containsValue(event.getPlayer().getName()))
            new ParticipantMatchCheckTask(event.getPlayer(), ParticipantMatchCheckType.CONNECTED);
    }

    public void playerDisconnect(PlayerQuitEvent event) {
        ChatUtils.broadcastDisconnection(event.getPlayer());
        if (BattleHandler.players.containsValue(event.getPlayer().getName()))
            new ParticipantMatchCheckTask(event.getPlayer(), ParticipantMatchCheckType.DISCONNECTED);
    }

    public void playerDeath(PlayerDeathEvent event) {
        if (BattleHandler.players.containsValue(event.getEntity().getName()))
            new ParticipantMatchCheckTask(event.getEntity(), ParticipantMatchCheckType.DEATH);
    }

}
