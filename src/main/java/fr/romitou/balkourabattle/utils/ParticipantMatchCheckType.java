package fr.romitou.balkourabattle.utils;

import fr.romitou.balkourabattle.tasks.ParticipantMatchCheckTask;

/**
 * This enum is used to make difference between the multiple checks of an participant.
 *
 * @see ParticipantMatchCheckTask
 */
public enum ParticipantMatchCheckType {
    /**
     * Represents a participant disconnection when fighting.
     */
    DISCONNECTED,
    /**
     * Represents a participant connection.
     */
    CONNECTED,
    /**
     * Represents a participant death.
     */
    DEATH
}
