package com.minecarts.auramanager;

import java.util.BitSet;

public enum Flag {
    BLOCK_PLACE(0), // prevent placing a block
    BLOCK_BREAK(1), // prevent destroying a block, trampling plants, etc.
    BLOCK_MODIFY(29), // prevent modifying note blocks, repeaters (diodes), etc.
    BUILD(BLOCK_PLACE, BLOCK_BREAK, BLOCK_MODIFY), // prevent building
    
    CONTAINER_OPEN(2), // prevent opening a container (chest, furnace, dispenser)
    CONTAINER_WITHDRAW(3), // prevent withdrawing items from a container (not yet implemented)
    CONTAINER_DEPOSIT(4), // prevent depositing items into a container (not yet implemented)
    CONTAINER(CONTAINER_OPEN, CONTAINER_WITHDRAW, CONTAINER_DEPOSIT), // prevent container activity
    
    PVP_DEAL_DAMAGE(5), // prevent damage done to other players
    PVP_TAKE_DAMAGE(6), // prevent damage taken from other players
    PVP(PVP_DEAL_DAMAGE, PVP_TAKE_DAMAGE), // prevents all PVP
    
    EXPLOSION_CREEPER(7), // prevent block damage from creeper explosions
    EXPLOSION_TNT(8), // prevent block damage from TNT explosions
    EXPLOSION_FIREBALL(9), // prevent block damage from fireball explosions (ghasts)
    EXPLOSION(EXPLOSION_CREEPER, EXPLOSION_TNT, EXPLOSION_FIREBALL), // prevent block damage from explosions
    
    // Flow is a complicated issue to solve with multiple sources, including natural, creating new sources.
    // Ideally, inbound flow from a source block placed by a member of your aura will be allowed, everything else disallowed.
    // This may not be possible, so the compromise is disabling the use of water/lava buckets outside of auras and
    // allowing them in auras they hold a membership to while disabling outbound flow and preventing the removal of auras
    // containing lava. This opens up the potential of creating a blanket of auras filled with water, then removing the auras
    // so the water flows into other auras, but we'll track bucket use to see who's behind these acts.
    LAVA_FLOW(10), // prevent lava flowing in
    WATER_FLOW(11), // prevent lava flowing in
    FLOW(LAVA_FLOW, WATER_FLOW), // prevent all flowing in
    
    MONSTER_DAMAGE(12), // prevent damage taken from and done to monsters
    ANIMAL_DAMAGE(13), // prevent damage taken from and done to animals
    CREATURE_DAMAGE(MONSTER_DAMAGE, ANIMAL_DAMAGE), // prevent damage to and taken from all creatures
    
    MONSTER_SPAWN(14), // prevent monsters spawning
    ANIMAL_SPAWN(15), // prevent animals spawning
    CREATURE_SPAWN(MONSTER_SPAWN, ANIMAL_SPAWN), // prevent all creatures spawning
    
    MONSTER_TARGET(16), // remove monster target and prevent targeting
    ANIMAL_TARGET(17), // do animals target?
    CREATURE_TARGET(MONSTER_TARGET, ANIMAL_TARGET), // prevent creature targeting
    
    ITEM_PICKUP(18), // prevent items from being picked up
    ITEM_DROP(19), // prevent items from dropping
    
    BLOCK_IGNITE(20), // prevent blocks from igniting (flint, fire spread)
    BLOCK_BURN(21), // prevent blocks from burning (ever burning fires!)
    FIRE(BLOCK_IGNITE, BLOCK_BURN),
    
    BLOCK_INTERACT(22), // prevents interacting with buttons and levers
    
    VEHICLE_PLACE(23), // prevents placing a vehicle (boat, minecart)
    VEHICLE_BREAK(24), // prevents breaking a vehicle
    VEHICLE_ENTER(25), // prevents boarding a vehicle
    VEHICLE_EXIT(26), // prevents deboarding a vehicle
    VEHICLE_PUSH(27), // prevents pushing a vehicle (entity collision)
    VEHICLE(VEHICLE_PLACE, VEHICLE_BREAK, VEHICLE_ENTER, VEHICLE_EXIT, VEHICLE_PUSH), // prevents vehicle use
    
    PVE_DAMAGE(28), // prevent all environmental damage (suffocation, falling, fire, etc.)
    DAMAGE(PVP, CREATURE_DAMAGE, PVE_DAMAGE), // prevent all damage, including suicide (immunity)
    
    // BLOCK_MODIFY(29) defined above
    
    // Undecided:
    // BLOCK_GROW // prevent wheat, sugarcane, and saplings from growing
    // BLOCK_DECAY // prevent leaves from decaying
    // BLOCK_CHANGE // grow, decay, melt?
    // BLOCK_GRAVITY // sand, gravel, water
    // PISTON // prevent pistons from pushing blocks (or pulling in the case of sticky pistons)
    
    ;
    
    private final BitSet bits = new BitSet();
    
    private Flag(int index) {
        bits.set(index, true);
    }
    private Flag(Flag... flags) {
        for(Flag flag : flags) {
            bits.and(flag.bits);
        }
    }
    
    public BitSet getBits() {
        return (BitSet) bits.clone();
    }
}
