package com.nyfaria.nyfspetshop.entity.ifaces;

public interface Thirsty {
    float getThirstLevel();
    void setThirstLevel(float thirstLevel);
    void tickThirst();
}
