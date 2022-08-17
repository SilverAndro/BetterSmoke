package io.github.silverandro.bettersmoke.mixin;

import net.minecraft.block.CampfireBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CampfireBlock.class)
public class MixinDisableClientCampfireSmoke {
	/**
	 * @author Silver
	 * @reason Removing this feature entirely lets go
	 */
	@Overwrite
	public static void spawnSmokeParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke) {
		RandomGenerator randomGenerator = world.getRandom();
		if (lotsOfSmoke) {
			world.addParticle(
					ParticleTypes.SMOKE,
					(double)pos.getX() + 0.5 + randomGenerator.nextDouble() / 4.0 * (double)(randomGenerator.nextBoolean() ? 1 : -1),
					(double)pos.getY() + 0.4,
					(double)pos.getZ() + 0.5 + randomGenerator.nextDouble() / 4.0 * (double)(randomGenerator.nextBoolean() ? 1 : -1),
					0.0,
					0.005,
					0.0
			);
		}
	}
}
