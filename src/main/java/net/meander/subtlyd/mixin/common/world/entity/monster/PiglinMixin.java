package net.meander.subtlyd.mixin.common.world.entity.monster;

import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Piglin.class, ZombifiedPiglin.class})
public class PiglinMixin {
    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void populateWithDagger(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        Mob piglin = (Mob) (Object) this;
        ItemStack mainHand = piglin.getItemBySlot(EquipmentSlot.MAINHAND);

        if ((mainHand.is(Items.GOLDEN_SWORD) || mainHand.is(Items.GOLDEN_SPEAR)) && random.nextFloat() < 0.35F) {
            ItemStack dagger = new ItemStack(ItemsSD.GOLDEN_DAGGER);
            dagger.applyComponents(mainHand.getComponentsPatch());
            piglin.setItemSlot(EquipmentSlot.MAINHAND, dagger);
        }
    }
}