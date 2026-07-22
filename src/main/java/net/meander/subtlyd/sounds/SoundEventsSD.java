package net.meander.subtlyd.sounds;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

/**
 * @see net.minecraft.sounds.SoundEvents
 */
public class SoundEventsSD {
    public static final SoundEvent WIND = register("block.air.ambient");
    public static final SoundEvent BUSH_IDLE = register("block.bush.ambient");
    public static final SoundEvent STEW_SERVED = register("block.cauldron.serve");
    public static final SoundEvent STEW_STEWS = register("block.cauldron.stewing");
    public static final SoundEvent ICE_FREEZE = register("block.frosted_ice.freeze");
    public static final SoundEvent SNOW_BRICK_BREAK = register("block.snow_bricks.break");
    public static final SoundEvent SNOW_BRICK_FALL = register("block.snow_bricks.fall");
    public static final SoundEvent SNOW_BRICK_HIT = register("block.snow_bricks.hit");
    public static final SoundEvent SNOW_BRICK_PLACE = register("block.snow_bricks.place");
    public static final SoundEvent SNOW_BRICK_STEP = register("block.snow_bricks.step");
    public static final SoundEvent FLAME_ARROW_HIT = register("entity.arrow.hit_flame");
    public static final SoundEvent FLAME_ARROW_SHOOT = register("entity.arrow.shoot_flame");
    public static final SoundEvent AREA_EFFECT_CLOUD_GAS = register("entity.area_effect_cloud.gas");
    public static final Holder.Reference<SoundEvent> BLAST_FUNGUS_EXPLODE = registerForHolder("entity.blast_fungus.explode");
    public static final SoundEvent ENDER_DRAGON_BREATH = register("entity.ender_dragon.breath");
    public static final SoundEvent EVOKER_FANGS_APPEAR = register("entity.evoker_fangs.appear");
    public static final SoundEvent WITHER_SKELETONS_SUMMONED = register("entity.wither_skeleton.summon");
    public static final SoundEvent BLAST_FUNGUS_THROW = register("item.blast_fungus.throw");
    public static final SoundEvent BLADE_CLASH = register("random.blade.clash");
    public static final SoundEvent BLADE_WOOD_CLASH = register("random.blade_wood.clash");
    public static final SoundEvent STICK_LIGHT = register("item.stick.light");
    public static final SoundEvent TRIDENT_CHARGING = register("item.trident.charging");
    public static final SoundEvent TRIDENT_CHARGED = register("item.trident.charged");
    public static final Holder.Reference<SoundEvent> LEAVES_AMBIENT = registerForHolder("block.oak_leaves.ambient");
    public static final SoundEvent GRASS_AMBIENT = register("block.short_grass.ambient");

    public static void registration() {}

    private static SoundEvent register(String string) {
        return register(UtilSD.identifier(string));
    }

    private static SoundEvent register(Identifier resourceLocation) { return register(resourceLocation, resourceLocation); }

    private static SoundEvent register(Identifier resourceLocation, Identifier resourceLocation2) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation2));
    }

    private static Holder.Reference<SoundEvent> registerForHolder(final String id) {
        return registerForHolder(UtilSD.identifier(id));
    }

    private static Holder.Reference<SoundEvent> registerForHolder(final Identifier id) {
        return registerForHolder(id, id);
    }

    private static Holder.Reference<SoundEvent> registerForHolder(final Identifier id, final Identifier soundId) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId));
    }
}
