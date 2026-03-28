package net.meander.subtlyd.client.resources.camera;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record CameraShakeEvent(Identifier soundEvent, float maxDistance, int durationTicks, float modifier) {
    public static final Codec<CameraShakeEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("sound_event").forGetter(CameraShakeEvent::soundEvent),
            Codec.FLOAT.fieldOf("max_distance").forGetter(CameraShakeEvent::maxDistance),
            Codec.INT.fieldOf("duration").forGetter(CameraShakeEvent::durationTicks),
            Codec.FLOAT.optionalFieldOf("modifier", 4.0F).forGetter(CameraShakeEvent::modifier)
    ).apply(instance, CameraShakeEvent::new));
}
