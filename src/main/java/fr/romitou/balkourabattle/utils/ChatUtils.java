package fr.romitou.balkourabattle.utils;

import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatUtils {

    private final static String PREFIX = "§6[§eBattle§6] §f";
    private final static String ERROR_PREFIX = "§cErreur: ";
    private final static String DELIMITER = "§7§m                                                                     ";
    private final static String[] SPACED_DELIMITER = new String[]{"", DELIMITER, ""};

    public static String getFormattedMessage(String... text) {
        return PREFIX + StringUtils.join(text, "");
    }

    public static void sendBeautifulMessage(Player player, List<TextComponent> textComponents) {
        player.sendMessage(SPACED_DELIMITER);
        textComponents.forEach(player::sendMessage);
        player.sendMessage(SPACED_DELIMITER);
    }

    public static void sendBeautifulMessage(Player player, String... strings) {
        player.sendMessage(SPACED_DELIMITER);
        player.sendMessage(strings);
        player.sendMessage(SPACED_DELIMITER);
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

    public static void modAlert(String... strings) {
        broadcast(ERROR_PREFIX + StringUtils.join(strings));
    }

}
