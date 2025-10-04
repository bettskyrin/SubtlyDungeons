package com.kr1s1s.subtlyd.mixin.util;

import com.kr1s1s.subtlyd.client.util.GroundShake;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/util/random/WeightedList;Lnet/minecraft/core/Holder;)V", at = @At("RETURN"))
    private void groundShakeExplosion(
            @Nullable Entity entity,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionDamageCalculator explosionDamageCalculator,
            double x,
            double y,
            double z,
            float power,
            boolean bl, Level.ExplosionInteraction explosionInteraction,
            ParticleOptions particleOptions,
            ParticleOptions particleOptions2,
            WeightedList<ExplosionParticleInfo> weightedList,
            Holder<SoundEvent> holder,
            CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            float maxDistance = 16 * (float) (power / 3);
            float distance = (float) Math.sqrt(player.distanceToSqr(x, y, z));
            GroundShake.shakeByDistance(15, maxDistance, distance, power / 3);
        }
    }
}
