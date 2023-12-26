package com.nyfaria.nyfspetshop.block;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.block.menu.groomingstation.GroomingStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class GroomingStation extends Block {
    private static final Component CONTAINER_TITLE = Component.translatable("container."+ Constants.MODID+".grooming");
    public GroomingStation(Properties properties) {
        super(properties);

    }
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
            return InteractionResult.CONSUME;
        }
    }
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) -> {
            return new GroomingStationMenu(p_52229_, p_52230_, ContainerLevelAccess.create(pLevel, pPos));
        }, CONTAINER_TITLE);
    }
}
