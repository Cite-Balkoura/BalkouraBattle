package fr.romitou.balkourabattle.tasks;

import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Participant;
import fr.romitou.balkourabattle.ChallongeManager;
import fr.romitou.balkourabattle.ChatManager;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticipantInactiveTask extends BukkitRunnable {

    private final Participant participant;

    public ParticipantInactiveTask(Participant participant) {
        this.participant = participant;
    }

    @Override
    public void run() {
        try {
            ChallongeManager.getChallonge().deleteParticipant(participant);
        } catch (DataAccessException e) {
            e.printStackTrace();
            ChatManager.modAlert(
                    "Le participant " + participant.getName() + " n'a pas pu être supprimé.",
                    "Assurez-vous que celui-ci soit bien supprimé auprès de Challonge."
            );
        }
    }
}
