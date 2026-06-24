package net.meander.subtlyd.mixin.common.world.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.meander.subtlyd.world.item.enchantment.EnchantmentHelperSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
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
    private boolean wasCrouching = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        tickTentSleep();
        activateShieldFromCrouch();
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

    private void activateShieldFromCrouch() {
        if (OptionsSD.SHIELD_CROUCH.get()) {
            Player player = (Player) (Object) this;
            boolean isCrouching = player.isCrouching();

            if (isCrouching != wasCrouching) {
                if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ShieldItem) {
                    if (isCrouching && !player.isUsingItem()) {
                        player.startUsingItem(InteractionHand.OFF_HAND);
                    } else if (player.isUsingItem() && player.getUseItem().getItem() instanceof ShieldItem) {
                        player.stopUsingItem();
                    }
                }
                wasCrouching = isCrouching;
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

    @Redirect(method = "isSweepAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean getItemInHand(ItemStack instance, TagKey<Item> tagKey) {
        return instance.is(ItemTagsSD.SWEEPING_WEAPON) && EnchantmentHelperSD.checkEnchantment(instance, Enchantments.SWEEPING_EDGE);
    }
}
