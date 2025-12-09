package com.kr1s1s.subtlyd.mixin.server.entity;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface PlayerAccessor {
    @Accessor("sleepCounter")
    void setSleepCounter(int sleepCounter);
}
