package com.kr1s1s.subtlyd.mixin.client.renderer.entity.state;

import com.kr1s1s.subtlyd.client.renderer.ZombieRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ZombieRenderState.class)
public class ZombieRenderStateMixin implements ZombieRenderStateAccessor {
    private boolean isLeader = false;

    @Override
    public boolean subtlyDungeons$isLeader() {
        return isLeader;
    }

    @Override
    public void subtlyDungeons$setLeader(boolean bl) {
        isLeader = bl;
    }
}
