package net.meander.subtlyd.client.renderer.state;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class TentRenderState extends EntityRenderState {
    public int hurtDir;
    public float hurtTime;
    public float damage;
    public float scale;
    public float yRot;
    public float xRot;
    private static final Identifier DEFAULT_TEXTURE = UtilSD.identifier("textures/entity/tent/white_tent.png");
    public Identifier texture = DEFAULT_TEXTURE;

    public float getXRot(float partialTicks) {
        return partialTicks == 1.0F ? xRot : Mth.rotLerp(partialTicks, xRot, xRot);
    }
}
