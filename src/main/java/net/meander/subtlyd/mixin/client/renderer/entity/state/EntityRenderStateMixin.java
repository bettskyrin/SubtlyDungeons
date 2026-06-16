package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.state.EntityRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAccessor {
    private boolean isWitherSkull;

    @Override
    public boolean subtlyd$isOnSoulFire() {
        return isWitherSkull;
    }

    @Override
    public void subtlyd$setSoulFire(boolean isOnSoulFire) {
        this.isWitherSkull = isOnSoulFire;
    }
}
