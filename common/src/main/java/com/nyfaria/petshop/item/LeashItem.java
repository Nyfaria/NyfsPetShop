package com.nyfaria.petshop.item;

import com.nyfaria.petshop.entity.BasePet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class LeashItem extends Item {

    private static final String LEASHED_PET_TAG = "LeashedPetUUID";
    private final DyeColor color;

    public LeashItem(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            Optional<UUID> petUUID = getLeashedPetUUID(stack);
            if (petUUID.isPresent() && player.isShiftKeyDown()) {
                Entity entity = ((net.minecraft.server.level.ServerLevel) level).getEntity(petUUID.get());
                if (entity instanceof BasePet pet) {
                    pet.clearCustomLeash();
                }
                clearLeashedPet(stack);
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    public static void setLeashedPet(ItemStack stack, UUID petUUID) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID(LEASHED_PET_TAG, petUUID);
    }

    public static Optional<UUID> getLeashedPetUUID(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.hasUUID(LEASHED_PET_TAG)) {
            return Optional.of(tag.getUUID(LEASHED_PET_TAG));
        }
        return Optional.empty();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide && entity instanceof Player player) {
            getLeashedPetUUID(stack).ifPresent(petUUID -> {
                Entity petEntity = ((ServerLevel) level).getEntity(petUUID);
                if (!(petEntity instanceof BasePet pet) || !pet.getCustomLeashHolder().map(uuid -> uuid.equals(player.getUUID())).orElse(false)) {
                    clearLeashedPet(stack);
                }
            });
        }
    }

    public static void clearLeashedPet(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            tag.remove(LEASHED_PET_TAG);
        }
    }

    public static boolean hasLeashedPet(ItemStack stack) {
        return getLeashedPetUUID(stack).isPresent();
    }

    public DyeColor getColor() {
        return color;
    }

    public int getColorValue() {
        return color.getTextColor();
    }
}
