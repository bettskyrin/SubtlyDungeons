package net.meander.subtlyd.mixin.common.world.entity.projectile.arrow;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;

@Mixin(Arrow.class)
public class ArrowMixin {
    @ModifyArg(method = "doPostHurtEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/PotionContents;forEachEffect(Ljava/util/function/Consumer;F)V"), index = 0)
    private Consumer<MobEffectInstance> scaleEffects(Consumer<MobEffectInstance> consumer, @Local(argsOnly = true, name = "mob") LivingEntity mob) {
        Arrow arrow = (Arrow) (Object) this;

        return effect -> {
            if (effect.getEffect().value().isInstantaneous()) {
                effect.getEffect().value().applyInstantaneousEffect(
                        (ServerLevel) arrow.level(),
                        arrow,
                        arrow.getOwner(),
                        mob,
                        effect.getAmplifier(),
                        0.125D
                );
            } else {
                consumer.accept(effect);
            }
        };
    }
}