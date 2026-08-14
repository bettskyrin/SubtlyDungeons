package net.meander.subtlyd.datafix;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import java.util.Map;
import java.util.function.Supplier;

public class V0SD extends Schema {
    public V0SD(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);

        schema.registerSimple(map, "subtlyd:black_tent_entity");
        schema.registerSimple(map, "subtlyd:gray_tent_entity");
        schema.registerSimple(map, "subtlyd:light_gray_tent_entity");
        schema.registerSimple(map, "subtlyd:white_tent_entity");
        schema.registerSimple(map, "subtlyd:pink_tent_entity");
        schema.registerSimple(map, "subtlyd:red_tent_entity");
        schema.registerSimple(map, "subtlyd:brown_tent_entity");
        schema.registerSimple(map, "subtlyd:orange_tent_entity");
        schema.registerSimple(map, "subtlyd:yellow_tent_entity");
        schema.registerSimple(map, "subtlyd:lime_tent_entity");
        schema.registerSimple(map, "subtlyd:green_tent_entity");
        schema.registerSimple(map, "subtlyd:cyan_tent_entity");
        schema.registerSimple(map, "subtlyd:light_blue_tent_entity");
        schema.registerSimple(map, "subtlyd:blue_tent_entity");
        schema.registerSimple(map, "subtlyd:purple_tent_entity");
        schema.registerSimple(map, "subtlyd:magenta_tent_entity");
        return map;
    }
}