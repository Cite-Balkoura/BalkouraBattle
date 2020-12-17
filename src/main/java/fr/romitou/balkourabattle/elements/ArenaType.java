package fr.romitou.balkourabattle.elements;

public enum ArenaType {

    /*
     * The arena is classic, who can host all matches.
     */
    CLASSIC("§3Classique"),
    /*
     * The arena is reserved for specific matches, specially the final matches.
     */
    FINAL("§9Finale");

    private final String message;

    ArenaType(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
