package com.minecarts.auramanager;

import java.util.Collection;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class BoundingBox {
    public final Coordinate min;
    public final Coordinate max;
    
    public BoundingBox(Block first, Block second) {
        this(Coordinate.fromBlock(first), Coordinate.fromBlock(second));
    }
    public BoundingBox(Coordinate a, Coordinate b) {
        this.min = Coordinate.min(a, b);
        this.max = Coordinate.max(a, b);
    }
    
    public boolean contains(Coordinate coord) {
        return contains(coord.x, coord.y, coord.z);
    }
    public boolean contains(int x, int y, int z) {
        if(x < min.x) return false;
        if(y < min.y) return false;
        if(z < min.z) return false;
        
        if(x > max.x) return false;
        if(y > max.y) return false;
        if(z > max.z) return false;
        
        return true;
    }
    
    public Collection<Chunk> getChunks(World world) {
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        if(world == null) return chunks;
        
        int x1 = min.x >> 4;
        int z1 = min.z >> 4;
        int x2 = max.x >> 4;
        int z2 = max.z >> 4;
        
        for(int x = x1; x <= x2; x++) {
            for(int z = z1; z <= z2; z++) {
                chunks.add(world.getChunkAt(x, z));
            }
        }
        
        return chunks;
    }
}
