package fr.romitou.balkourabattle.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUils {

    private final static String PREFIX = "§6[§eBattle§6] §f";

    public static void sendMessage(CommandSender sender, String... text) {
        sender.sendMessage(PREFIX + StringUtils.join(text, ""));
    }

    public static void sendMessage(Player player, String... text) {
        player.sendMessage(PREFIX + StringUtils.join(text, ""));
    }

}
