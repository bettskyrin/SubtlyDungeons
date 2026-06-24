package net.meander.subtlyd.mixin.common.world.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.meander.subtlyd.world.item.enchantment.EnchantmentHelperSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
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

    @ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
    private float slowPowderSnowMining(float destroySpeed, BlockState state) {
        Player player = (Player) (Object) this;

        if (player.isInPowderSnow) {
            return destroySpeed / 5.0F;
        }

        return destroySpeed;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    private void disableItemCooldown(Player instance) {}

    @Redirect(method = "canCriticalAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z"))
    private boolean modifyCrits(Player player) {
        return false;
    }

    @Inject(method = "isSweepAttack", at = @At("RETURN"), cancellable = true)
    private void allowSweepAttacks(boolean fullStrengthAttack, boolean criticalAttack, boolean knockbackAttack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            Player player = (Player) (Object) this;
            boolean canSweep = fullStrengthAttack && !criticalAttack && !knockbackAttack && player.onGround() && player.getDeltaMovement().horizontalDistanceSqr() < 0.1D;

            if (canSweep) {
                ItemStack weapon = player.getMainHandItem();

                if (weapon.is(ItemTags.AXES) || weapon.is(Items.MACE)) {
                    if (EnchantmentHelperSD.checkEnchantment(weapon, Enchantments.SWEEPING_EDGE)) {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}
