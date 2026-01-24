package com.meander.subtlyd.mixin.common.entity.monster;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@SuppressWarnings("DataFlowIssue")
@Mixin(Raider.class)
public class RaiderMixin {
    private final Raider raider = (Raider) (Object) this;
    private final Level level = raider.level();

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void finalizeSpawn(CallbackInfoReturnable<Object> cir){
        allowFlameCrossbows();
    }

    /**
     * Determines if the raid difficulty is high enough to allow pillagers with flame enchanted crossbows to spawn in a raid.
     * The raid difficulty must reach level 10 before this can happen.
     * i.e. Normal Difficulty with a raid omen of at least V, on wave 3 or higher OR Hard Difficulty with a raid omen of at least 4, on wave 3 or higher
     */
    private void allowFlameCrossbows(){
        ItemStack mainHandItem = raider.getItemBySlot(EquipmentSlot.MAINHAND);
        final int DIFFICULTY_THRESHOLD = 10;
        final int WAVE_THRESHOLD = 3;

        if (getRaidDifficulty() >= DIFFICULTY_THRESHOLD) {
            float arsonThreshold = 0.0625F * (getRaidDifficulty() - WAVE_THRESHOLD) * raider.getCurrentRaid().getEnchantOdds();
            if (mainHandItem.isEnchanted() && mainHandItem.is(Items.CROSSBOW)) {
                Optional<Enchantment> flameEnchantment = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOptional(Enchantments.FLAME);

                if (raider.getRandom().nextFloat() < arsonThreshold && flameEnchantment.isPresent()) {
                    EnchantmentHelper.setEnchantments(mainHandItem, ItemEnchantments.EMPTY);
                    mainHandItem.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).wrapAsHolder(flameEnchantment.get()), 1);
                }
            }
            setBoost();
        }
    }

    @Inject(method = "pickUpItem", at = @At("RETURN"))
    private void pickUpItem(ServerLevel serverLevel, ItemEntity itemEntity, CallbackInfo ci) { setBoost(); }

    /**
     * Gives pillager captains a 3-minute resistance boost based on raid difficulty level.
     */
    private void setBoost() {
        if (raider.isCaptain() && getRaidDifficulty() >= 4) {
            raider.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 3600, getCaptainBonus()));
        }
    }

    /**
     * Determines the pillager captain resistance boost level.
     * @return The level of resistance to grant
     */
    private int getCaptainBonus() {
        if (getRaidDifficulty() >= 7) {
            return 1;
        }
        return 0;
    }

    /**
     * Calculates the raid difficulty level with the formula: Level difficulty + Raid Omen Level + Raid wave
     * @return The raid difficulty level
     */
    private int getRaidDifficulty() {
        if (raider.hasActiveRaid()) {
            return level.getDifficulty().getId() + raider.getCurrentRaid().getRaidOmenLevel() + raider.getWave();
        }
        return 0;
    }
}
