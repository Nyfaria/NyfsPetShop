package com.nyfaria.petshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.petshop.block.PetBowl;
import com.nyfaria.petshop.entity.BasePet;
import com.nyfaria.petshop.init.BlockStateInit;
import com.nyfaria.petshop.init.MemoryModuleTypeInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoToBowl<E extends BasePet> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED), Pair.of(MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleTypeInit.BOWL_POS.get(), MemoryStatus.VALUE_PRESENT));

    @Nullable
    protected Path path;
    @Nullable
    protected BlockPos lastTargetPos;
    protected float speedModifier;

    public GoToBowl() {
        runFor(entity -> entity.getRandom().nextInt(100) + 150);
        cooldownFor(entity -> entity.getRandom().nextInt(40));
    }

    private static <E extends BasePet> void drinkFromBowl(E entity, BlockPos walkTarget) {
        BlockState state = entity.level().getBlockState(walkTarget);
        PetBowl.Type type = state.getValue(BlockStateInit.BOWL_TYPE);
        entity.performBowlAction(type);
        entity.playSound(type.getSound());
        int fullnessity = state.getValue(BlockStateInit.FULLNESSITY);
        if (fullnessity <= 1)
            entity.level().setBlockAndUpdate(walkTarget, state.setValue(BlockStateInit.BOWL_TYPE, PetBowl.Type.EMPTY).setValue(BlockStateInit.FULLNESSITY, 0));
        else
            entity.level().setBlockAndUpdate(walkTarget, state.setValue(BlockStateInit.FULLNESSITY, fullnessity - 1));
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        Brain<?> brain = entity.getBrain();
        BlockPos walkTarget = BrainUtils.getMemory(brain, MemoryModuleTypeInit.BOWL_POS.get()).get();

        if (!hasReachedTarget(entity, walkTarget) && attemptNewPath(entity, walkTarget, false)) {
            this.lastTargetPos = walkTarget;

            return true;
        } else if (hasReachedTarget(entity, walkTarget)) {
            drinkFromBowl(entity, walkTarget);
        }

        BrainUtils.clearMemory(brain, MemoryModuleTypeInit.BOWL_POS.get());
        BrainUtils.clearMemory(brain, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

        return false;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        if (this.path == null || this.lastTargetPos == null)
            return false;

        if (entity.getNavigation().isDone())
            return false;

        BlockPos walkTarget = BrainUtils.getMemory(entity, MemoryModuleTypeInit.BOWL_POS.get()).get();

        return !hasReachedTarget(entity, walkTarget);
    }

    @Override
    protected void start(E entity) {
        startOnNewPath(entity);
    }

    @Override
    protected void tick(E entity) {
        Path path = entity.getNavigation().getPath();
        Brain<?> brain = entity.getBrain();

        if (this.path != path) {
            this.path = path;

            BrainUtils.setMemory(brain, MemoryModuleType.PATH, path);
        }

        if (path != null && this.lastTargetPos != null) {
            BlockPos walkTarget = BrainUtils.getMemory(brain, MemoryModuleTypeInit.BOWL_POS.get()).get();

            if (walkTarget.distSqr(this.lastTargetPos) > 4 && attemptNewPath(entity, walkTarget, hasReachedTarget(entity, walkTarget))) {
                this.lastTargetPos = walkTarget;

                startOnNewPath(entity);
            }

        }

    }

    @Override
    protected void stop(E entity) {
        Brain<?> brain = entity.getBrain();
        BlockPos walkTarget = BrainUtils.getMemory(brain, MemoryModuleTypeInit.BOWL_POS.get()).get();
        if (hasReachedTarget(entity, walkTarget)) {
            drinkFromBowl(entity, walkTarget);
        }
        if (!entity.getNavigation().isStuck() || !BrainUtils.hasMemory(brain, MemoryModuleTypeInit.BOWL_POS.get()) || hasReachedTarget(entity, BrainUtils.getMemory(brain, MemoryModuleTypeInit.BOWL_POS.get()).get()))
            this.cooldownFinishedAt = 0;

        entity.getNavigation().stop();
        BrainUtils.clearMemories(brain, MemoryModuleTypeInit.BOWL_POS.get(), MemoryModuleType.PATH);

        this.path = null;
    }

    protected boolean attemptNewPath(E entity, BlockPos walkTarget, boolean reachedCurrentTarget) {
        Brain<?> brain = entity.getBrain();
        BlockPos pos = walkTarget;
        this.path = entity.getNavigation().createPath(pos, 0);
        this.speedModifier = 1;

        if (reachedCurrentTarget) {
            BrainUtils.clearMemory(brain, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

            return false;
        }

        if (this.path != null && this.path.canReach()) {
            BrainUtils.clearMemory(brain, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        } else {
            BrainUtils.setMemory(brain, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, entity.level().getGameTime());
        }

        if (this.path != null)
            return true;

        Vec3 newTargetPos = DefaultRandomPos.getPosTowards(entity, 10, 7, Vec3.atBottomCenterOf(pos), Mth.HALF_PI);

        if (newTargetPos != null) {
            this.path = entity.getNavigation().createPath(newTargetPos.x(), newTargetPos.y(), newTargetPos.z(), 0);

            return this.path != null;
        }

        return false;
    }

    protected boolean hasReachedTarget(E entity, BlockPos target) {
        return target.distManhattan(entity.blockPosition()) <= 2.0f;
    }

    protected void startOnNewPath(E entity) {
        BrainUtils.setMemory(entity, MemoryModuleType.PATH, this.path);
        entity.getNavigation().moveTo(this.path, this.speedModifier);
    }

}