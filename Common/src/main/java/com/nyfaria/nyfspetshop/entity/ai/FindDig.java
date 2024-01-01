package com.nyfaria.nyfspetshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.entity.BasePet;
import com.nyfaria.nyfspetshop.entity.enums.MovementType;
import com.nyfaria.nyfspetshop.init.BlockStateInit;
import com.nyfaria.nyfspetshop.init.MemoryModuleTypeInit;
import com.nyfaria.nyfspetshop.init.TagInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindDig<E extends BasePet> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleTypeInit.DIG_POS.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleTypeInit.WAIT_TO_SEARCH_AGAIN.get(), MemoryStatus.VALUE_ABSENT)
    );

    public FindDig() {
        super();
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
        } else {
            BrainUtils.setForgettableMemory(entity, MemoryModuleTypeInit.WAIT_TO_SEARCH_AGAIN.get(), true, 200);
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }
}
