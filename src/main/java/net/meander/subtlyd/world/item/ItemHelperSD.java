package net.meander.subtlyd.world.item;

import net.meander.subtlyd.world.item.enchantment.EnchantmentHelperSD;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ItemHelperSD {
    public static final int CHANNELING_CHARGE_TIME = 80;

    public static boolean canChargeChanneling(Level level, LivingEntity livingEntity, ItemStack itemStack) {
        return EnchantmentHelperSD.checkEnchantment(itemStack, Enchantments.CHANNELING)
        && level.canHaveWeather() && (livingEntity instanceof Player || level.isThundering());
    }
}
