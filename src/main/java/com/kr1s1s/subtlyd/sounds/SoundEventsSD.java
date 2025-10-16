package com.kr1s1s.subtlyd.sounds;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundEventsSD {
    public static SoundEvent WIND = register("block.air.idle");

    public static void init() { }


    private static SoundEvent register(String string) {
        return register(ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, string));
    }

    private static SoundEvent register(ResourceLocation resourceLocation) {
        return register(resourceLocation, resourceLocation);
    }

    private static SoundEvent register(ResourceLocation resourceLocation, ResourceLocation resourceLocation2) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation2));
    }

}
