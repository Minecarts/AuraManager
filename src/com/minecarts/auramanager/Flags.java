package com.minecarts.auramanager;

import java.util.BitSet;

public class Flags {
    private final BitSet bits = new BitSet();
    
    public Flags(Flag... flags) {
        for(Flag flag : flags) {
            bits.or(flag.getBits());
        }
    }
    
    @Override
    public String toString() {
        return bits.toString();
    }
    public long toLong() {
        return Long.parseLong(bits.toString(), 2);
    }
    public int toInt() {
        return Integer.parseInt(bits.toString(), 2);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof Flags) return bits.equals(((Flags) obj).bits);
        if(obj instanceof BitSet) return bits.equals((BitSet) obj);
        return false;
    }
    @Override
    public int hashCode() {
        return bits.hashCode();
    }
    
    public boolean has(Flag flag) {
        Flags flags = new Flags(flag);
        flags.bits.and(this.bits);
        return equals(flags);
    }
}
