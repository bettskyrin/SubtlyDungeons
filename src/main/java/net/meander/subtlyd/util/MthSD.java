package net.meander.subtlyd.util;

/**
 * @see net.minecraft.util.Mth
 */
public class MthSD {
    public static double roundToTenThousandths(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
