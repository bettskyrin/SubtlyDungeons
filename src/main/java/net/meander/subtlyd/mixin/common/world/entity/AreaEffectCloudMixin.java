package net.meander.subtlyd.mixin.common.world.entity;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaEffectCloud.class)
public class AreaEffectCloudMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void playSounds(CallbackInfo ci) {
        AreaEffectCloud cloud = (AreaEffectCloud) (Object) this;

        if (!cloud.level().isClientSide()) {
            ParticleType<?> particle = cloud.getParticle().getType();

            if (particle == ParticleTypes.DRAGON_BREATH) {
                if (cloud.tickCount == 1) {
                    cloud.playSound(SoundEventsSD.ENDER_DRAGON_BREATH, 1.0F, 1.0F);
                }
            } else if (particle == ParticleTypes.ENTITY_EFFECT) {
                if (cloud.tickCount == 1) {
                    cloud.playSound(SoundEventsSD.AREA_EFFECT_CLOUD_GAS, 0.5F, 1.0F);
                }
            }
        }
    }
}
