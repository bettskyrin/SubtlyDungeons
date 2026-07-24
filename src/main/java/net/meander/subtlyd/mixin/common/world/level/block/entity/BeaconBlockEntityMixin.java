package net.meander.subtlyd.mixin.common.world.level.block.entity;

import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @ModifyVariable(method = "applyEffects", at = @At(value = "STORE", ordinal = 0), name = "range")
    private static double newBeaconRange(double range) {
        return range * 4.0;
    }
}
