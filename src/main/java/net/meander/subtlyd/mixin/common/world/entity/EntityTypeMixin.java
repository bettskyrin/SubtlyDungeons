package net.meander.subtlyd.mixin.common.world.entity;

import com.mojang.serialization.Dynamic;
import net.meander.subtlyd.datafix.DataFixerSD;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityProcessor;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityType.class)
public class EntityTypeMixin {
    @Inject(method = "loadEntityRecursive(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnRequest;Lnet/minecraft/world/entity/EntityProcessor;)Lnet/minecraft/world/entity/Entity;", at = @At("HEAD"))
    private static void applyDataFixer(CompoundTag tag, Level level, EntitySpawnRequest request, EntityProcessor postLoad, CallbackInfoReturnable<Entity> cir) {
        int savedVersion = tag.getIntOr("DataVersionSD", 0);
        int currentVersion = UtilSD.DATA_VERSION;
        Optional<String> id = tag.getString("id");

        if (id.isPresent()) {
            if (id.get().startsWith(UtilSD.NAMESPACE)) {
                if (savedVersion < currentVersion) {
                    Dynamic<Tag> dynamic = DataFixerSD.getFixer().update(References.ENTITY, new Dynamic<>(NbtOps.INSTANCE, tag), savedVersion, currentVersion);

                    if (dynamic.getValue() instanceof CompoundTag compound) {
                        tag.merge(compound);
                        tag.putInt("DataVersionSD", currentVersion);
                    }
                }
            }
        }
    }
}