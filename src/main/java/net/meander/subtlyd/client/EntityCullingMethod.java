package net.meander.subtlyd.client;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum EntityCullingMethod {
	FRUSTUM(0, "options.entity_culling.frustum"),
	OCCLUSION(1, "options.entity_culling.occlusion");

	private static final IntFunction<EntityCullingMethod> BY_ID = ByIdMap.continuous(p -> p.id, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
	public static final Codec<EntityCullingMethod> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, p -> p.id);
	private final int id;
	private final Component caption;

	EntityCullingMethod(final int id, final String key) {
		this.id = id;
		caption = Component.translatable(key);
	}

	public Component caption() {
		return caption;
	}
}
