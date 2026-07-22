package net.meander.subtlyd.client.renderer.special;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

/**
 * @see SpecialModelRenderers
 */
public class SpecialModelRenderersSD {
    public static void bootstrap() {
        SpecialModelRenderers.ID_MAPPER.put(UtilSD.identifier("heavy_shield"), HeavyShieldSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
