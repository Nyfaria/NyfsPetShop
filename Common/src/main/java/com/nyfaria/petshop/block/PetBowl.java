package com.nyfaria.petshop.block;

import com.nyfaria.petshop.init.BlockStateInit;
import com.nyfaria.petshop.item.KibbleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PetBowl extends Block {
    private static VoxelShape SHAPE = makeShape();
    private final DyeColor color;


    public PetBowl(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateInit.BOWL_TYPE, Type.EMPTY).setValue(BlockStateInit.FULLNESSITY, 0));
    }

    public static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.125, 0.875, 0.125, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0.125, 0.125, 0.875, 0.3125, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.125, 0.125, 0.25, 0.3125, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.125, 0.75, 0.75, 0.3125, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.125, 0.125, 0.75, 0.3125, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.234375, 0.25, 0.75, 0.234375, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.25, 0.25, 0.75, 0.25, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.296875, 0.25, 0.75, 0.296875, 0.75), BooleanOp.OR);

        return shape;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getItemInHand(interactionHand).getItem() == Items.WATER_BUCKET) {
            if (state.getValue(BlockStateInit.BOWL_TYPE) != Type.KIBBLE) {
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlockAndUpdate(pos, state.setValue(BlockStateInit.BOWL_TYPE, Type.WATER).setValue(BlockStateInit.FULLNESSITY, 3));
                if (!player.isCreative()) {
                    player.setItemInHand(interactionHand, new ItemStack(Items.BUCKET));
                }
            }
        } else if (player.getItemInHand(interactionHand).getItem() instanceof KibbleItem) {
            if (state.getValue(BlockStateInit.BOWL_TYPE) != Type.WATER) {
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 0.5F);
                level.setBlockAndUpdate(pos, state.setValue(BlockStateInit.BOWL_TYPE, Type.KIBBLE).setValue(BlockStateInit.FULLNESSITY, 3));
                if (!player.isCreative()) {
                    KibbleItem.doKibbleAction(player.getItemInHand(interactionHand), player);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
        definition.add(BlockStateInit.BOWL_TYPE, BlockStateInit.FULLNESSITY);
    }

    public enum Type implements StringRepresentable {
        KIBBLE("kibble", SoundEvents.CAT_EAT),
        WATER("water", SoundEvents.GENERIC_DRINK),
        EMPTY("empty", null);

        private final String name;
        private final SoundEvent sound;

        Type(String name, SoundEvent sound) {
            this.name = name;
            this.sound = sound;
        }

        public SoundEvent getSound() {
            return sound;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
