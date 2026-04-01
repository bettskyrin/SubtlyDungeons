package net.meander.subtlyd.world.inventory;

import net.minecraft.util.Mth;

public class AnvilMenuSD {
    public static int getCostByEnchantibility(int input, int addition) {
        int difference = Math.abs(input - addition);

        if (difference <= 1) {
            return 40;
        }
        return 40 + Mth.ceil(difference * 2.4);
    }
}
