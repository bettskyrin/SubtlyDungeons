package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.meander.subtlyd.world.item.enchantment.EnchantmentHelperSD;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    /**
     * Creates a buffer period where a trident's charge can be transferred properly to a thrown trident.
     */
    @ModifyVariable(method = "releaseUsing", at = @At("STORE"), name = "trident")
    private ThrownTrident maintainCharge(ThrownTrident trident, ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        if (EnchantmentHelperSD.checkEnchantment(trident.getWeaponItem(), Enchantments.CHANNELING) && trident.level().canHaveWeather()) {
            final int TICK_BUFFER = 50;
            int ticksUsed = trident.getWeaponItem().getUseDuration(entity) - remainingTime;

            if (ticksUsed >= TICK_BUFFER) {
                ((ChargedTridentState.Accessor) trident).setCharged(true);
            }
        }
        return trident;
    }
}
