package com.nyfaria.petshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.petshop.entity.BasePet;
import com.nyfaria.petshop.entity.enums.MovementType;
import com.nyfaria.petshop.init.MemoryModuleTypeInit;
import com.nyfaria.petshop.init.POIInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.object.TriPredicate;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FindPOI<E extends BasePet> extends PetExtendedBehavior<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleTypeInit.WAIT_TO_SEARCH_AGAIN.get(), MemoryStatus.VALUE_ABSENT)
    );
    public MemoryModuleType<Optional<BlockPos>> memoryModuleType = null;
    public TagKey<PoiType> poiTag = null;
    private TriPredicate<Level, BlockPos, BlockState> propertyPredicate = (level, pos, property) -> true;
    private PoiManager.Occupancy occupancy = PoiManager.Occupancy.ANY;

    public FindPOI() {
        super();
        movementTypePredicate((pet, movementType) -> movementType != MovementType.STAY);
        cooldownFor(entity -> 200);
    }

    public FindPOI<E> withMemory(MemoryModuleType<Optional<BlockPos>> memoryModuleType) {
        this.memoryModuleType = memoryModuleType;
        return this;
    }

    public FindPOI<E> withTag(TagKey<PoiType> poiTag) {
        this.poiTag = poiTag;
        return this;
    }

    public FindPOI<E> withOccupancy(PoiManager.Occupancy occupancy) {
        this.occupancy = occupancy;
        return this;
    }

    public FindPOI<E> checkState(TriPredicate<Level, BlockPos, BlockState> propertyPredicate) {
        this.propertyPredicate = propertyPredicate;
        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return super.checkExtraStartConditions(level, entity) && memoryModuleType != null && poiTag != null;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        super.start(level, entity, gameTime);

        PoiType poiType = POIInit.PET_BEDS.get();
        BlockPos blockpos = entity.blockPosition();
        PoiManager poimanager = level.getPoiManager();
        Stream<PoiRecord> stream = poimanager.getInRange((poiTypeHolder) -> {
            boolean isRightType = poiTypeHolder.is(poiTag);
            return isRightType;
        }, blockpos, 20, occupancy);
        List<BlockPos> posList = stream.map(PoiRecord::getPos).toList();
        List<BlockPos> filteredPosList = posList.stream().filter(pos -> propertyPredicate.test(level, pos, level.getBlockState(pos))).toList();
        List<BlockPos> sortedFilteredPosList = filteredPosList.stream().sorted(Comparator.comparingDouble((pos) -> pos.distSqr(blockpos))).toList();
        if (!sortedFilteredPosList.isEmpty()) {
            BrainUtils.setMemory(entity, memoryModuleType, Optional.of(sortedFilteredPosList.get(0)));
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }
}
