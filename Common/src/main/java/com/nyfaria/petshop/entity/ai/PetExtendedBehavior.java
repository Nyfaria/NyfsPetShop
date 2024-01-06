package com.nyfaria.petshop.entity.ai;

import com.nyfaria.petshop.entity.BasePet;
import com.nyfaria.petshop.entity.enums.MovementType;
import net.minecraft.server.level.ServerLevel;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.function.BiPredicate;

public abstract class PetExtendedBehavior<E extends BasePet> extends ExtendedBehaviour<E> {

    public BiPredicate<E, MovementType> movementTypePredicate = (pet, movementType) -> true;

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return movementTypePredicate.test(entity, entity.getMovementType());
    }

    public PetExtendedBehavior<E> movementTypePredicate(BiPredicate<E, MovementType> movementTypePredicate) {
        this.movementTypePredicate = movementTypePredicate;
        return this;
    }
}
