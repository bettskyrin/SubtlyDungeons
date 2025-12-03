package com.kr1s1s.subtlyd.mixin.client.renderer;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.file.Path;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Invoker("takeAutoScreenshot")
    void invokeTakeAutoScreenshot(Path path);
}
