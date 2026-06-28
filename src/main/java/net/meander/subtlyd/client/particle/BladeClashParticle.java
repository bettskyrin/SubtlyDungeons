package net.meander.subtlyd.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class BladeClashParticle extends CritParticle {
    protected BladeClashParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite textureAtlasSprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, textureAtlasSprite);

        rCol = 0.98F;
        gCol = 0.83F;
        bCol = 0.01F;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprite) {
            sprites = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            BladeClashParticle particle = new BladeClashParticle(level, x, y, z, xAux, yAux, zAux, sprites.get(level.getRandom()));

            particle.setSpriteFromAge(sprites);
            return particle;
        }
    }
}