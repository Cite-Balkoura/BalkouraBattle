package fr.romitou.balkourabattle.utils;

/**
 * This enum is used to make difference between the multiple checks of an participant.
 *
 * @see fr.romitou.balkourabattle.tasks.CheckParticipantMatch
 */
public enum ParticipantCheckType {
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
