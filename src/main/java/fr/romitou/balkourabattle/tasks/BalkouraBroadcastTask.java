package fr.romitou.balkourabattle.tasks;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BalkouraBroadcastTask extends BukkitRunnable {

    private static final TextComponent balkouraLink = new TextComponent("§ehttps://cite-balkoura.fr/1vs1");
    private static final List<TextComponent> textComponents = new LinkedList<>(List.of(
            new TextComponent("§7Si vous vous déconnectez durant votre combat, pas de problème ! Vous aurez" +
                    " §e60 secondes§7 pour vous reconnecter afin de redémarrer la manche."),
            new TextComponent("§7En cas de récidive de déconnexion et afin de limiter les abus, vous serez" +
                    " automatiquement disqualifié au bout de 3 déconnexions."),
            new TextComponent("§7Si vous n'êtes pas présent lors du démarrage d'un match, vous aurez §e60 secondes" +
                    " §7pour vous reconnecter. Sinon, votre adversaire gagnera la manche !"),
            new TextComponent("§7Un §cDeath Match§7 sera démarré après §e60 secondes§7 de combat si aucun joueur" +
                    " ne parvient à gagner la manche."),
            new TextComponent("§7Les matchs sont organisés en deux manches gagnantes. En cas d'égalité à la" +
                    " seconde manche, une dernière manche sera jouée pour déterminer le gagnant."),
            new TextComponent("§7Il est possible que vous ayez à patienter que votre match démarre. Pas d'inquiétude," +
                    " celui-ci démarrera lorsque le match précédent sera terminé et que son vainqueur sera désigné."),
            new TextComponent("§7Pour consulter vos précédents ou prochains matchs, rendez-vous sur le site de" +
                    " la Cité de Balkoura ou exécutez §e/battle info§7."),
            new TextComponent("§7Si vous avez des questions supplémentaires, n'hésitez pas à les poser : les" +
                    " modérateurs vous y répondront.")
    ));
    private static final Random random = new Random();
    private static TextComponent lastMessage = textComponents.get(random.nextInt(textComponents.size()));

    static {
        TextComponent balkouraMessage = new TextComponent("§7Accédez au classement en ligne sur ");
        balkouraLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://cite-balkoura.fr/1vs1"));
        balkouraMessage.addExtra(balkouraLink);
        balkouraMessage.addExtra(" §7!");
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
        TextComponent prefix = new TextComponent("§6§lTIP! ");
        announce.setColor(ChatColor.GRAY);
        prefix.addExtra(announce);
        // Due to permissions issues, we are forced to do it this way.
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(prefix));
    }
}
