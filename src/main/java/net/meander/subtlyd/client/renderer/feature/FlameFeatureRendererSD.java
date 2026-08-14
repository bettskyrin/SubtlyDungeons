package net.meander.subtlyd.client.renderer.feature;

/**
 * @see net.minecraft.client.renderer.feature.FlameFeatureRenderer.Submit
 */
public interface FlameFeatureRendererSD {
    interface Submit {
        boolean isSoulFire();
        void setSoulFire(boolean isSoulFire);
    }
}
