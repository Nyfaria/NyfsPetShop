package com.nyfaria.petshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.petshop.entity.BasePet;
import com.nyfaria.petshop.init.MemoryModuleTypeInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.FreePositionTracker;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class Dig<T extends BasePet> extends ExtendedBehaviour<T> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleTypeInit.DIG_POS.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleTypeInit.DIGGING.get(), MemoryStatus.VALUE_PRESENT)
    );


    private String digAnimation = "dig";
    private String digAnimation2 = "tail_wag_sit";
    private String controller = "move_controller";
    private String controller2 = "tail_controller";
    private String stopAnimation = "idle";

    public Dig() {
        super();
        runFor(entity -> Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T entity) {
        BlockPos digTarget = BrainUtils.getMemory(entity, MemoryModuleTypeInit.DIG_POS.get()).get();
        if (level.getBlockEntity(digTarget) instanceof BrushableBlockEntity) {
            return true;
        }
        BrainUtils.clearMemory(entity, MemoryModuleTypeInit.DIG_POS.get());
        BrainUtils.clearMemory(entity, MemoryModuleTypeInit.DIGGING.get());
        BrainUtils.clearMemory(entity, SBLMemoryTypes.NEARBY_BLOCKS.get());
        return false;
    }

    @Override
    protected void start(ServerLevel level, T entity, long gameTime) {
        super.start(level, entity, gameTime);
        BlockPos digTarget = BrainUtils.getMemory(entity, MemoryModuleTypeInit.DIG_POS.get()).get();
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new FreePositionTracker(new Vec3(digTarget.getX(), digTarget.getY(), digTarget.getZ())));
        entity.triggerAnim(controller, digAnimation);
        entity.triggerAnim(controller2, digAnimation2);

    }

    @Override
    protected boolean shouldKeepRunning(T entity) {
        return BrainUtils.hasMemory(entity, MemoryModuleTypeInit.DIG_POS.get()) && canStillDig((ServerLevel) entity.level(), entity);
    }

    @Override
    protected void tick(ServerLevel level, T entity, long gameTime) {
        if (canStillDig(level, entity)) {
            BrushableBlockEntity brushableBlockEntity = (BrushableBlockEntity) level.getBlockEntity(BrainUtils.getMemory(entity, MemoryModuleTypeInit.DIG_POS.get()).get());
            if (brushableBlockEntity != null) {
                brushableBlockEntity.brush(gameTime, (Player) entity.getOwner(), Direction.UP);
            }
        }
        super.tick(level, entity, gameTime);

    }

    private boolean canStillDig(ServerLevel level, T entity) {
        BlockPos digTarget = BrainUtils.getMemory(entity, MemoryModuleTypeInit.DIG_POS.get()).get();
        return level.getBlockEntity(digTarget) instanceof BrushableBlockEntity;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void stop(ServerLevel level, T entity, long gameTime) {
        super.stop(level, entity, gameTime);

        if (!BrainUtils.hasMemory(entity, MemoryModuleTypeInit.DIG_POS.get()) || !canStillDig(level, entity)) {
            entity.triggerAnim(controller, stopAnimation);
            entity.triggerAnim(controller2, stopAnimation);
            BrainUtils.clearMemory(entity.getBrain(), MemoryModuleTypeInit.DIGGING.get());
            BrainUtils.clearMemory(entity.getBrain(), MemoryModuleTypeInit.DIG_POS.get());
            BrainUtils.clearMemory(entity, SBLMemoryTypes.NEARBY_BLOCKS.get());
        }
    }

    protected Dig<T> digAnimation(String digAnimation) {
        this.digAnimation = digAnimation;
        return this;
    }

    protected Dig<T> controller(String controller) {
        this.controller = controller;
        return this;
    }

    protected Dig<T> stopAnimation(String stopAnimation) {
        this.stopAnimation = stopAnimation;
        return this;
    }

    protected Dig<T> digAnimation2(String digAnimation2) {
        this.digAnimation2 = digAnimation2;
        return this;
    }

    protected Dig<T> controller2(String controller2) {
        this.controller2 = controller2;
        return this;
    }
}
