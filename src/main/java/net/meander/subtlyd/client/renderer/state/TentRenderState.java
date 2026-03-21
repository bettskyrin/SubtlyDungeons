package net.meander.subtlyd.client.renderer.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;

public class TentRenderState extends EntityRenderState {
    public boolean hasRedOverlay;
    public int hurtDir;
    public float scale;
    public float yRot;
    public float xRot;
    public float hurtTime;
    public float damage;

    public float getXRot(float f) {
        return f == 1.0F ? this.xRot : Mth.lerp(f, this.xRot, this.xRot);
    }
}
