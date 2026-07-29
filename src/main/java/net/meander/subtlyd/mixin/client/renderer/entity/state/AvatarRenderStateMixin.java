package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.state.QuiverRenderState;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements QuiverRenderState {
    private boolean hasQuiver;
    private Identifier quiverTexture = UtilSD.identifier("textures/entity/equipment/quiver.png");

    @Override
    public boolean hasQuiver() {
        return hasQuiver;
    }

    @Override
    public Identifier getQuiverTexture() {
        return quiverTexture;
    }

    @Override
    public void setHasQuiver(boolean hasQuiver) {
        this.hasQuiver = hasQuiver;
    }

    @Override
    public void setQuiverTexture(Identifier texture) {
        this.quiverTexture = texture;
    }
}