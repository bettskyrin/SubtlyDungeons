package net.meander.subtlyd.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class SporeCloudParticle extends SingleQuadParticle {
    private final TextureAtlasSprite sprite;

    protected SporeCloudParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
        super(level, x, y, z, xa, ya, za, sprite);

        this.sprite = sprite;

        xd = xa + (random.nextDouble() * 2.0 - 1.0) * 0.01 ;
        yd = ya + (random.nextDouble() * 2.0 - 1.5) * 0.01 ;
        zd = za + (random.nextDouble() * 2.0 - 1.0) * 0.01 ;

        friction = 0.85F;
        gravity = 0.04F;

        scale(2.0F);
        setSize(0.25F, 0.25F);

        /* Color */
        rCol = 0.6F + (random.nextFloat() * 0.2F);
        gCol = 0.1F + (random.nextFloat() * 0.1F);
        bCol = 0.8F + (random.nextFloat() * 0.2F);

        quadSize *= 1.5F + random.nextFloat() * 0.5F; // Thickness
        lifetime = (int) (20.0 / (random.nextDouble() * 0.2 + 0.3));

        setSprite(this.sprite);
    }

    @Override
    public void tick() {
        super.tick();
        setSprite(sprite);

        int ticksRemaining = getLifetime() - age;

        if (ticksRemaining <= 10) {
            setAlpha((float) ticksRemaining / 10.0F);
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            SporeCloudParticle particle = new SporeCloudParticle(level, x, y, z, xAux, yAux, zAux, sprites.get(level.getRandom()));

            particle.setAlpha(0.95F);
            return particle;
        }
    }
}
