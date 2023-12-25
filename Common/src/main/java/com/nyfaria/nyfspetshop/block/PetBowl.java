package com.nyfaria.nyfspetshop.block;

import com.nyfaria.nyfspetshop.init.BlockStateInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class PetBowl extends Block {

    public PetBowl(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateInit.BOWL_TYPE, Type.EMPTY));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (state.getValue(BlockStateInit.BOWL_TYPE) == Type.EMPTY) {
            if (player.getItemInHand(interactionHand).getItem() == Items.WATER_BUCKET) {
                level.setBlockAndUpdate(pos, state.setValue(BlockStateInit.BOWL_TYPE, Type.WATER));
                if (!player.isCreative()) {
                    player.setItemInHand(interactionHand,new ItemStack(Items.BUCKET));
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
        definition.add(BlockStateInit.BOWL_TYPE);
    }

    public enum Type implements StringRepresentable {
        FOOD("food"),
        WATER("water"),
        EMPTY("empty");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
