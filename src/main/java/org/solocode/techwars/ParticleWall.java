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
        // Remove overlapping walls
        Location newCenterLocation = null;
        if(!wallCenterLocations.isEmpty()) {
            newCenterLocation = wallCenterLocations.get(wallCenterLocations.size() -1);
        }

        updateBorders(newCenterLocation);

        // Add new center after cleanup
        this.wallCenterLocations.add(newCenterLocation);

        World world = newCenterLocation.getWorld();
        if (world == null) return;

        // Per wall
        for (int i = 0; i <= 4; i++) {
            switch (i) {
                case 0:
                    summonParticles(8, 100, newCenterLocation.clone().add(0, 0, 7), world, Rotations.EASTWEST);
                case 1:
                    summonParticles(8, 100, newCenterLocation.clone().add(-8, 0, 0), world, Rotations.NOTRHTSOUTH);
                case 2:
                    summonParticles(8, 100, newCenterLocation.clone().add(0, 0, -8), world, Rotations.EASTWEST);
                case 3:
                    summonParticles(8, 100, newCenterLocation.clone().add(7, 0, 0), world, Rotations.NOTRHTSOUTH);
            }
        }
    }



    private void updateBorders(Location newCenter) {

        wallCenterLocations.removeIf(existing -> existing.getWorld().equals(newCenter.getWorld()) &&
                existing.distanceSquared(newCenter) <= 4); // 2 blocks distance squared = 4


        wallEdgeLocations.removeIf(edge -> edge.getWorld().equals(newCenter.getWorld()) &&
                edge.distanceSquared(newCenter) <= 4);
    }


    public void summonParticles(int width, int height, Location centerLocation, World world, Rotations rot) {
        double spacing = 1.0;

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
