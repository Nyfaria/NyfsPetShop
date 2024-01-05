package com.nyfaria.nyfspetshop.entity;

import com.nyfaria.nyfspetshop.CommonClass;
import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.entity.enums.MovementType;
import com.nyfaria.nyfspetshop.init.MemoryModuleTypeInit;
import com.nyfaria.nyfspetshop.platform.Services;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public abstract class BasePet extends TamableAnimal implements SmartBrainOwner<BasePet>, GeoEntity {
    public static final EntityDataAccessor<MovementType> MOVEMENT_TYPE = SynchedEntityData.defineId(BasePet.class, CommonClass.MOVEMENT_TYPE);
    public static final EntityDataAccessor<Boolean> HAS_HAT = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> HAS_COLLAR = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> HAS_BOOTS = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Vector3f> HAT_COLOR = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.VECTOR3);
    public static final EntityDataAccessor<Vector3f> COLLAR_COLOR = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.VECTOR3);
    public static final EntityDataAccessor<Vector3f> BOOTS_COLOR = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.VECTOR3);
    public static final EntityDataAccessor<Boolean> BEGGING = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.BOOLEAN);
    public Optional<ItemStack> itemStack = Optional.of(ItemStack.EMPTY);

    protected final EntityType<? extends BasePet> type;

    protected BasePet(EntityType<? extends BasePet> type, Level $$1) {
        super(type, $$1);
        this.type = type;
    }

    public ItemStack getPetItemStack() {
        return itemStack.get();
    }

    public void setPetItemStack(ItemStack petItemStack) {
        itemStack = Optional.of(petItemStack);
    }

    public String getPublicEncodeId() {
        EntityType<?> $$0 = this.getType();
        ResourceLocation $$1 = EntityType.getKey($$0);
        return $$0.canSerialize() && $$1 != null ? $$1.toString() : null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOVEMENT_TYPE, MovementType.FOLLOW);
        this.entityData.define(HAS_HAT, false);
        this.entityData.define(HAS_COLLAR, false);
        this.entityData.define(HAS_BOOTS, false);
        this.entityData.define(HAT_COLOR, new Vector3f(1, 1, 1));
        this.entityData.define(COLLAR_COLOR, new Vector3f(1, 1, 1));
        this.entityData.define(BOOTS_COLOR, new Vector3f(1, 1, 1));
        this.entityData.define(BEGGING, false);
    }

    @Override
    public InteractionResult mobInteract(Player interactingPlayer, InteractionHand hand) {

        if (isOwnedBy(interactingPlayer) && interactingPlayer.isCrouching() && interactingPlayer.getItemInHand(hand).isEmpty()) {
            if (!level().isClientSide) {
                setMovementType(getMovementType().cycle());
                interactingPlayer.displayClientMessage(Component.translatable("player_message." + Constants.MODID + ".movementType", getName(), getMovementType().getDisplayName()), true);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        } else if (!level().isClientSide && interactingPlayer.getItemInHand(hand).getItem() instanceof DyeItem dyeItem) {
            setHatColor(new Vector3f(dyeItem.getDyeColor().getTextureDiffuseColors()));

        } else if(isTreat(interactingPlayer.getMainHandItem())){
            if(!level().isClientSide){
                doTreatStuff(interactingPlayer,hand);
            } else {
                level().addParticle(ParticleTypes.HEART, getX(), getY() + getBbHeight(), getZ(), 0, 0.5f, 0);
                level().addParticle(ParticleTypes.HEART, getX(), getY() + getBbHeight() - 0.2, getZ(), 0, 0.5f, 0);

            }
        }else {
            if (interactingPlayer.getItemInHand(hand).isEmpty()) {
                if (!interactingPlayer.level().isClientSide) {
                    doPetStuff(interactingPlayer,hand);
                } else {
                    level().addParticle(ParticleTypes.HEART, getX(), getY() + getBbHeight(), getZ(), 0, 0.5f, 0);
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(interactingPlayer, hand);
    }

    public void doPetStuff(Player player, InteractionHand hand) {

    }

    public void doTreatStuff(Player player, InteractionHand hand) {

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
        tag.putBoolean("has_hat", hasHat());
        tag.putBoolean("has_collar", hasCollar());
        tag.putBoolean("has_boots", hasBoots());
        CompoundTag hatColor = new CompoundTag();
        hatColor.putFloat("r", getHatColor().x());
        hatColor.putFloat("g", getHatColor().y());
        hatColor.putFloat("b", getHatColor().z());
        tag.put("hat_color", hatColor);
        CompoundTag collarColor = new CompoundTag();
        collarColor.putFloat("r", getCollarColor().x());
        collarColor.putFloat("g", getCollarColor().y());
        collarColor.putFloat("b", getCollarColor().z());
        tag.put("collar_color", collarColor);
        CompoundTag bootsColor = new CompoundTag();
        bootsColor.putFloat("r", getBootsColor().x());
        bootsColor.putFloat("g", getBootsColor().y());
        bootsColor.putFloat("b", getBootsColor().z());
        tag.put("boots_color", bootsColor);
        if (!getPetItemStack().isEmpty()) {
            CompoundTag petItemStack = new CompoundTag();
            getPetItemStack().save(petItemStack);
            tag.put("pet_item_stack", petItemStack);
        }
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {

        return super.getPickResult();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setMovementType(MovementType.fromName(tag.getString("movement_type")));
        setHasHat(tag.getBoolean("has_hat"));
        setHasCollar(tag.getBoolean("has_collar"));
        setHasBoots(tag.getBoolean("has_boots"));
        CompoundTag hatColor = tag.getCompound("hat_color");
        setHatColor(hatColor.getFloat("r"), hatColor.getFloat("g"), hatColor.getFloat("b"));
        CompoundTag collarColor = tag.getCompound("collar_color");
        setCollarColor(collarColor.getFloat("r"), collarColor.getFloat("g"), collarColor.getFloat("b"));
        CompoundTag bootsColor = tag.getCompound("boots_color");
        setBootsColor(bootsColor.getFloat("r"), bootsColor.getFloat("g"), bootsColor.getFloat("b"));
        if (tag.contains("pet_item_stack")) {
            setPetItemStack(ItemStack.of(tag.getCompound("pet_item_stack")));
        }

    }

    @NotNull
    public EntityType<? extends BasePet> getType() {
        return type;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    public abstract void performBowlAction(PetBowl.Type type);

    public void setHasHat(boolean hasHat) {
        this.entityData.set(HAS_HAT, hasHat);
    }

    public boolean hasHat() {
        return this.entityData.get(HAS_HAT);
    }

    public void setHasCollar(boolean hasCollar) {
        this.entityData.set(HAS_COLLAR, hasCollar);
    }

    public boolean hasCollar() {
        return this.entityData.get(HAS_COLLAR);
    }

    public void setHasBoots(boolean hasBoots) {
        this.entityData.set(HAS_BOOTS, hasBoots);
    }

    public boolean hasBoots() {
        return this.entityData.get(HAS_BOOTS);
    }

    public void setHatColor(Vector3f color) {
        this.entityData.set(HAT_COLOR, color);
    }

    public Vector3f getHatColor() {
        return this.entityData.get(HAT_COLOR);
    }

    public void setCollarColor(Vector3f color) {
        this.entityData.set(COLLAR_COLOR, color);
    }

    public Vector3f getCollarColor() {
        return this.entityData.get(COLLAR_COLOR);
    }

    public void setBootsColor(Vector3f color) {
        this.entityData.set(BOOTS_COLOR, color);
    }

    public Vector3f getBootsColor() {
        return this.entityData.get(BOOTS_COLOR);
    }

    public void setHatColor(int r, int g, int b) {
        this.entityData.set(HAT_COLOR, new Vector3f(r, g, b));
    }

    public void setCollarColor(int r, int g, int b) {
        this.entityData.set(COLLAR_COLOR, new Vector3f(r, g, b));
    }

    public void setBootsColor(int r, int g, int b) {
        this.entityData.set(BOOTS_COLOR, new Vector3f(r, g, b));
    }

    public void setHatColor(float r, float g, float b) {
        this.entityData.set(HAT_COLOR, new Vector3f(r, g, b));
    }

    public void setCollarColor(float r, float g, float b) {
        this.entityData.set(COLLAR_COLOR, new Vector3f(r, g, b));
    }

    public void setBootsColor(float r, float g, float b) {
        this.entityData.set(BOOTS_COLOR, new Vector3f(r, g, b));
    }

    public void setBegging(boolean begging) {
        this.entityData.set(BEGGING, begging);
    }

    public boolean isBegging() {
        return this.entityData.get(BEGGING);
    }

    public boolean canDoStuff() {
        return !isBegging() && getMovementType() != MovementType.STAY;
    }
    public boolean isTreat(ItemStack stack) {
        return false;
    }
    public boolean isPetSleeping() {
        return BrainUtils.hasMemory(this, MemoryModuleTypeInit.SLEEPING.get());
    }
}
