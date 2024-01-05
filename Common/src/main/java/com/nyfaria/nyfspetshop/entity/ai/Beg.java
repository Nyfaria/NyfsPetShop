package com.nyfaria.nyfspetshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.nyfspetshop.entity.BasePet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.FreePositionTracker;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Beg<T extends BasePet> extends ExtendedBehaviour<T> {
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> BEGGING = ObjectArrayList.of(Pair.of(MemoryModuleType.NEAREST_PLAYERS, MemoryStatus.VALUE_PRESENT));;
    private String animation;
    private String animation2;
    private Item begItem;
    private String controller;
    private String controller2 = "";
    private Player treatPlayer;

    @Override
    protected void start(ServerLevel level, T entity, long gameTime) {
        super.start(level, entity, gameTime);
        entity.triggerAnim(controller,animation);
        if(!controller2.isEmpty()) {
            entity.triggerAnim(controller2, animation2);
        }
        entity.setBegging(true);
        entity.refreshDimensions();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T entity) {
        return checkForTreat(entity);
    }

    private boolean checkForTreat(T entity) {
        @Nullable List<Player> player = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_PLAYERS);
        if (player == null || player.isEmpty()) {
            return false;
        }
        for(Player p : player){
            if (p.distanceTo(entity) < 5 && p.getMainHandItem().is(begItem) || p.getOffhandItem().is(begItem)){
                treatPlayer = p;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void start(T entity) {
        super.start(entity);
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new FreePositionTracker(treatPlayer.getEyePosition().subtract(0.0,0.5,0.0)));
    }

    @Override
    protected void tick(ServerLevel level, T entity, long gameTime) {
        super.tick(level, entity, gameTime);
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new FreePositionTracker(treatPlayer.getEyePosition().subtract(0.0,0.5,0.0)));
    }

    @Override
    protected void stop(ServerLevel level, T entity, long gameTime) {
        super.stop(level, entity, gameTime);
        if(!checkForTreat(entity)){
            entity.triggerAnim(controller, "idle");
            if(!controller2.isEmpty()){
            entity.triggerAnim(controller2, "idle");
            }
            @Nullable PositionTracker lookTarget = BrainUtils.getMemory(entity, MemoryModuleType.LOOK_TARGET);
            BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, null);
            BrainUtils.setForgettableMemory(entity, MemoryModuleType.LOOK_TARGET, lookTarget, 200);
            entity.setBegging(false);
            entity.refreshDimensions();
        }
    }

    public final Beg<T> setAnimation(String animation) {
        this.animation = animation;
        return this;
    }
    public final Beg<T> setBegItem(Item begItem) {
        this.begItem = begItem;
        return this;
    }
    public final Beg<T> setController(String controller) {
        this.controller = controller;
        return this;
    }
    public final Beg<T> setAnimation2(String animation2) {
        this.animation2 = animation2;
        return this;
    }
    public final Beg<T> setController2(String controller2) {
        this.controller2 = controller2;
        return this;
    }
    @Override
    protected boolean shouldKeepRunning(T entity) {
        return checkForTreat(entity);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return BEGGING;
    }
}
