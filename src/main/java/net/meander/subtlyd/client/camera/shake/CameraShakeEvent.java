package net.meander.subtlyd.client.camera.shake;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record CameraShakeEvent(Identifier soundEvent, int range, int durationTicks, float intensity) {
    public static final Codec<CameraShakeEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("sound_event").forGetter(CameraShakeEvent::soundEvent),
                Codec.INT.fieldOf("range").forGetter(CameraShakeEvent::range),
                Codec.INT.fieldOf("duration").forGetter(CameraShakeEvent::durationTicks),
                Codec.FLOAT.optionalFieldOf("intensity", 4.0F).forGetter(CameraShakeEvent::intensity)
            )
            .apply(instance, CameraShakeEvent::new)
    );

    public static CameraShakeEvent create(final Identifier soundEvent, final int range, int durationTicks, float intensity) {
        return new CameraShakeEvent(soundEvent, range, durationTicks, intensity);
    }
}
