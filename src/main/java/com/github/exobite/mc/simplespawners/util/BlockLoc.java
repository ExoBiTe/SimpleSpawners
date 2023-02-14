package com.github.exobite.mc.simplespawners.util;

import org.bukkit.Location;
import org.bukkit.World;

public record BlockLoc(int x, int y, int z) {

    public Location toLocation(World w) {
        return new Location(w, x, y, z);
    }

}
