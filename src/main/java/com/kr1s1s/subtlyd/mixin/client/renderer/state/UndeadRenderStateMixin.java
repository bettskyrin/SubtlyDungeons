package com.kr1s1s.subtlyd.mixin.client.renderer.state;

import com.kr1s1s.subtlyd.client.renderer.UndeadRenderStateAccessor;
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
    @Override public boolean subtlyDungeons$isLeader() {
        return isLeader;
    }

    /**
     * Sets an undead entity's leader status.
     * @param bl The leader status.
     */
    @Override public void subtlyDungeons$setLeader(boolean bl) {
        isLeader = bl;
    }
}
