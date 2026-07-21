package net.meander.subtlyd.mixin.client.gui.screens.worldselection;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.client.gui.screens.TailoredWorldGenSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.SelectableEntry;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.nio.file.Path;

@Environment(EnvType.CLIENT)
@Mixin(WorldSelectionList.class)
public class WorldSelectionListMixin {
    private static final boolean canChangeUi = OptionsSD.gui().get();

    @Inject(method = "getRowWidth", at = @At("RETURN"), cancellable = true)
    public void getRowWidth(CallbackInfoReturnable<Integer> cir) {
        if (canChangeUi) {
            final WorldSelectionList worldSelectionList = (WorldSelectionList) (Object) this;
            cir.setReturnValue(worldSelectionList.getScreen().width - 8);
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(targets = "net.minecraft.client.gui.screens.worldselection.WorldSelectionList$WorldListEntry")
    private abstract static class WorldListEntryMixin extends WorldSelectionList.Entry implements SelectableEntry {
        @Shadow @Final private LevelSummary summary;
        private static final boolean canChangeUi = OptionsSD.gui().get();
        private static final int ICON_WIDTH = 57;
        private static final int ICON_HEIGHT = 32;

        @Inject(method = "recreateWorld", at = @At("HEAD"))
        private void loadRecreateSettings(CallbackInfo ci) {
            Path oldWorldDir = Minecraft.getInstance().gameDirectory.toPath()
                    .resolve("saves")
                    .resolve(summary.getLevelId());

            TailoredWorldGenSettings.loadSettingsFromFile(oldWorldDir);
        }

        @ModifyArgs(method = "extractContent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V"))
        private void modifyWorldIconWidth(Args args) {
            if (canChangeUi) {
                args.set(6, ICON_WIDTH);
                args.set(8, ICON_WIDTH);
            }
        }

        @ModifyArg(method = "extractContent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"), index = 2)
        private int modifyWorldIconFillWidth(int x0) {
            if (canChangeUi) {
                return getContentX() + ICON_WIDTH;
            }
            return x0;
        }

        /**
         * Replaces the isMouseOver with isMouseWithin
         */
        @Redirect(method = "extractContent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/WorldSelectionList$WorldListEntry;mouseOverIcon(III)Z"))
        private boolean modifyIsOverIcon(WorldSelectionList.WorldListEntry instance, int relX, int relY, int size, final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
            if (canChangeUi) {
                return isMouseWithin(mouseX, mouseY, getContentX() + ICON_WIDTH, getContentY() + size);
            }
            return instance.mouseOverIcon(relX, relY, size);
        }

        @Redirect(method = "extractContent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
        private void modifyPlayButtonIconSprite(GuiGraphicsExtractor instance, RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height) {
            if (canChangeUi) {
                int MID_ICON = getContentX() + ICON_WIDTH / 4;

                instance.blitSprite(renderPipeline, location, MID_ICON, getContentY(), width, height);
            }
        }

        @Inject(method = "getTextX", at = @At("RETURN"), cancellable = true)
        private void extendTextWidth(CallbackInfoReturnable<Integer> cir) {
            if (canChangeUi) {
                cir.setReturnValue(getContentX() + ICON_WIDTH + 3);
            }
        }

        /**
         * Replaces isMouseOver with isMouseWithin based on a mouseButtonEvent.
         */
        @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/WorldSelectionList$WorldListEntry;mouseOverIcon(III)Z"))
        private boolean modifyIsClickedIcon(WorldSelectionList.WorldListEntry instance, int relX, int relY, int size, final MouseButtonEvent event, final boolean doubleClick) {
            if (canChangeUi) {
                return isMouseWithin((int) event.x(), (int) event.y(), getContentX() + ICON_WIDTH, getContentY() + ICON_HEIGHT);
            }
            return instance.mouseOverIcon(relX, relY, size);
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
            return mouseX >= getContentX()
                    && mouseX < right
                    && mouseY >= getContentY()
                    && mouseY < top;
        }
    }
}
