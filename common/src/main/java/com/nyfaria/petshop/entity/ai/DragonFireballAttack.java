package com.nyfaria.petshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.petshop.entity.BaseDragon;
import com.nyfaria.petshop.entity.DragonFireball;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class DragonFireballAttack<E extends BaseDragon> extends DelayedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT)
    );

    protected Function<E, Integer> cooldownSupplier = entity -> entity.getRandom().nextIntBetweenInclusive(1200, 2400);
    protected float attackRadius = 256f;

    @Nullable
    protected LivingEntity target = null;

    public DragonFireballAttack(int delayTicks) {
        super(delayTicks);
    }

    public DragonFireballAttack<E> cooldown(Function<E, Integer> supplier) {
        this.cooldownSupplier = supplier;
        return this;
    }

    public DragonFireballAttack<E> attackRadius(float radius) {
        this.attackRadius = radius * radius;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.target = BrainUtils.getTargetOfEntity(entity);
        if (this.target == null || !(this.target instanceof Monster)) {
            return false;
        }
        return BrainUtils.canSee(entity, this.target) && entity.distanceToSqr(this.target) <= this.attackRadius;
    }

    @Override
    protected void start(E entity) {
        BehaviorUtils.lookAtEntity(entity, this.target);
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }

    @Override
    protected void doDelayedAction(E entity) {
        if (this.target == null || !(this.target instanceof Monster)) {
            return;
        }

        if (!BrainUtils.canSee(entity, this.target) || entity.distanceToSqr(this.target) > this.attackRadius) {
            return;
        }

        Vec3 targetPos = this.target.position();
        Vec3 entityPos = entity.position();
        double dx = targetPos.x - entityPos.x;
        double dy = targetPos.y - entityPos.y;
        double dz = targetPos.z - entityPos.z;

        DragonFireball fireball = new DragonFireball(entity.level(), entity, dx, dy, dz);
        fireball.setPos(entity.getX(), entity.getEyeY(), entity.getZ());
        entity.level().addFreshEntity(fireball);

        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.cooldownSupplier.apply(entity));
    }
}
