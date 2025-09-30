package com.kr1s1s.subtlyd.mixin;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Raider.class)
public class RaiderMixin {
    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void finalizeSpawn(CallbackInfoReturnable<Object> cir){
        Raider raider = (Raider) (Object) this;
        ItemStack mainHandItem = raider.getItemBySlot(EquipmentSlot.MAINHAND);

        if ((raider.level().getDifficulty().equals(Difficulty.HARD) && raider.getCurrentRaid().getRaidOmenLevel() >= 4) || (raider.level().getDifficulty().equals(Difficulty.NORMAL) && raider.getCurrentRaid().getRaidOmenLevel() == 5)) {
            if (raider.hasActiveRaid() && !mainHandItem.isEnchanted()) {
                if (mainHandItem.is(Items.CROSSBOW) && (raider.getRandom().nextFloat() < 0.5F)) {
                    mainHandItem.enchant(raider.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).wrapAsHolder(raider.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOptional(Enchantments.FLAME).get()), 1);
                    SubtlyDungeons.debug("Enchanted: " + mainHandItem.getEnchantments());
                }
            }
        }
    }
}
