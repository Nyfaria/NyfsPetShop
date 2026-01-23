package com.nyfaria.petshop.entity.ifaces;

public interface Thirsty {
    float getThirstLevel();

    void setThirstLevel(float thirstLevel);

    void tickThirst();
}
