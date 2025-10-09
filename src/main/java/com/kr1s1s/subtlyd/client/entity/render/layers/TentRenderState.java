package com.kr1s1s.subtlyd.client.entity.render.layers;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

public class TentRenderState extends EntityRenderState {
    public float scale;
    public float yRot;
    public float xRot;
    public DyeColor color;

    public float getXRot(float f) {
        return f == 1.0F ? this.xRot : Mth.lerp(f, this.xRot, this.xRot);
    }
}
