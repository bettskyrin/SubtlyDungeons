package com.meander.subtlyd.world.level.block.sounds;

import com.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.world.level.block.SoundType;

public class SoundTypeSD {
    public static final SoundType SNOW_BRICKS = new SoundType(
            1.0F, 1.0F, SoundEventsSD.SNOW_BRICK_BREAK, SoundEventsSD.SNOW_BRICK_STEP, SoundEventsSD.SNOW_BRICK_PLACE, SoundEventsSD.SNOW_BRICK_HIT, SoundEventsSD.SNOW_BRICK_FALL
    );
}
