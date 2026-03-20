package net.meander.subtlyd.mixin.client.gui.screens.worldselection;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.SelectableEntry;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(WorldSelectionList.class)
public class WorldSelectionListMixin {
    final WorldSelectionList worldSelectionList = (WorldSelectionList) (Object) this;

    @Inject(method = "getRowWidth", at = @At("RETURN"), cancellable = true)
    public void getRowWidth(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(worldSelectionList.getScreen().width - 8);
    }

    @Environment(EnvType.CLIENT)
    @Mixin(targets = "net.minecraft.client.gui.screens.worldselection.WorldSelectionList$WorldListEntry")
    private abstract static class WorldListEntryMixin extends WorldSelectionList.Entry implements SelectableEntry {
        private static final int ICON_WIDTH = 57;
        private static final int ICON_HEIGHT = 32;


        /**
         * Changes the width of the world icon.
         */
        @ModifyArgs(method = "extractContent",
                    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V"))
        private void blit(Args args) {
            args.set(6, ICON_WIDTH);
            args.set(8, ICON_WIDTH);
        }

        /**
         * Changes the world icon fill width.
         */
        @ModifyArg(method = "extractContent",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"), index = 2)
        private int fill(int x0) {
            return this.getContentX() + ICON_WIDTH;
        }

        /**
         * Replaces the isMouseOver with isMouseWithin
         */
        @Redirect(method = "extractContent",
                    at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/gui/screens/worldselection/WorldSelectionList$WorldListEntry;mouseOverIcon(III)Z"))
        private boolean modifyIsOverIcon(WorldSelectionList.WorldListEntry instance, int relX, int relY, int size, final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
            return isMouseWithin(mouseX, mouseY, this.getContentX() + ICON_WIDTH, this.getContentY() + size);
        }

        /**
         * Modifies the "play button" icon sprites.
         */
        @Redirect(method = "extractContent",
                at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
        private void modifyBlitSprite(GuiGraphicsExtractor instance, RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height) {
            int MID_ICON = this.getContentX() + ICON_WIDTH / 4;
            instance.blitSprite(renderPipeline, location, MID_ICON, this.getContentY(), width, height);
        }

        /**
         * Extends the width of the text.
         */
        @Inject(method = "getTextX", at = @At("RETURN"), cancellable = true)
        private void getTextX(CallbackInfoReturnable<Integer> cir) {
            cir.setReturnValue(this.getContentX() + ICON_WIDTH + 3);
        }

        /**
         * Replaces isMouseOver with isMouseWithin based on a mouseButtonEvent.
         */
        @Redirect(method = "mouseClicked",
                at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/screens/worldselection/WorldSelectionList$WorldListEntry;mouseOverIcon(III)Z"))
        private boolean modifyIsClickedIcon(WorldSelectionList.WorldListEntry instance, int relX, int relY, int size, final MouseButtonEvent event, final boolean doubleClick) {
            return isMouseWithin((int) event.x(), (int) event.y(), this.getContentX() + ICON_WIDTH, this.getContentY() + ICON_HEIGHT);
        }

        /**
         * Tests if the mouse is within a specified rectangle.
         * @param mouseX The mouse's x-position.
         * @param mouseY THe mouse's y-position.
         * @param right The right edge of the rectangle.
         * @param top The top edge of the rectangle.
         * @return Whether the mouse is within this rectangle or not.
         */
        private boolean isMouseWithin(int mouseX, int mouseY, int right, int top) {
            return mouseX >= this.getContentX()
                    && mouseX < right
                    && mouseY >= this.getContentY()
                    && mouseY < top;
        }
    }
}
