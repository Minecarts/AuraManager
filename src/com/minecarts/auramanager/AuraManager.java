package com.minecarts.auramanager;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.text.MessageFormat;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.configuration.file.FileConfiguration;

import com.minecarts.auramanager.listener.*;
import org.bukkit.event.Listener;

import org.bukkit.event.Event.Type;
import org.bukkit.event.Event.Priority;
import static org.bukkit.event.Event.Type.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;

import com.minecarts.dbquery.DBQuery;
import com.minecarts.dbconnector.provider.Provider;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.nio.ByteBuffer;


public class AuraManager extends JavaPlugin {
    private static final Logger logger = Logger.getLogger("com.minecarts.auramanager"); 
    
    protected boolean debug = true;
    protected FileConfiguration config;
    
    protected DBQuery dbq;
    protected Provider provider;
    
    private final HashMap<Integer, Aura> auras = new HashMap<Integer, Aura>();
    private final HashMap<Chunk, ArrayList<Aura>> chunkIndex = new HashMap<Chunk, ArrayList<Aura>>();
    
    
    public void onEnable() {
        dbq = (DBQuery) getServer().getPluginManager().getPlugin("DBQuery");
        reloadConfig();
        
        PluginManager pluginManager = getServer().getPluginManager();
        HashMap<Listener, Type[]> listeners = new HashMap<Listener, Type[]>() {{
            put(new WorldListener(AuraManager.this), new Type[]{ WORLD_LOAD, CHUNK_LOAD });
        }};
        
        for(Entry<Listener, Type[]> entry : listeners.entrySet()) {
            for(Type type : entry.getValue()) {
                pluginManager.registerEvent(type, entry.getKey(), Priority.Monitor, this);
            }
        }
        
        log("Version {0} enabled.", getDescription().getVersion());
        
        new AuraQuery("SELECT * FROM auras WHERE world=\"test_world\"").sync().fetch();
    }
    
    public void onDisable() {
    }
    
    
    public void log(String message) {
        log(Level.INFO, message);
    }
    public void log(Level level, String message) {
        logger.log(level, MessageFormat.format("{0}> {1}", getDescription().getName(), message));
    }
    public void log(String message, Object... args) {
        log(MessageFormat.format(message, args));
    }
    public void log(Level level, String message, Object... args) {
        log(level, MessageFormat.format(message, args));
    }
    
    public void debug(String message) {
        if(debug) log(message);
    }
    public void debug(String message, Object... args) {
        if(debug) log(message, args);
    }
    
    
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        
        if(config == null) config = getConfig();
        
        debug = config.getBoolean("debug");
        provider = dbq.getProvider(config.getString("DBConnector.provider"));
    }
    
    
    private void addAura(Aura aura) {
        auras.put(aura.getId(), aura);
        
        for(Chunk chunk : aura.getChunks()) {
            ArrayList<Aura> index = chunkIndex.get(chunk);
            
            if(index == null) {
                index = new ArrayList<Aura>();
                chunkIndex.put(chunk, index);
            }
            
            index.add(aura);
        }
    }
    
    public Aura getAuraAt(Block block, Flag filter) {
        ArrayList<Aura> index = chunkIndex.get(block.getChunk());
        if(index == null) return null;
        
        for(Aura aura : index) {
            if(aura.isActive() && aura.hasFlag(filter) && aura.contains(block)) {
                return aura;
            }
        }
        
        return null;
    }
    
    
    class Query extends com.minecarts.dbquery.Query {
        public Query(String sql, boolean async) {
            this(sql);
            this.async = async;
        }
        public Query(String sql) {
            super(AuraManager.this, AuraManager.this.provider, sql);
        }
        
        @Override
        public void onComplete(FinalQuery query) {
            if(query.elapsed() < 500) {
                debug("Query took {0,number,#} ms", query.elapsed());
            }
            else {
                log("Slow query took {0,number,#} ms", query.elapsed());
            }
        }
    }
    
    class AuraQuery extends Query {
        public AuraQuery(String sql) {
            super(sql);
        }
        
        @Override
        public void onFetch(ArrayList<HashMap> rows) {
            if(rows.isEmpty()) return;
            
            AuraManager.this.log("Fetched {0} auras", rows.size());
            
            final Server server = getServer();
            int orphans = 0;
            
            for(HashMap row : rows) {
                Integer id = (Integer) row.get("id");
                String worldName = (String) row.get("world");
                
                World world = server.getWorld(worldName);
                if(world == null) {
                    AuraManager.this.debug("Aura #{0} world ({1}) not found", id, worldName);
                    orphans++;
                    continue;
                }
                
                int x = (Integer) row.get("block_x");
                int y = (Integer) row.get("block_y");
                int z = (Integer) row.get("block_z");
                Block origin = world.getBlockAt(x, y, z);
                
                if(origin.getTypeId() != (Integer) row.get("block_id")) {
                    AuraManager.this.debug("Aura #{0} origin block type does not match", id);
                    orphans++;
                    continue;
                }
                
                Coordinate left = new Coordinate((Integer) row.get("x1"), (Integer) row.get("y1"), (Integer) row.get("z1"));
                Coordinate right = new Coordinate((Integer) row.get("x2"), (Integer) row.get("y2"), (Integer) row.get("z2"));
                BoundingBox bounds = new BoundingBox(left, right);
                
                Flags flags = Flags.fromInt(ByteBuffer.wrap((byte[]) row.get("flags")).getInt());
                
                addAura(new Aura(id, origin, bounds, flags));
            }
            
            AuraManager.this.log("{0} auras could not be loaded (no world, wrong block type, etc.)", orphans);
        }
        
        
    }
}
