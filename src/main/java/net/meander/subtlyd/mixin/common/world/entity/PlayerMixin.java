package net.meander.subtlyd.mixin.common.world.entity;

import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        tickTentSleep();
    }

    /**
     * @param itemStack The item being enchanted.
     * @param enchantmentCost The experience cost.
     */
    @Inject(method = "onEnchantmentPerformed", at = @At("TAIL"))
    private void onEnchantmentPerformed(ItemStack itemStack, int enchantmentCost, CallbackInfo ci) {
        int magicLevel = itemStack.getOrDefault(DataComponentsSD.MAGIC_LEVEL, 0) + Mth.ceil(enchantmentCost * 1.5);

        itemStack.set(DataComponentsSD.MAGIC_LEVEL, magicLevel);
    }

    /**
     * Wakes up the player once it's daytime
     */
    private void tickTentSleep() {
        final Player player = (Player) (Object) this;

        if (player.level() instanceof ServerLevel && player.level().isBrightOutside()) {
            if (TentEntity.getTent(player, true) != null) {
                player.stopSleepInBed(false, true);
            }
        }
    }

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void interruptMeal(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;

        if (player.isUsingItem()) {
            if (source.getEntity() instanceof LivingEntity) {
                ItemStack activeItem = player.getUseItem();
                if (activeItem.has(DataComponents.FOOD) || activeItem.has(DataComponents.CONSUMABLE)) {
                    player.stopUsingItem();
                }
            }
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    private void disableItemCooldown(Player instance) {}

    @Redirect(method = "canCriticalAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z"))
    private boolean modifyCrits(Player player) {
        return false;
    }
}
