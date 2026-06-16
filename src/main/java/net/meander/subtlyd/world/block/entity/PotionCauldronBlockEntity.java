package net.meander.subtlyd.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PotionCauldronBlockEntity extends BlockEntity {
    private Holder<Potion> potion;
    private String potionType;

    public PotionCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesSD.POTION_CAULDRON, pos, state);
        potion = Potions.FIRE_RESISTANCE;
        potionType = BuiltInRegistries.ITEM.getKey(Items.POTION).toString();
    }

    public Holder<Potion> getPotion() {
        return potion;
    }

    public void setPotion(Holder<Potion> potion) {
        this.potion = potion;
    }

    public @NotNull String getPotionType() {
        return potionType;
    }

    public void setPotionType(String potionType) {
        this.potionType = potionType;
    }

    @Override
    public void loadAdditional(ValueInput valueInput) {
        if (valueInput.getString("PotionName").isPresent() && valueInput.getString("PotionType").isPresent()) {
            Identifier id = Identifier.tryParse(valueInput.getString("PotionName").get());
            String type = valueInput.getString("PotionType").get();

            if (id != null) {
                Optional<Holder.Reference<Potion>> holder = BuiltInRegistries.POTION.get(id);

                if (holder.isPresent()) {
                    potion = holder.get();
                    potionType = type;
                }
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        if (potion != null) {
            Identifier potionResource = BuiltInRegistries.POTION.getKey(potion.value());

            if (potionResource != null) {
                String potionName = potionResource.toString();

                valueOutput.putString("PotionName", potionName);
                valueOutput.putString("PotionType", potionType);
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithFullMetadata(provider);
    }
}
