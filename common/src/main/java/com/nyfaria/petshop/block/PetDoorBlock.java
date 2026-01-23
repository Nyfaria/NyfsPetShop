package com.nyfaria.petshop.block;

import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

public class PetDoorBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    private static final VoxelShape NORTH_SHAPE = makeNorthShape();
    private static final VoxelShape SOUTH_SHAPE = makeSouthShape();
    private static final VoxelShape EAST_SHAPE = makeEastShape();
    private static final VoxelShape WEST_SHAPE = makeWestShape();

    public static VoxelShape makeNorthShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.0625, 1, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.9375, 0, 0, 1, 1, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.875, 0, 0.9375, 1, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.03125, 0.9375, 0.875, 0.09375), BooleanOp.OR);
        return shape;
    }

    public static VoxelShape makeSouthShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.9375, 0, 0.875, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 0.0625, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.875, 0.875, 0.9375, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.90625, 0.9375, 0.875, 0.96875), BooleanOp.OR);
        return shape;
    }

    public static VoxelShape makeEastShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0, 1, 1, 0.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.9375, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0.875, 0.0625, 1, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.90625, 0, 0.0625, 0.96875, 0.875, 0.9375), BooleanOp.OR);
        return shape;
    }

    public static VoxelShape makeWestShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.9375, 0.125, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.125, 1, 0.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.875, 0.0625, 0.125, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.03125, 0, 0.0625, 0.09375, 0.875, 0.9375), BooleanOp.OR);
        return shape;
    }
    public PetDoorBlock(Properties $$0) {
        super($$0);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            level.setBlock(pos, state.cycle(OPEN), 3);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(OPEN) ? Shapes.empty() : getShape(state, level, pos, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(FACING, OPEN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        return this.defaultBlockState().setValue(FACING, $$0.getHorizontalDirection().getOpposite());
    }
}