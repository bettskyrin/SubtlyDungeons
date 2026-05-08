package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownTridentRenderState.class)
public class ThrownTridentRenderStateMixin implements ChargedTridentState.Accessor {
    private boolean subtlyd$charged = false;

    @Override
    public boolean subtlyd$isCharged() {
        return subtlyd$charged;
    }

    @Override
    public void subtlyd$setCharged(boolean charged) {
        subtlyd$charged = charged;
    }
}
