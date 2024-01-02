package com.nyfaria.nyfspetshop.block;

import com.nyfaria.nyfspetshop.block.entity.BirdCageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BirdCage extends BaseEntityBlock {
    public BirdCage(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            BlockPos posBelow = pos.below();
            use(level.getBlockState(posBelow), level, posBelow, player, interactionHand, hitResult);
        } else {
            BirdCageBlockEntity blockEntity = (BirdCageBlockEntity) level.getBlockEntity(pos);
            if (blockEntity == null) return InteractionResult.SUCCESS;
            if (!blockEntity.getPetTag().isEmpty()) {
                if (player.setEntityOnShoulder(blockEntity.getPetTag())) {
                    blockEntity.setPetTag(new CompoundTag());
                }
                return InteractionResult.SUCCESS;
            } else if (!player.getShoulderEntityLeft().isEmpty()) {
                blockEntity.setPetTag(player.getShoulderEntityLeft());
                player.setShoulderEntityLeft(new CompoundTag());
                return InteractionResult.SUCCESS;
            } else if (!player.getShoulderEntityRight().isEmpty()) {
                blockEntity.setPetTag(player.getShoulderEntityRight());
                player.setShoulderEntityRight(new CompoundTag());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(pContext)) {
            return this.defaultBlockState();
        } else {
            return null;
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            DoubleBlockHalf corner = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
            if(corner == DoubleBlockHalf.LOWER){
                BirdCageBlockEntity be = (BirdCageBlockEntity) level.getBlockEntity(pos);
                if(be != null){
                    be.respawnEntityOnShoulder();
                }

            }
            level.destroyBlock(corner == DoubleBlockHalf.LOWER ? pos.above() : pos.below(), false);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }



    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(BlockStateProperties.DOUBLE_BLOCK_HALF);
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), pState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return blockState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? null : new BirdCageBlockEntity(pos, blockState);
    }
}
