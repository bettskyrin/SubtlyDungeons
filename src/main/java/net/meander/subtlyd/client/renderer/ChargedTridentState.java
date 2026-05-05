package net.meander.subtlyd.client.renderer;

public class ChargedTridentState {
    public static final ThreadLocal<Boolean> IS_CHARGED = ThreadLocal.withInitial(() -> false);

    public interface Accessor {
        boolean subtlyDungeons$isCharged();
        void subtlyDungeons$setCharged(boolean charged);
    }
}
