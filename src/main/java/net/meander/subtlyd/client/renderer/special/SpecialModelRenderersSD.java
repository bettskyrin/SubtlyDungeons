package net.meander.subtlyd.client.renderer.special;

import net.meander.subtlyd.util.Util;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

/**
 * @see SpecialModelRenderers
 */
public class SpecialModelRenderersSD {
    public static void bootstrap() {
        SpecialModelRenderers.ID_MAPPER.put(Util.identifier("heavy_shield"), HeavyShieldSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
