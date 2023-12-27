package com.nyfaria.nyfspetshop.item;

import com.nyfaria.nyfspetshop.entity.BasePet;
import com.nyfaria.nyfspetshop.init.CosmeticRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class PetItem extends Item {
    public static Map<Item, DyeColor> colorMap = Map.ofEntries(
            Map.entry(Items.WHITE_WOOL, DyeColor.WHITE),
            Map.entry(Items.ORANGE_WOOL, DyeColor.ORANGE),
            Map.entry(Items.MAGENTA_WOOL, DyeColor.MAGENTA),
            Map.entry(Items.LIGHT_BLUE_WOOL, DyeColor.LIGHT_BLUE),
            Map.entry(Items.YELLOW_WOOL, DyeColor.YELLOW),
            Map.entry(Items.LIME_WOOL, DyeColor.LIME),
            Map.entry(Items.PINK_WOOL, DyeColor.PINK),
            Map.entry(Items.GRAY_WOOL, DyeColor.GRAY),
            Map.entry(Items.LIGHT_GRAY_WOOL, DyeColor.LIGHT_GRAY),
            Map.entry(Items.CYAN_WOOL, DyeColor.CYAN),
            Map.entry(Items.PURPLE_WOOL, DyeColor.PURPLE),
            Map.entry(Items.BLUE_WOOL, DyeColor.BLUE),
            Map.entry(Items.BROWN_WOOL, DyeColor.BROWN),
            Map.entry(Items.GREEN_WOOL, DyeColor.GREEN),
            Map.entry(Items.RED_WOOL, DyeColor.RED),
            Map.entry(Items.BLACK_WOOL, DyeColor.BLACK)
    );

    public PetItem(Properties itemProperties) {
        super(itemProperties);
    }

    public static ItemStack setCosmetic(ItemStack stack, CosmeticRegistry.Type type, ItemStack woolStack, Level pLevel) {
        BasePet pet = getEntity(stack, pLevel);
        pet.load(stack.getTag().getCompound("petData"));
        DyeColor color = DyeColor.WHITE;
        Vector3f colorVector = new Vector3f(1, 1, 1);
        if (colorMap.containsKey(woolStack.getItem())) {
            color = colorMap.get(woolStack.getItem());
        } else if (woolStack.getItem() instanceof DyeItem dyeItem) {
            color = dyeItem.getDyeColor();
        }
        colorVector = new Vector3f(color.getTextureDiffuseColors());
        switch (type) {
            case HAT:
                if (pet.hasHat()) {
                    if (woolStack.is(Items.SHEARS)) {
                        pet.setHasHat(false);
                        pet.setHatColor(new Vector3f(1, 1, 1));
                        break;
                    }
                    if(woolStack.getItem() instanceof DyeItem){
                        pet.setHatColor(colorVector);
                        break;
                    }
                    return ItemStack.EMPTY;
                } else if (woolStack.is(ItemTags.WOOL)) {
                    pet.setHasHat(true);
                    pet.setHatColor(new Vector3f(colorVector));
                } else {
                    return ItemStack.EMPTY;
                }
                break;
            case COLLAR:
                if (pet.hasCollar()) {
                    if (woolStack.is(Items.SHEARS)) {
                        pet.setHasCollar(false);
                        pet.setCollarColor(new Vector3f(1, 1, 1));
                        break;
                    }
                    if (woolStack.getItem() instanceof DyeItem) {
                        pet.setCollarColor(colorVector);
                        break;
                    }
                    return ItemStack.EMPTY;
                } else if (woolStack.is(ItemTags.WOOL)) {
                    pet.setHasCollar(true);
                    pet.setCollarColor(new Vector3f(colorVector));
                } else {
                    return ItemStack.EMPTY;
                }
                break;
            case BOOTS:
                if (pet.hasBoots()) {
                    if (woolStack.is(Items.SHEARS)) {
                        pet.setHasBoots(false);
                        pet.setBootsColor(new Vector3f(1, 1, 1));
                        break;
                    }
                    if (woolStack.getItem() instanceof DyeItem) {
                        pet.setBootsColor(colorVector);
                        break;
                    }
                    return ItemStack.EMPTY;
                } else if (woolStack.is(ItemTags.WOOL)) {
                    pet.setHasBoots(true);
                    pet.setBootsColor(new Vector3f(colorVector));
                } else {
                    return ItemStack.EMPTY;
                }
                break;
        }

        CompoundTag tag = stack.getTag();
        CompoundTag petData = new CompoundTag();
        pet.save(petData);
        tag.put("petData", petData);
        stack.setTag(tag);
        return stack;
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
                            LivingEntity entity = getEntity(itemstack, pLevel);
                            entity.load(tag.getCompound("petData"));
                            entity.setPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
                            pLevel.addFreshEntity(entity);
                        } else {
                            tag.putBoolean("inside", true);
                            LivingEntity pet = (LivingEntity) ((ServerLevel) pLevel).getEntity(tag.getUUID("pet_uuid"));
                            if (pet.hasCustomName()) {
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
                    BasePet pet = (BasePet) BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(tag.getString("entityType"))).spawn((ServerLevel) pLevel, pPlayer.blockPosition(), MobSpawnType.MOB_SUMMONED);
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
                            if (pet.hasCustomName()) {
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

    public static <T extends BasePet> T getEntity(ItemStack stack, Level pLevel) {
        return (T) BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(stack.getTag().getString("entityType"))).create(pLevel);
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
