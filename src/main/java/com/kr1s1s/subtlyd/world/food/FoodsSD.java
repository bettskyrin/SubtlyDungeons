package com.kr1s1s.subtlyd.world.food;

import net.minecraft.world.food.FoodProperties;

public class FoodsSD {
    public static final FoodProperties APPLE_PIE = new FoodProperties.Builder().nutrition(8).saturationModifier(0.3F).build();
    public static final FoodProperties CALAMARI = new FoodProperties.Builder().nutrition(3).saturationModifier(0.1F).build();
    public static final FoodProperties COOKED_CALAMARI = new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build();
    public static final FoodProperties POTTAGE = stew(6).build();
    private static FoodProperties.Builder stew(int i) {
        return new FoodProperties.Builder().nutrition(i).saturationModifier(0.6F);
    }
}
