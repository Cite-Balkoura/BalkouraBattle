package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.model.Match;
import fr.romitou.balkourabattle.BattleManager;
import fr.romitou.balkourabattle.utils.ChatUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.List;

public class MatchesRequestTask extends BukkitRunnable {

    @Override
    public void run() {
        List<Match> matches = BattleManager.getApprovalWaitingMatchs();
        if (matches.size() == 0) return;
        List<TextComponent> textComponents = new LinkedList<>();
        textComponents.add(new TextComponent("   §f§l» §eMatchs en attente de validation :"));
        textComponents.add(new TextComponent(""));
        matches.forEach(match -> {
            TextComponent base = new TextComponent("   §e● §fMatch " + match.getIdentifier() + " §7| ");
            TextComponent info = new TextComponent("§6[+ d'infos] ");
            info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/battle info " + match.getId()));
            base.addExtra(info);
            TextComponent accept = new TextComponent("§a[Accepter]");
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/battle accept " + match.getId()));
            base.addExtra(accept);
            textComponents.add(base);
        });
        BattleManager.getOnlineModerators().forEach(player -> ChatUtils.sendBeautifulMessage(player, textComponents));
    }

}
