package com.nyfaria.petshop.init;

import com.nyfaria.petshop.block.TBTBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;

public class TBTHorizontalBlockItem extends BlockItem {

    public TBTHorizontalBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState pState, Property<T> pProperty, String pValueIdentifier) {
        return pProperty.getValue(pValueIdentifier).map((p_40592_) -> {
            return pState.setValue(pProperty, p_40592_);
        }).orElse(pState);
    }

    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState, BlockPos pos) {
        return pContext.getLevel().setBlock(pos, pState, 11);
    }

    public InteractionResult place(BlockPlaceContext pContext) {
        if (!pContext.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(pContext);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) return InteractionResult.FAIL;
                TBTBlock.Corner corner = blockstate.getValue(BlockStateInit.CORNER);
                BlockState blockstate2 = blockstate.setValue(BlockStateInit.CORNER, corner.getClockWise());
                BlockState blockstate3 = blockstate.setValue(BlockStateInit.CORNER, corner.getClockWise().getClockWise());
                BlockState blockstate4 = blockstate.setValue(BlockStateInit.CORNER, corner.getClockWise().getClockWise().getClockWise());
                BlockPos pos1 = blockplacecontext.getClickedPos();
                BlockPos pos2 = pos1.offset(corner.getClockWiseOffset());
                BlockPos pos3 = pos2.offset(corner.getClockWise().getClockWiseOffset());
                BlockPos pos4 = pos3.offset(corner.getClockWise().getClockWise().getClockWiseOffset());
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else if (!pContext.getLevel().getBlockState(pos1).canBeReplaced()
                        || !pContext.getLevel().getBlockState(pos2).canBeReplaced()
                        || !pContext.getLevel().getBlockState(pos3).canBeReplaced()
                        || !pContext.getLevel().getBlockState(pos4).canBeReplaced()) {
                    return InteractionResult.FAIL;
                } else {
                    this.placeBlock(blockplacecontext, blockstate, pos1);
                    this.placeBlock(blockplacecontext, blockstate2, pos2);
                    this.placeBlock(blockplacecontext, blockstate3, pos3);
                    this.placeBlock(blockplacecontext, blockstate4, pos4);
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
                        }
                    }

                    level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                    SoundType soundtype = blockstate1.getSoundType();
                    level.playSound(player, blockpos, this.getPlaceSound(blockstate1), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (player == null || !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

    private BlockState updateBlockStateFromTag(BlockPos pPos, Level pLevel, ItemStack pStack, BlockState pState) {
        BlockState blockstate = pState;
        CompoundTag compoundtag = pStack.getTag();
        if (compoundtag != null) {
            CompoundTag compoundtag1 = compoundtag.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> statedefinition = pState.getBlock().getStateDefinition();

            for (String s : compoundtag1.getAllKeys()) {
                Property<?> property = statedefinition.getProperty(s);
                if (property != null) {
                    String s1 = compoundtag1.get(s).getAsString();
                    blockstate = updateState(blockstate, property, s1);
                }
            }
        }

        if (blockstate != pState) {
            pLevel.setBlock(pPos, blockstate, 2);
        }

        return blockstate;
    }

}
