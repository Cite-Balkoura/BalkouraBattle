package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Participant;
import at.stefangeyer.challonge.model.query.ParticipantQuery;
import fr.romitou.balkourabattle.BattleHandler;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.utils.ChatUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ParticipantsRegistrationTask extends BukkitRunnable {

    @Override
    public void run() {
        HashMap<String, Long> registeredParticipants = new HashMap<>();
        try {
            ChallongeManager.getChallonge().getParticipants(ChallongeManager.getTournament()).forEach(participant -> registeredParticipants.put(participant.getName(), participant.getId()));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            if (registeredParticipants.containsKey(player.getName())) {
                BattleHandler.players.put(registeredParticipants.get(player.getName()), player.getName());
                ChatUtils.sendMessage(player, "Vous êtes inscrit pour ce tournois. Préparez-vous !");
            }
            if (BattleHandler.players.containsValue(player.getName()))
                return;
            ParticipantQuery participantQuery = ParticipantQuery.builder()
                    .name(player.getName())
                    .build();
            try {
                Participant participant = ChallongeManager.getChallonge().addParticipant(
                        ChallongeManager.getTournament(),
                        participantQuery
                );
                BattleHandler.players.put(participant.getId(), player.getName());
                ChatUtils.sendMessage(player, "Vous êtes inscrit pour ce tournois. Préparez-vous !");
                Thread.sleep(1000); // We wait one second in order to not surcharge Challonge's API.
            } catch (InterruptedException | DataAccessException e) {
                e.printStackTrace();
            }
        });
        ChatUtils.broadcast("Les participants suivant sont inscrits pour ce tournois :");
        ChatUtils.broadcast(StringUtils.join(BattleHandler.players.values(), ", "));
        ChatUtils.broadcast("Que le meilleur gagne !");
    }
}
