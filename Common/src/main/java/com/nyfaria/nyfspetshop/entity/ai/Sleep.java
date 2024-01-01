package com.nyfaria.nyfspetshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.nyfspetshop.entity.BasePet;
import com.nyfaria.nyfspetshop.init.MemoryModuleTypeInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class Sleep<T extends BasePet> extends ExtendedBehaviour<T> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleTypeInit.SLEEPING.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleTypeInit.DIG_POS.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleTypeInit.DIGGING.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleTypeInit.BOWL_POS.get(), MemoryStatus.VALUE_ABSENT)

    );


    private String sleepAnimation = "sleep";
    private String sleepAnimation2 = "idle";
    private String controller = "move_controller";
    private String controller2 = "tail_controller";
    private String stopAnimation = "idle";

    public Sleep() {
        super();
        runFor(entity -> Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T entity) {
        return level.isNight() || level.random.nextInt(100) < 10;
    }

    @Override
    protected void start(ServerLevel level, T entity, long gameTime) {
        super.start(level, entity, gameTime);
        entity.triggerAnim(controller, sleepAnimation);
        entity.triggerAnim(controller2, sleepAnimation2);

    }


    @Override
    protected boolean shouldKeepRunning(T entity) {
        return canStillSleep((ServerLevel) entity.level(), entity);
    }


    private boolean canStillSleep(ServerLevel level, T entity) {
        return level.isNight() || entity.getRandom().nextInt(100) > 50;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void stop(ServerLevel level, T entity, long gameTime) {
        super.stop(level, entity, gameTime);

        if (!BrainUtils.hasMemory(entity, MemoryModuleTypeInit.SLEEPING.get()) || !canStillSleep(level, entity)) {
            entity.triggerAnim(controller, stopAnimation);
            entity.triggerAnim(controller2, stopAnimation);
//            level.getPoiManager().release(BrainUtils.getMemory(entity,MemoryModuleTypeInit.BED.get()).get());
            BrainUtils.clearMemory(entity.getBrain(), MemoryModuleTypeInit.SLEEPING.get());
            BrainUtils.clearMemory(entity.getBrain(), MemoryModuleTypeInit.BED.get());
        }
    }

    protected Sleep<T> sleepAnimation(String digAnimation) {
        this.sleepAnimation = digAnimation;
        return this;
    }

    protected Sleep<T> controller(String controller) {
        this.controller = controller;
        return this;
    }

    protected Sleep<T> stopAnimation(String stopAnimation) {
        this.stopAnimation = stopAnimation;
        return this;
    }

    protected Sleep<T> sleepAnimation2(String digAnimation2) {
        this.sleepAnimation2 = digAnimation2;
        return this;
    }

    protected Sleep<T> controller2(String controller2) {
        this.controller2 = controller2;
        return this;
    }
}
