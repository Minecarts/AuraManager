package com.minecarts.auramanager;

public class Flags {
    private final int value;
    
    private Flags(int value) {
        this.value = value;
    }
    
    public Flags(Flag... flags) {
        int total = 0;
        for(Flag flag : flags) {
            total |= flag.toInt();
        }
        value = total;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof Flags) return value == ((Flags) obj).value;
        return false;
    }
    @Override
    public int hashCode() {
        return value;
    }
    
    public int toInt() {
        return value;
    }
    @Override
    public String toString() {
        return Integer.toBinaryString(value);
    }
    
    public static Flags fromInt(int value) {
        return new Flags(value);
    }
    public static Flags fromString(String binary) {
        return new Flags(Integer.parseInt(binary, 2));
    }
    
    
    public boolean has(Flag flag) {
        return (value & flag.toInt()) > 0;
    }
}
