package com.nyfaria.nyfspetshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.nyfspetshop.entity.BaseDog;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;

import java.util.List;

public class Dig<T extends BaseDog> extends DelayedBehaviour<T> {


    private String startAnimation = "";
    private String animationController = "";

    public Dig(int delayTicks) {
        super(delayTicks);
    }

    public final Dig startAnimation(String startAnimation){
        this.startAnimation = startAnimation;
        return this;
    }
    public final Dig animationController(String animationController){
        this.animationController = animationController;
        return this;
    }


    @Override
    protected void start(T entity) {
        super.start(entity);
        entity.triggerAnim(animationController, startAnimation);
    }

    @Override
    protected void doDelayedAction(T entity) {

    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return null;
    }
}
