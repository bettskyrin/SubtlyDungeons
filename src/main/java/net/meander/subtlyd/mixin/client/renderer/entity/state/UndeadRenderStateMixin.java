package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.entity.state.UndeadRenderStateSD;
import net.minecraft.client.renderer.entity.state.UndeadRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(UndeadRenderState.class)
public class UndeadRenderStateMixin implements UndeadRenderStateSD {
    private boolean isLeader;

    @Override public boolean isLeader() {
        return isLeader;
    }

    @Override public void setLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }
}
