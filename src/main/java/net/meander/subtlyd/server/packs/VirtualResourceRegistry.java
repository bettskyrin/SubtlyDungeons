package net.meander.subtlyd.server.packs;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualResourceRegistry {
    private static final Map<Identifier, byte[]> resource = new ConcurrentHashMap<>();
    private static final PackResources VIRTUAL_PACK = new VirtualPackResources();

    public static byte[] getResource(Identifier location) {
        return resource.get(location);
    }

    public static PackResources getVirtualPack() {
        return VIRTUAL_PACK;
    }

    public static Set<Identifier> getAllFiles() {
        return resource.keySet();
    }

    public static void registerResource(Identifier location, String jsonContent) {
        resource.put(location, jsonContent.getBytes(StandardCharsets.UTF_8));
    }

    public static void clear() {
        resource.clear();
    }

    public static boolean hasFile(Identifier location) {
        return resource.containsKey(location);
    }
}
