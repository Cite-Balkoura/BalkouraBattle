package fr.romitou.balkourabattle;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    private static final BalkouraBattle INSTANCE = BalkouraBattle.getInstance();

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        if (BattleManager.freeze.contains(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setShouldDropExperience(false);
        event.setKeepInventory(true);
        event.getEntity().spigot().respawn();
        BattleHandler.handleDeath(event.getEntity());
    }

    @EventHandler
    public void playerConnectEvent(PlayerJoinEvent event) {
        BattleHandler.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void playerDisconnectEvent(PlayerQuitEvent event) {
        BattleHandler.handleDisconnect(event.getPlayer());
    }

}
