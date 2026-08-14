package net.meander.subtlyd.mixin.client.gui.screens.debug;

import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameModeSwitcherScreen.GameModeIcon.class)
public class GameModeIconMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;)V", ordinal = 2))
    private static ItemLike modifyItem(ItemLike itemLike) {
        return Items.BURIED_TREASURE_MAP;
    }
}