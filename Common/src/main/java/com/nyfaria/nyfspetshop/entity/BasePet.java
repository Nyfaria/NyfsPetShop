package com.nyfaria.nyfspetshop.entity;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.entity.enums.MovementType;
import com.nyfaria.nyfspetshop.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class BasePet extends TamableAnimal implements SmartBrainOwner<BasePet>, GeoEntity {
    public static final EntityDataAccessor<MovementType> MOVEMENT_TYPE = SynchedEntityData.defineId(BasePet.class, Services.PLATFORM.getMovementTypeSerializer());

    protected final EntityType<? extends BasePet> type;

    protected BasePet(EntityType<? extends BasePet> type, Level $$1) {
        super(type, $$1);
        this.type = type;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOVEMENT_TYPE, MovementType.FOLLOW);
    }

    @Override
    public InteractionResult mobInteract(Player interactingPlayer, InteractionHand hand) {
        if(isOwnedBy(interactingPlayer)) {
            if (interactingPlayer.isCrouching() && interactingPlayer.getItemInHand(hand).isEmpty()) {
                if (!level().isClientSide) {
                    setMovementType(getMovementType().cycle());
                    interactingPlayer.displayClientMessage(Component.translatable("player_message." + Constants.MODID + ".movementType",getName(),getMovementType().getDisplayName()), true);
                }
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
        }
        return super.mobInteract(interactingPlayer, hand);
    }

    public MovementType getMovementType() {
        return this.entityData.get(MOVEMENT_TYPE);
    }
    public void setMovementType(MovementType movementType) {
        this.entityData.set(MOVEMENT_TYPE, movementType);
    }



    @Nullable
    public BasePet getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        BasePet baby = getType().create(serverLevel);
        if (baby != null) {
            UUID ownerUUID = this.getOwnerUUID();
            if (ownerUUID != null) {
                baby.setOwnerUUID(ownerUUID);
                baby.setTame(true);
            }
        }

        return baby;
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("movement_type", getMovementType().name);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setMovementType(MovementType.fromName(tag.getString("movement_type")));
    }
    @NotNull
    public EntityType<? extends BasePet> getType() {
        return type;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.3F).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }
    public abstract void performBowlAction(PetBowl.Type type);


}
