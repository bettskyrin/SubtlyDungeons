package net.meander.subtlyd.mixin.client.renderer.item;

import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.meander.subtlyd.tags.PotionTagsSD;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemHelperSD;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {
    @Inject(method = "updateForTopItem", at = @At("TAIL"))
    private void updateTridentAura(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed, CallbackInfo ci) {
        if (item.is(Items.TRIDENT)) {
            if (owner instanceof LivingEntity livingEntity) {
                if (livingEntity.getTicksUsingItem() >= ItemHelperSD.CHANNELING_CHARGE_TIME && ItemHelperSD.canChargeChanneling(level, livingEntity, item)) {
                    ((ChargedTridentState.Accessor) output).setCharged(true);
                    return;
                }
            }
            ((ChargedTridentState.Accessor) output).setCharged(false);
        }
    }

    @ModifyVariable(method = "updateForTopItem", at = @At("HEAD"), argsOnly = true, name = "item")
    private ItemStack createPotionArchetypes(ItemStack item) {
        if (item.is(Items.POTION) || item.is(Items.SPLASH_POTION) || item.is(Items.LINGERING_POTION)) {
            PotionContents contents = item.get(DataComponents.POTION_CONTENTS);

            if (contents != null && contents.potion().isPresent()) {
                Holder<Potion> potion = contents.potion().get();
                Identifier modelId = null;
                String prefix = "";

                if (item.is(Items.SPLASH_POTION)) {
                    prefix = "splash_";
                } else if (item.is(Items.LINGERING_POTION)) {
                    prefix = "lingering_";
                }

                if (potion.is(PotionTagsSD.CONICAL)) {
                    modelId = Util.identifier(prefix + "conical_bottle");
                } else if (potion.is(PotionTagsSD.SPHERICAL)) {
                    modelId = Util.identifier(prefix + "spherical_bottle");
                } else if (potion.is(PotionTagsSD.VIAL)) {
                    modelId = Util.identifier(prefix + "vial_bottle");
                }

                if (modelId != null && !modelId.equals(item.get(DataComponents.ITEM_MODEL))) {
                    ItemStack potionStack = item.copy();

                    potionStack.set(DataComponents.ITEM_MODEL, modelId);
                    return potionStack;
                }
            }
        }
        return item;
    }
}
