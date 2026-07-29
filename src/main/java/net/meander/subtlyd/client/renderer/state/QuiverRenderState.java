package net.meander.subtlyd.client.renderer.state;

import net.minecraft.resources.Identifier;

public interface QuiverRenderState {
    boolean hasQuiver();

    Identifier getQuiverTexture();

    void setHasQuiver(boolean hasQuiver);

    void setQuiverTexture(Identifier texture);
}