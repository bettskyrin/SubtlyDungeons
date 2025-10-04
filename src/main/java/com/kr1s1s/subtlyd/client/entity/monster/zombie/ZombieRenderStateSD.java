package com.kr1s1s.subtlyd.client.entity.monster.zombie;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

@Environment(EnvType.CLIENT)
public class ZombieRenderStateSD extends ZombieRenderState {
    public boolean isLeader;
}
