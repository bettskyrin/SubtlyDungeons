package net.meander.subtlyd.server.packs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

class VirtualPackResources implements PackResources {
    private final PackLocationInfo LOCATION = new PackLocationInfo(
            "subtlyd_virtual_pack",
            Component.literal("Subtly Dungeons Virtual Pack"),
            PackSource.BUILT_IN,
            Optional.empty()
    );

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... elements) {
        return null;
    }


    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType type, Identifier id) {
        if (VirtualResourceRegistry.hasFile(id)) {
            return () -> new java.io.ByteArrayInputStream(VirtualResourceRegistry.getResource(id));
        }
        return null;
    }

    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput resourceOutput) {
        for (Identifier id : VirtualResourceRegistry.getAllFiles()) {
            if (id.getNamespace().equals(namespace) && id.getPath().startsWith(path)) {
                IoSupplier<InputStream> resource = getResource(type, id);

                if (resource != null) {
                    resourceOutput.accept(id, resource);
                }
            }
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return Set.of("subtlyd", "minecraft");
    }


    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionType<T> metadataSerializer) {
        return null;
    }

    @Override
    public PackLocationInfo location() {
        return LOCATION;
    }

    @Override
    public void close() {}
}