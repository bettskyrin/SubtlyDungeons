package com.kr1s1s.subtlyd.mixin;

import com.kr1s1s.subtlyd.mobs.ZombieSD;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieMixin {
    @Inject(method = "handleAttributes", at = @At("TAIL"), cancellable = true)
    protected void alterAttributes(float f, CallbackInfo ci) {
        ZombieSD.alterAttributes((Zombie) (Object) this, f);
    }
}
