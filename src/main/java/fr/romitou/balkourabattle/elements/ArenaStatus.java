package fr.romitou.balkourabattle.elements;

public enum ArenaStatus {

    /*
     * The arena is free to host a match.
     */
    FREE("§aLibre"),
    /*
     * The arena is waiting for approval to host a match.
     */
    VALIDATING("§6En attente de validation"),
    /*
     * The arena is already used by another match.
     */
    BUSY("§cOccupé");

    private final String message;

    ArenaStatus(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
