package com.minecarts.auramanager;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Chunk;

public class Aura {
    private Integer id;
    private boolean updated;
    
    private World world;
    private Coordinate origin;
    private Material originType;
    private BoundingBox bounds;
    private Flags flags;
    
    
    private Aura() {
    }
    
    public Aura(Integer id, Block origin, BoundingBox bounds, Flags flags) {
        this.id = id;
        this.world = origin.getWorld();
        this.origin = Coordinate.fromBlock(origin);
        this.originType = origin.getType();
        this.bounds = bounds;
        this.flags = flags;
    }
    
    
    public boolean isActive() {
        return originType == origin.toBlock(world).getType();
    }
    
    public boolean hasFlag(Flag flag) {
        return flags.has(flag);
    }
    
    public boolean contains(Block block) {
        return bounds.contains(Coordinate.fromBlock(block));
    }
    
    public int getId() {
        return id;
    }
    
    public Collection<Chunk> getChunks() {
        return bounds.getChunks(world);
    }
    
}
