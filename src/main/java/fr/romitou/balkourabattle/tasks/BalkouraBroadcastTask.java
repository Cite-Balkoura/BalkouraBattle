package fr.romitou.balkourabattle.tasks;

import fr.romitou.balkourabattle.ChatManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BalkouraBroadcastTask extends BukkitRunnable {

    private static final TextComponent balkouraLink = new TextComponent("§ehttps://cite-balkoura.fr/1vs1");
    private static final List<TextComponent> textComponents = new LinkedList<>(List.of(
            new TextComponent("Si vous vous déconnectez durant votre combat, vous aurez §e60 secondes§f" +
                    " pour vous reconnecter ou votre adversaire sera gagnant de la manche."),
            new TextComponent("En cas de récidive de déconnexion et afin de limiter les abus, vous serez" +
                    " automatiquement disqualifié au bout de 3 déconnexions."),
            new TextComponent("Si vous n'êtes pas présent au démarrage d'un match, vous aurez §e60 secondes" +
                    " §fpour vous reconnecter et recommencer le match ou votre adversaire sera gagnant de la mache."),
            new TextComponent("Un §cDeath Match§f sera démarré après §e60 secondes§f du démarrage du match."),
            new TextComponent("Les matchs sont organisés en deux manches gagnantes. En cas d'égalité à la" +
                    " seconde manche, une dernière manche sera jouée pour déterminer le gagnant."),
            new TextComponent("Si vous avez des questions supplémentaires, n'hésitez pas à les poser. Les" +
                    " modérateurs vous y répondront.")
    ));
    private static final Random random = new Random();
    private static TextComponent lastMessage = textComponents.get(random.nextInt(textComponents.size()));

    static {
        TextComponent balkouraMessage = new TextComponent("Accédez au classement en ligne sur ");
        balkouraLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://cite-balkoura.fr/1vs1"));
        balkouraMessage.addExtra(balkouraLink);
        balkouraMessage.addExtra(" §f!");
        textComponents.add(balkouraMessage);
    }

    @Override
    public void run() {
        TextComponent announce = new TextComponent();
        if (announce == lastMessage)
            while (announce == lastMessage)
                announce = textComponents.get(random.nextInt(textComponents.size()));
        else
            announce = textComponents.get(random.nextInt(textComponents.size()));
        lastMessage = announce;
        ChatManager.broadcast(announce);
    }
}
