package com.kr1s1s.subtlyd.client.renderer;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.player.PlayerSkin;

public class GuiPlayerRenderer {
    public static void renderPlayer(GuiGraphics guiGraphics, int x, int y, int scale, float mouseX, float mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        GameProfile profile = minecraft.getGameProfile();
        PlayerSkin skin = minecraft.getSkinManager().createLookup(profile, false).get();

        EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
    }
}
