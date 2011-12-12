package com.minecarts.auramanager;

import org.bukkit.block.Block;
import org.bukkit.World;

public class Coordinate {
    public final int x;
    public final int y;
    public final int z;
    
    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public static Coordinate min(Coordinate a, Coordinate b) {
        return new Coordinate(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
    }
    public static Coordinate max(Coordinate a, Coordinate b) {
        return new Coordinate(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
    }
    
    public static Coordinate fromBlock(Block block) {
        return new Coordinate(block.getX(), block.getY(), block.getZ());
    }
    public Block toBlock(World world) {
        return world.getBlockAt(x, y, z);
    }
    
}
