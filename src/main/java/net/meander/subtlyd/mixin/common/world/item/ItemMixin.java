package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.world.item.ItemHelperSD;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "onUseTick", at = @At("TAIL"))
    private void playTridentChargeSound(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining, CallbackInfo ci) {
        if (itemStack.is(Items.TRIDENT)) {
            if (ItemHelperSD.canChargeChanneling(level, livingEntity, itemStack)) {
                int ticksUsed = itemStack.getItem().getUseDuration(itemStack, livingEntity) - ticksRemaining;
                BlockPos pos = BlockPos.containing(livingEntity.getX(), livingEntity.getY() + 0.5F, livingEntity.getZ());

                if (ticksUsed >= 10 && ticksUsed < ItemHelperSD.CHANNELING_CHARGE_TIME) {
                    if (level.getRandom().nextFloat() < 0.07F) {
                        level.playSound(null, pos, SoundEventsSD.TRIDENT_CHARGING, SoundSource.PLAYERS);
                    }
                } else if (ticksUsed >= ItemHelperSD.CHANNELING_CHARGE_TIME) {
                    if ((ticksUsed - ItemHelperSD.CHANNELING_CHARGE_TIME) % 43 == 0) { // Every 2.15 seconds
                        level.playSound(null, pos, SoundEventsSD.TRIDENT_CHARGED, SoundSource.PLAYERS);
                    }
                }
            }
        }
    }
}
