package fr.romitou.balkourabattle.elements;

import org.bukkit.Location;

import java.util.Arrays;
import java.util.Objects;

public class Arena {

    private int id;
    private Location[] locations;
    private ArenaStatus arenaStatus;
    private ArenaType arenaType;

    public Arena(int id, Location[] locations, ArenaStatus arenaStatus, ArenaType arenaType) {
        this.id = id;
        this.locations = locations;
        this.arenaStatus = arenaStatus;
        this.arenaType = arenaType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location[] getLocations() {
        return locations;
    }

    public void setLocations(Location[] locations) {
        this.locations = locations;
    }

    public ArenaStatus getArenaStatus() {
        return arenaStatus;
    }

    public void setArenaStatus(ArenaStatus arenaStatus) {
        this.arenaStatus = arenaStatus;
    }

    public ArenaType getArenaType() {
        return arenaType;
    }

    public void setArenaType(ArenaType arenaType) {
        this.arenaType = arenaType;
    }

    // Don't take in account the arena status for easily allowing replacing a match who only have a different status.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return id == arena.id && Arrays.equals(locations, arena.locations) && arenaType == arena.arenaType;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, arenaType);
        result = 31 * result + Arrays.hashCode(locations);
        return result;
    }
}
