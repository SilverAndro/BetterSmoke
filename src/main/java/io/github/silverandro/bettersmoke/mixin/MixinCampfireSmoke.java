package io.github.silverandro.bettersmoke.mixin;

import io.github.silverandro.bettersmoke.SmokeManager;
import net.minecraft.block.CampfireBlock;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CampfireBlock.class)
public class MixinCampfireSmoke {
	@SuppressWarnings("OverwriteAuthorRequired")
	@Overwrite
	public static void spawnSmokeParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke) {
		RandomGenerator randomGenerator = world.getRandom();
		DefaultParticleType defaultParticleType = isSignal ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
		Vec3d power = SmokeManager.lookupSmoke(world, pos, lotsOfSmoke);
		world.addImportantParticle(
				defaultParticleType,
				true,
				(double)pos.getX() + 0.5 + randomGenerator.nextDouble() / 3.0 * (double)(randomGenerator.nextBoolean() ? 1 : -1),
				(double)pos.getY() + randomGenerator.nextDouble() + randomGenerator.nextDouble(),
				(double)pos.getZ() + 0.5 + randomGenerator.nextDouble() / 3.0 * (double)(randomGenerator.nextBoolean() ? 1 : -1),
				power.x,
				power.y,
				power.z
		);
		if (lotsOfSmoke) {
			world.addParticle(
					ParticleTypes.SMOKE,
					(double)pos.getX() + 0.5 + randomGenerator.nextDouble() / 4.0 * (double)(randomGenerator.nextBoolean() ? 1 : -1),
					(double)pos.getY() + 0.4,
					(double)pos.getZ() + 0.5 + randomGenerator.nextDouble() / 4.0 * (double)(randomGenerator.nextBoolean() ? 1 : -1),
					power.x,
					power.y,
					power.z
			);
		}
	}
}
