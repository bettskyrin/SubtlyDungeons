package com.kr1s1s.subtlyd.client.gui.screens;

import com.kr1s1s.subtlyd.client.renderer.GuiPlayerRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ProfileScreen extends Screen {
    protected final Screen lastScreen;

    public ProfileScreen(final Screen lastScreen) {
        super(Component.translatable("menu.profile"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        // TODO
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        GuiPlayerRenderer.renderPlayer(guiGraphics, this.width / 2, this.height / 2 + 50, 80, mouseX, mouseY);
    }
}
