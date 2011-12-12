package com.minecarts.auramanager.listener;

import com.minecarts.auramanager.AuraManager;
import org.bukkit.event.world.*;

public class WorldListener extends org.bukkit.event.world.WorldListener {
    
    private AuraManager plugin;
    
    public WorldListener(AuraManager plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onWorldLoad(WorldLoadEvent event) {
        plugin.log("{0}", event);
    }
    
    @Override
    public void onChunkLoad(ChunkLoadEvent event) {
        
    }
}
