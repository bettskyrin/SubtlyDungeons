package com.kr1s1s.subtlyd.mixin.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Raider.class)
@SuppressWarnings("unused")
public class RaiderMixin {
    Raider raider = (Raider) (Object) this;
    Level level = raider.level();

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void finalizeSpawn(CallbackInfoReturnable<Object> cir){
        ItemStack mainHandItem = raider.getItemBySlot(EquipmentSlot.MAINHAND);

        if (raider.hasActiveRaid()) {
            if ((level.getDifficulty().equals(Difficulty.HARD) && raider.getCurrentRaid().getRaidOmenLevel() >= 4) || (level.getDifficulty().equals(Difficulty.NORMAL) && raider.getCurrentRaid().getRaidOmenLevel() == 5)) {
                if (mainHandItem.isEnchanted() && mainHandItem.is(Items.CROSSBOW)) {
                    if (raider.getRandom().nextFloat() < 0.25F  * raider.getCurrentRaid().getEnchantOdds()) {
                        EnchantmentHelper.setEnchantments(mainHandItem, ItemEnchantments.EMPTY);
                        mainHandItem.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).wrapAsHolder(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOptional(Enchantments.FLAME).get()), 1);
                    }
                }
            }
            setBoost();
        }
    }

    @Inject(method = "pickUpItem", at = @At("RETURN"))
    private void setBoost(ServerLevel serverLevel, ItemEntity itemEntity, CallbackInfo ci) {
        setBoost();
    }

    private void setBoost() {
        if (raider.isCaptain()) {
            raider.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 3600, getCaptainBonus()));
        }
    }

    private int getCaptainBonus() {
        switch (level.getDifficulty()) {
            case HARD:
                return 1;
            default:
                return 0;
        }
    }
}
