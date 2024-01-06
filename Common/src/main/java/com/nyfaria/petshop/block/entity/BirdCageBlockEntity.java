package com.nyfaria.petshop.block.entity;

import com.nyfaria.petshop.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BirdCageBlockEntity extends BlockEntity {

    private CompoundTag petTag = new CompoundTag();

    public BirdCageBlockEntity(BlockPos $$1, BlockState $$2) {
        super(BlockInit.BIRD_CAGE_BE.get(), $$1, $$2);
    }

    public CompoundTag getPetTag() {
        return petTag;
    }

    public void setPetTag(CompoundTag petTag) {
        this.petTag = petTag;
        updateBlock();
    }

    public void respawnEntityOnShoulder() {
        if (!this.level.isClientSide && !petTag.isEmpty()) {
            EntityType.create(petTag, this.level).ifPresent((p_289491_) -> {

                p_289491_.setPos(this.getBlockPos().getCenter().x(), this.getBlockPos().getCenter().y(), this.getBlockPos().getCenter().z());
                ((ServerLevel) this.level).addWithUUID(p_289491_);
            });
        }

    }

    @Override
    public void load(CompoundTag tag) {
        loadData(tag);
        super.load(tag);
    }

    public void saveData(CompoundTag pTag) {
        pTag.put("petTag", petTag);
    }


    public void loadData(CompoundTag pTag) {
        petTag = pTag.getCompound("petTag");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        saveData(pTag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveData(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void updateBlock() {
        BlockState blockState = level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, 3);
        this.setChanged();
    }
}
