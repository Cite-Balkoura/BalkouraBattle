package fr.romitou.balkourabattle;

import fr.romitou.balkourabattle.tasks.CheckParticipantMatch;
import fr.romitou.balkourabattle.utils.ChatUtils;
import fr.romitou.balkourabattle.utils.ParticipantCheckType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    public void playerConnect(PlayerJoinEvent event) {
        ChatUtils.broadcastConnection(event.getPlayer());
        if (BattleHandler.getPlayers().containsValue(event.getPlayer().getName()))
            new CheckParticipantMatch(event.getPlayer(), ParticipantCheckType.CONNECTED);
    }

    public void playerDisconnect(PlayerQuitEvent event) {
        ChatUtils.broadcastDisconnection(event.getPlayer());
        if (BattleHandler.getPlayers().containsValue(event.getPlayer().getName()))
            new CheckParticipantMatch(event.getPlayer(), ParticipantCheckType.DISCONNECTED);
    }

    public void playerDeath(PlayerDeathEvent event) {
        if (BattleHandler.getPlayers().containsValue(event.getEntity().getName()))
            new CheckParticipantMatch(event.getEntity(), ParticipantCheckType.DEATH);
    }

}
