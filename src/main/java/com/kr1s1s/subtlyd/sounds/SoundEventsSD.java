package com.kr1s1s.subtlyd.sounds;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundEventsSD {
    public static final SoundEvent WIND = register("block.air.idle");
    public static final SoundEvent BUSH_IDLE = register("block.bush.idle");
    public static final SoundEvent SNOW_BRICK_BREAK = register("block.snow_bricks.break");
    public static final SoundEvent SNOW_BRICK_FALL = register("block.snow_bricks.fall");
    public static final SoundEvent SNOW_BRICK_HIT = register("block.snow_bricks.hit");
    public static final SoundEvent SNOW_BRICK_PLACE = register("block.snow_bricks.place");
    public static final SoundEvent SNOW_BRICK_STEP = register("block.snow_bricks.step");
    public static final SoundEvent STICK_LIGHT = register("item.stick.light");

    public static void registration() { }

    private static SoundEvent register(String string) {
        return register(SubtlyDungeons.resourceLocation(string));
    }

    private static SoundEvent register(ResourceLocation resourceLocation) { return register(resourceLocation, resourceLocation); }

    private static SoundEvent register(ResourceLocation resourceLocation, ResourceLocation resourceLocation2) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation2));
    }

}
