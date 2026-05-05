package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.sounds.SoundEventsSD;
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
            int ticksUsed = itemStack.getItem().getUseDuration(itemStack, livingEntity) - ticksRemaining;

            if (ticksUsed >= 10 && ticksUsed < 60) {
                if (level.getRandom().nextFloat() < 0.05F) {
                    level.playSound(null, livingEntity.blockPosition(), SoundEventsSD.TRIDENT_CHARGING, SoundSource.PLAYERS);
                }
            } else if (ticksUsed >= 60) {
                if ((ticksUsed - 60) % 43 == 0) { // Every 2.15 seconds
                    level.playSound(null, livingEntity.blockPosition(), SoundEventsSD.TRIDENT_CHARGED, SoundSource.PLAYERS);
                }
            }
        }
    }
}
