package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.state.UndeadRenderStateAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.UndeadRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(UndeadRenderState.class)
public class UndeadRenderStateMixin implements UndeadRenderStateAccessor {
    private boolean isLeader;

    /**
     * @return Whether an undead entity is a leader zombie or not.
     */
    @Override public boolean isLeader() {
        return isLeader;
    }

    /**
     * Sets an undead entity's leader status.
     * @param bl The leader status.
     */
    @Override public void setLeader(boolean bl) {
        isLeader = bl;
    }
}
