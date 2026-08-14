package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.entity.state.EntityRenderStateSD;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateSD {
    private boolean isOnSoulFire;

    @Override
    public boolean isOnSoulFire() {
        return isOnSoulFire;
    }

    @Override
    public void setOnSoulFire(boolean isOnSoulFire) {
        this.isOnSoulFire = isOnSoulFire;
    }
}
