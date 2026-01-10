package com.kr1s1s.subtlyd.mixin.client.gui.screens.worldselection;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(WorldSelectionList.class)
public class WorldSelectionListMixin {
    WorldSelectionList worldSelectionList = (WorldSelectionList) (Object) this;

    @Inject(method = "getRowWidth", at = @At("RETURN"), cancellable = true)
    public void getRowWidth(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(worldSelectionList.getScreen().width - 8);
    }

    @Environment(EnvType.CLIENT)
    @Mixin(targets = "net.minecraft.client.gui.screens.worldselection.WorldSelectionList$WorldListEntry")
    private static final class WorldListEntryMixin {
        private static final int ICON_WIDTH = 57;
        private static final int ICON_HEIGHT = 32;

        @ModifyArg(method = "renderContent",
                    at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/gui/components/StringWidget;setPosition(II)V"), index = 1)
        private int changeYPos(int arg, @Local(ordinal = 1, argsOnly = true) int contentY) {
            return contentY + 9 + 9 + 3;
        }

        @ModifyVariable(method = "renderContent", at = @At("STORE"), ordinal = 5)
        private int modifyIsOverIcon(int value) {
            return isMouseWithin(i, j, this.getContentX() + ICON_WIDTH, this.getContentY() + ICON_HEIGHT);
        }
    }
}
