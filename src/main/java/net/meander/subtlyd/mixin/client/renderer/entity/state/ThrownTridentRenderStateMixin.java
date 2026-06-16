package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownTridentRenderState.class)
public class ThrownTridentRenderStateMixin implements ChargedTridentState.Accessor {
    private boolean charged = false;

    @Override
    public boolean isCharged() {
        return charged;
    }

    @Override
    public void setCharged(boolean isCharged) {
        charged = isCharged;
    }
}
