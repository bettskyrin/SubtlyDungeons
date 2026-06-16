package net.meander.subtlyd.mixin.client.renderer.feature;

import net.meander.subtlyd.client.renderer.feature.FlameFeatureRendererSubmitAccessor;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlameFeatureRenderer.Submit.class)
public class FlameFeatureRendererSubmitMixin implements FlameFeatureRendererSubmitAccessor {
    private boolean isSoulFire = false;

    @Override
    public boolean isSoulFire() {
        return isSoulFire;
    }

    @Override
    public void setSoulFire(boolean isSoulFire) {
        this.isSoulFire = isSoulFire;
    }
}
