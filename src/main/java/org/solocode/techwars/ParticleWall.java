package org.solocode.techwars;

import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleWall {

    public void createWall (Location centerLocation, Rotations rot) {
        switch (rot) {
            case EASTWEST:
                centerLocation.getWorld().spawnParticle(Particle.DOLPHIN, centerLocation, 700, 16, 200, 1);
                break;
            case NOTRHTSOUTH:
                centerLocation.getWorld().spawnParticle(Particle.DOLPHIN, centerLocation, 700, 1, 200,  16);
                break;
        }
    }

    public enum Rotations {
        NOTRHTSOUTH,
        EASTWEST
    }

}
