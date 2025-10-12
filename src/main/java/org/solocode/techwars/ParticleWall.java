package org.solocode.techwars;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ParticleWall {

    public ArrayList<Location> wallCenterLocations = new ArrayList<>();
    public ArrayList<Location> wallEdgeLocations = new ArrayList<>();

    Random rand = new Random();

    public void createWall(List<Location> wallCenterLocations) {
        for (Location centerLocation : wallCenterLocations) {

            World world = centerLocation.getWorld();
            if (world == null) continue;
            //Per wall
            for (int i = 0; i <= 4; i++){
                switch (i) {
                    case 0:
                        summonParticles(8, 100, centerLocation.clone().add(0, 0, 7), world, Rotations.EASTWEST);
                    case 1:
                        summonParticles(8, 100, centerLocation.clone().add(-8, 0, 0), world, Rotations.NOTRHTSOUTH);
                    case 2:
                        summonParticles(8, 100, centerLocation.clone().add(0, 0, -8), world, Rotations.EASTWEST);
                    case 3:
                        summonParticles(8, 100, centerLocation.clone().add(7, 0, 0), world, Rotations.NOTRHTSOUTH);
                    default:
                        break;
                }
            }
        }
    }


    public void summonParticles(int width, int height, Location centerLocation, World world, Rotations rot) {
        double spacing = 1.0; // particle spacing

        for (int y = 0; y < height; y++) {
            for (int x = -width; x <= width; x++) {


                Location particleLoc = centerLocation.clone();

                if (rot == Rotations.NOTRHTSOUTH) {
                    particleLoc.add(0, y * spacing , x * spacing);
                } else { //East west
                    particleLoc.add(x * spacing , y * spacing, 0);
                }

                Particle.DustOptions blue = new Particle.DustOptions(Color.AQUA, 1.0f);


                world.spawnParticle(
                        Particle.REDSTONE,
                        particleLoc,
                        1,
                        blue
                );
            }
        }
    }

    public enum Rotations {
        NOTRHTSOUTH,
        EASTWEST
    }

}
