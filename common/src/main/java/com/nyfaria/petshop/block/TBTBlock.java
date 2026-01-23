package com.nyfaria.petshop.block;

import com.nyfaria.petshop.init.BlockStateInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TBTBlock extends Block {
    private static VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);


    public TBTBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateInit.CORNER, Corner.FRONT_LEFT));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return switch (context.getHorizontalDirection()) {
            case NORTH -> this.defaultBlockState().setValue(BlockStateInit.CORNER, Corner.FRONT_LEFT);
            case SOUTH -> this.defaultBlockState().setValue(BlockStateInit.CORNER, Corner.BACK_RIGHT);
            case EAST -> this.defaultBlockState().setValue(BlockStateInit.CORNER, Corner.BACK_LEFT);
            case WEST -> this.defaultBlockState().setValue(BlockStateInit.CORNER, Corner.FRONT_RIGHT);
            default -> super.getStateForPlacement(context);
        };
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Corner corner = state.getValue(BlockStateInit.CORNER);
            level.destroyBlock(pos.offset(corner.getClockWiseOffset()), false);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPE;
//        return switch(state.getValue(BlockStateInit.CORNER)){
//            case FRONT_LEFT -> Shapes.block();
//            case FRONT_RIGHT -> Shapes.block();
//            case BACK_LEFT -> Shapes.block();
//            case BACK_RIGHT -> Shapes.block();
//            default -> Shapes.block();
//        };
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
        definition.add(BlockStateInit.CORNER);
    }


    public enum Corner implements StringRepresentable {
        BACK_LEFT("bl"),
        BACK_RIGHT("br"),
        FRONT_LEFT("fl"),
        FRONT_RIGHT("fr");


        private final String name;

        Corner(String name) {
            this.name = name;
        }

        public Corner getClockWise() {
            return switch (this) {
                case BACK_LEFT -> BACK_RIGHT;
                case BACK_RIGHT -> FRONT_RIGHT;
                case FRONT_RIGHT -> FRONT_LEFT;
                case FRONT_LEFT -> BACK_LEFT;
            };
        }

        public BlockPos getClockWiseOffset() {
            return switch (this) {
                case BACK_LEFT -> new BlockPos(1, 0, 0);
                case BACK_RIGHT -> new BlockPos(0, 0, 1);
                case FRONT_RIGHT -> new BlockPos(-1, 0, 0);
                case FRONT_LEFT -> new BlockPos(0, 0, -1);
            };
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
