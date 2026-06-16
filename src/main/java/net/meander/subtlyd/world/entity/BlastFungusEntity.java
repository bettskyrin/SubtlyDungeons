package net.meander.subtlyd.world.entity;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.LevelSD;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BlastFungusEntity extends ThrowableItemProjectile {
    public BlastFungusEntity(final EntityType<? extends BlastFungusEntity> type, final Level level) {
        super(type, level);
    }

    public BlastFungusEntity(final Level level, final LivingEntity mob, final ItemStack itemStack) {
        super(EntityTypesSD.BLAST_FUNGUS, mob, level, itemStack);
    }

    public BlastFungusEntity(final Level level, final double x, final double y, final double z, final ItemStack itemStack) {
        super(EntityTypesSD.BLAST_FUNGUS, x, y, z, level, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemsSD.BLAST_FUNGUS;
    }

    private ParticleOptions getParticle() {
        ItemStack item = this.getItem();
        return item.isEmpty() ? ParticleTypes.CRIMSON_SPORE : new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(item));
    }

    @Override
    public void handleEntityEvent(final byte id) {
        if (id == 3) {
            ParticleOptions particle = this.getParticle();

            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(final EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        explodeFungus();
    }

    @Override
    protected void onHit(final HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte)3);
            explodeFungus();
            this.discard();
        }
    }

    private void explodeFungus() {
        this.level().explode(this,
                Explosion.getDefaultDamageSource(this.level(), this.getOwner()),
                new ExplosionDamageCalculator() {
                    @Override
                    public float getKnockbackMultiplier(Entity entity) {
                        return 0.35F;
                    }

                    @Override
                    public float getEntityDamageAmount(final Explosion explosion, final Entity entity, final float exposure) {
                        float diameter = explosion.radius() * 2.0F;
                        Vec3 center = explosion.center();
                        float dist = Mth.sqrt((float) entity.distanceToSqr(center)) / diameter;
                        float pow = ((1.0F - dist) * exposure) / 5.5F;

                        return (pow * pow + pow) / 2.0F * 7.0F * diameter + 1.0F;
                    }
                },
                this.getX(),
                this.getY(),
                this.getZ(),
                4.0F,
                false,
                Level.ExplosionInteraction.MOB,
                ParticleTypes.WARPED_SPORE,
                ParticleTypes.CRIMSON_SPORE,
                LevelSD.DEFAULT_EXPLOSION_SPORE_PARTICLES,
                SoundEventsSD.BLAST_FUNGUS_EXPLODE);
    }
}
