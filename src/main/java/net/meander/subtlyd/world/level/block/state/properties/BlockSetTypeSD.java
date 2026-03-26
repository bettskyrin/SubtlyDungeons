package net.meander.subtlyd.world.level.block.state.properties;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.Map;
import java.util.stream.Stream;

public class BlockSetTypeSD {
    private static final Map<String, BlockSetType> TYPES = new Object2ObjectArrayMap<>();
    public static final BlockSetType BASALT = register(
            new BlockSetType(
                    "polished_blackstone",
                    true,
                    true,
                    false,
                    BlockSetType.PressurePlateSensitivity.MOBS,
                    SoundType.STONE,
                    SoundEvents.IRON_DOOR_CLOSE,
                    SoundEvents.IRON_DOOR_OPEN,
                    SoundEvents.IRON_TRAPDOOR_CLOSE,
                    SoundEvents.IRON_TRAPDOOR_OPEN,
                    SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
                    SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
                    SoundEvents.STONE_BUTTON_CLICK_OFF,
                    SoundEvents.STONE_BUTTON_CLICK_ON
            )
    );

    private static BlockSetType register(final BlockSetType type) {
        TYPES.put(type.name(), type);
        return type;
    }

    public static Stream<BlockSetType> values() {
        return TYPES.values().stream();
    }
}
