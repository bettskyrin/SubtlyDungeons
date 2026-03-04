package net.meander.subtlyd.mixin.common.entity;

import net.meander.subtlyd.util.data.tags.EntityTypeTagsSD;
import net.meander.subtlyd.world.entity.EntityTypeSD;
import net.meander.subtlyd.world.entity.ai.goal.SeekShelterGoal;
import net.meander.subtlyd.world.entity.ai.goal.SeekWarmthGoal;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {
    /**
     * Adds the goal of finding shelther from the rain and cold. Only warm and temperate animals seek warmth.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addEnvironmentGoals(EntityType<?> type, Level level, CallbackInfo ci) {
        if (((Object) this) instanceof PathfinderMob mob) {
            if (mob.is(EntityTypeTagsSD.CAN_SEEK_WARMTH)) {
                Identifier variant = EntityTypeSD.getTemperatureVariantType(mob);

              if (variant != TemperatureVariants.COLD) {
                  mob.goalSelector.addGoal(3, new SeekWarmthGoal(mob, 1.0D, 16));
              }
            }

            if (mob.is(EntityTypeTagsSD.SEEKS_SHELTER)) {
                mob.goalSelector.addGoal(2, new SeekShelterGoal(mob, 1.25D));
            }
        }
    }
}