package net.meander.subtlyd.mixin.common.entity.animal;

import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Dolphin.class)
public abstract class DolphinMixin extends AgeableWaterCreature {
    protected DolphinMixin(EntityType<? extends AgeableWaterCreature> type, Level level) {
        super(type, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addHuntingGoal(CallbackInfo ci) {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Squid.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Cod.class, false));
    }

    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean allowCalamariFeeding(ItemStack stack, TagKey<Item> tag) {
        if (tag == ItemTags.FISHES) {
            return stack.is(tag) || stack.is(ItemsSD.CALAMARI);
        }
        return stack.is(tag);
    }
}
