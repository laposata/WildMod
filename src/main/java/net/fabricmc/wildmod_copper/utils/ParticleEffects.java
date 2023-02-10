package net.fabricmc.wildmod_copper.utils;

import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class ParticleEffects {
    public static final Vec3f SMOKE_GRAY = new Vec3f(Vec3d.unpackRgb(0x6f5d59));
    public static final ParticleEffect SMOKE_EFFECT = new DustParticleEffect(SMOKE_GRAY, 2);
    public static final ParticleEffect REDSTONE_EFFECT = new DustParticleEffect(DustParticleEffect.RED, 1);
}
