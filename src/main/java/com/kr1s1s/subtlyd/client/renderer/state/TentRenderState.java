package com.kr1s1s.subtlyd.client.renderer.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

public class TentRenderState extends EntityRenderState {
    public float scale;
    public boolean hasRedOverlay;
    public float yRot;
    public float xRot;

    public float getXRot(float f) {
        return f == 1.0F ? this.xRot : Mth.lerp(f, this.xRot, this.xRot);
    }
}
