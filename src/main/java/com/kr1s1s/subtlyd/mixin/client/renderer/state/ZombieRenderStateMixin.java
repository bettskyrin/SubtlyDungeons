package com.kr1s1s.subtlyd.mixin.client.renderer.state;

import com.kr1s1s.subtlyd.client.renderer.ZombieRenderStateAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(ZombieRenderState.class)
public class ZombieRenderStateMixin implements ZombieRenderStateAccessor {
    private boolean isLeader;

    /**
     * @return Whether a zombie is a leader zombie or not.
     */
    @Override public boolean subtlyDungeons$isLeader() {
        return isLeader;
    }

    /**
     * Sets a zombie entity's leader status.
     * @param bl The leader status.
     */
    @Override public void subtlyDungeons$setLeader(boolean bl) {
        isLeader = bl;
    }
}
