package com.nyfaria.petshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.petshop.entity.BasePet;
import com.nyfaria.petshop.entity.enums.MovementType;
import com.nyfaria.petshop.init.MemoryModuleTypeInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Optional;

public class FindDig<E extends BasePet> extends PetExtendedBehavior<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleTypeInit.DIG_POS.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleTypeInit.WAIT_TO_SEARCH_AGAIN.get(), MemoryStatus.VALUE_ABSENT)
    );
    private MemoryModuleType<Optional<BlockPos>> poiPos;

    public FindDig() {
        cooldownFor(entity -> 200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        MovementType movementType = entity.getMovementType();
        return movementType != MovementType.STAY;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        super.start(level, entity, gameTime);
        List<Pair<BlockPos, BlockState>> list = BrainUtils.getMemory(entity, SBLMemoryTypes.NEARBY_BLOCKS.get()).stream().toList();
        if (!list.isEmpty()) {
            BrainUtils.setMemory(entity, MemoryModuleTypeInit.DIG_POS.get(), Optional.of(list.get(0).getFirst()));
        }
    }


    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }
}
