package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownTridentRenderState.class)
public class ThrownTridentRenderStateMixin implements ChargedTridentState.Accessor {
    private boolean subtlyDungeons$charged = false;

    @Override
    public boolean subtlyDungeons$isCharged() {
        return subtlyDungeons$charged;
    }

    @Override
    public void subtlyDungeons$setCharged(boolean charged) {
        subtlyDungeons$charged = charged;
    }
}
