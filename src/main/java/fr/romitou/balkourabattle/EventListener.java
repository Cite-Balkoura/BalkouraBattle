package fr.romitou.balkourabattle;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;

public class EventListener implements Listener {

    private static final ViaAPI<?> VIA_API = Via.getAPI();

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (BattleManager.freeze.contains(event.getPlayer()) && from != to)
            event.getPlayer().teleport(from);
    }

    @EventHandler
    public void entityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (!(e.getFinalDamage() >= player.getHealth())) return;
        player.setHealth(20);
        e.setCancelled(true);
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
        int protocol = VIA_API.getPlayerVersion(event.getPlayer().getUniqueId());
        if (protocol >= 48)
            ChatManager.sendMessage(event.getPlayer(), "§cAttention, le PvP de cet événement est en 1.8. N'hésitez pas à vous connecter avec le client Minecraft officiel en 1.8.");
        BattleHandler.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void playerDisconnectEvent(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        BattleHandler.handleDisconnect(event.getPlayer());
    }

}
