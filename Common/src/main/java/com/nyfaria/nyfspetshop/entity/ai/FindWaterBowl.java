package com.nyfaria.nyfspetshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.entity.BasePet;
import com.nyfaria.nyfspetshop.entity.enums.MovementType;
import com.nyfaria.nyfspetshop.entity.ifaces.Thirsty;
import com.nyfaria.nyfspetshop.init.BlockInit;
import com.nyfaria.nyfspetshop.init.BlockStateInit;
import com.nyfaria.nyfspetshop.init.MemoryModuleTypeInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindWaterBowl<E extends BasePet & Thirsty> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleTypeInit.WATER_BOWL.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleTypeInit.WAIT_TO_SEARCH_AGAIN.get(), MemoryStatus.VALUE_ABSENT)
    );

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        float thirstLevel = entity.getThirstLevel();
        MovementType movementType = entity.getMovementType();
        return thirstLevel < 0.8f && movementType != MovementType.STAY;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        super.start(level, entity, gameTime);

        BlockPos blockpos = entity.blockPosition();
        PoiManager poimanager = level.getPoiManager();
        Stream<PoiRecord> stream = poimanager.getInRange((p_218130_) -> {
            return p_218130_.is(BlockInit.PET_BOWL.getId());
        }, blockpos, 20, PoiManager.Occupancy.ANY);
        List<BlockPos> list = stream.map(PoiRecord::getPos).filter(pos -> level.getBlockState(pos).getValue(BlockStateInit.BOWL_TYPE) == PetBowl.Type.WATER).sorted(Comparator.comparingDouble((p_148811_) -> {
            return p_148811_.distSqr(blockpos);
        })).collect(Collectors.toList());
        if (!list.isEmpty()) {
            BrainUtils.setMemory(entity, MemoryModuleTypeInit.WATER_BOWL.get(), Optional.of(list.get(0)));
        } else {
            BrainUtils.setForgettableMemory(entity, MemoryModuleTypeInit.WAIT_TO_SEARCH_AGAIN.get(), true, 200);
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }
}
