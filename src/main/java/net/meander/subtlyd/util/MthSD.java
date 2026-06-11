package net.meander.subtlyd.util;

public class MthSD {
    public static double roundToTenThousandths(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
