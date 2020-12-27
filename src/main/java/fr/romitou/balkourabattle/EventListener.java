package fr.romitou.balkourabattle;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (BattleManager.freeze.contains(event.getPlayer()) && from != to)
            event.getPlayer().teleport(from);
    }

    @EventHandler
    public void hungerMeterChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!BattleManager.combat.contains(player)) event.setCancelled(true);
    }

    @EventHandler
    public void entityDamage(EntityDamageEvent event) {
        Player player = (Player) event.getEntity();
        if (!(event.getFinalDamage() >= player.getHealth())) return;
        player.setHealth(20);
        event.setCancelled(true);
        BattleHandler.handleDeath(player);
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setKeepInventory(true);
        event.getEntity().spigot().respawn();
        BattleHandler.handleDeath(event.getEntity());
    }

    @EventHandler
    public void playerConnectEvent(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        BattleHandler.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void playerDisconnectEvent(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        BattleHandler.handleDisconnect(event.getPlayer());
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("modo.event"))
            event.setCancelled(true);
    }

}
