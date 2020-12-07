package fr.romitou.balkourabattle.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtils {

    private final static String PREFIX = "§6[§eBattle§6] §f";

    public static String getFormattedMessage(String... text) {
        return PREFIX + StringUtils.join(text, "");
    }

    /**
     * This method is useful to send a pre-formatted message to a sender.
     *
     * @param sender  The sender.
     * @param strings The message.
     */
    public static void sendMessage(CommandSender sender, String... strings) {
        sender.sendMessage(getFormattedMessage(strings));
    }

    /**
     * This method is useful to send a pre-formatted message to a player.
     *
     * @param player  The player.
     * @param strings The message.
     */
    public static void sendMessage(Player player, String... strings) {
        player.sendMessage(getFormattedMessage(strings));
    }

    public static void broadcast(String... strings) {
        // Due to permissions issues, we are forced to do it this way.
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(getFormattedMessage(strings)));
    }

    public static void broadcastConnection(Player player) {
        broadcast("§a[+]§f Bienvenue, §e" + player.getName() + "§f.");
    }

    public static void broadcastDisconnection(Player player) {
        broadcast("§c[-]§f Au revoir, §e" + player.getName() + "§f.");
    }

}
