package com.nyfaria.nyfspetshop.entity.ifaces;

public interface Hungry {
    float getHungerLevel();
    void setHungerLevel(float hungerLevel);
    void tickHunger();
}
