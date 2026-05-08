package net.meander.subtlyd.client.renderer;

public class ChargedTridentState {
    public static final ThreadLocal<Boolean> CHANNELING_CHARGE = ThreadLocal.withInitial(() -> false);

    public interface Accessor {
        boolean subtlyd$isCharged();
        void subtlyd$setCharged(boolean charged);
    }
}
