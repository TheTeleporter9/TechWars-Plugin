package org.solocode.util;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class ChunkUtils {

    /**
     * Returns the center location of the chunk that contains the given location.
     * The center is (chunkX * 16 + 8, original Y, chunkZ * 16 + 8).
     *
     * @param loc Any location in the chunk
     * @return A new Location representing the chunk center
     */
    public static Location getChunkCenter(Location loc) {
        World world = loc.getWorld();
        Chunk chunk = loc.getChunk();

        int centerX = (chunk.getX() << 4) + 8; // chunkX * 16 + 8
        int centerZ = (chunk.getZ() << 4) + 8; // chunkZ * 16 + 8
        double y = loc.getY(); // keep same Y height, or set to fixed height if needed

        return new Location(world, centerX, y, centerZ);
    }
}
