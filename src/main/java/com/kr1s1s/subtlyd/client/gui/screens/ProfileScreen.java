package com.kr1s1s.subtlyd.client.gui.screens;

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
        
    }

}
