package com.nyfaria.nyfspetshop.item;

import com.nyfaria.nyfspetshop.init.EntityInit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class PetItem extends Item {

    public PetItem(Properties itemProperties) {
        super(itemProperties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND) {
            ItemStack itemstack = pPlayer.getMainHandItem();
            if (!pLevel.isClientSide() && itemstack.hasTag()) {
                CompoundTag tag = itemstack.getTag();
                if (tag.contains("owner_uuid")) {
                    if (pPlayer.getUUID().equals(itemstack.getTag().getUUID("owner_uuid"))) {
                        if (tag.contains("inside") && tag.getBoolean("inside")) {
                            tag.putBoolean("inside", false);
                            LivingEntity entity = (LivingEntity) BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(itemstack.getTag().getString("entityType"))).create(pLevel);
                            entity.load(tag.getCompound("petData"));
                            entity.setPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
                            pLevel.addFreshEntity(entity);
                        } else {
                            tag.putBoolean("inside", true);
                            LivingEntity pet = (LivingEntity) ((ServerLevel) pLevel).getEntity(tag.getUUID("pet_uuid"));
                            if(pet.hasCustomName()){
                                itemstack.setHoverName(pet.getCustomName());
                            }
                            CompoundTag petData = new CompoundTag();
                            pet.save(petData);
                            pet.discard();
                            tag.put("petData", petData);
                        }
                    } else {
                        return InteractionResultHolder.fail(itemstack);
                    }
                } else {
                    TamableAnimal pet = (TamableAnimal) BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(tag.getString("entityType"))).spawn((ServerLevel) pLevel, pPlayer.blockPosition(), MobSpawnType.MOB_SUMMONED);
                    tag.putUUID("owner_uuid", pPlayer.getUUID());
                    tag.putUUID("pet_uuid", pet.getUUID());
                    pet.tame(pPlayer);
                }
                itemstack.setTag(tag);
            }
            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionHand pUsedHand = context.getHand();
        Level pLevel = context.getLevel();
        Player pPlayer = context.getPlayer();
        Vec3 spawnPos = context.getClickedPos().relative(context.getClickedFace()).getCenter();
        if (pUsedHand == InteractionHand.MAIN_HAND) {
            ItemStack itemstack = pPlayer.getMainHandItem();
            if (!pLevel.isClientSide() && itemstack.hasTag()) {
                CompoundTag tag = itemstack.getTag();
                if (tag.contains("owner_uuid")) {
                    if (pPlayer.getUUID().equals(itemstack.getTag().getUUID("owner_uuid"))) {
                        if (tag.contains("inside") && tag.getBoolean("inside")) {
                            tag.putBoolean("inside", false);
                            LivingEntity entity = (LivingEntity) BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(itemstack.getTag().getString("entityType"))).create(pLevel);
                            entity.load(tag.getCompound("petData"));
                            entity.setPos(spawnPos.x(), spawnPos.y(), spawnPos.z());
                            pLevel.addFreshEntity(entity);
                        } else {
                            tag.putBoolean("inside", true);
                            LivingEntity pet = (LivingEntity) ((ServerLevel) pLevel).getEntity(tag.getUUID("pet_uuid"));
                            if(pet.hasCustomName()){
                                itemstack.setHoverName(pet.getCustomName());
                            }
                            pet.save(tag.getCompound("petData"));
                            pet.discard();
                        }
                    } else {
                        return InteractionResult.FAIL;
                    }
                } else {
                    TamableAnimal pet = (TamableAnimal) BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(tag.getString("entityType"))).spawn((ServerLevel) pLevel, BlockPos.containing(spawnPos), MobSpawnType.MOB_SUMMONED);
                    tag.putUUID("owner_uuid", pPlayer.getUUID());
                    tag.putUUID("pet_uuid", pet.getUUID());
                    tag.putString("inside", "false");
                    pet.tame(pPlayer);
                }
                itemstack.setTag(tag);
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        if (pStack.hasTag()) {
            if (pStack.getTag().contains("entityType")) {
                pTooltipComponents.add(Component.translatable(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(pStack.getTag().getString("entityType"))).getDescriptionId()).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
