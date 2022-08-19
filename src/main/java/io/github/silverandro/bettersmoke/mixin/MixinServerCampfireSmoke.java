package io.github.silverandro.bettersmoke.mixin;

import io.github.silverandro.bettersmoke.BetterSmokeMain;
import io.github.silverandro.bettersmoke.NetworkingUtilKt;
import io.github.silverandro.bettersmoke.SmokeManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public class MixinServerCampfireSmoke {
	@Inject(method = "litServerTick", at = @At("HEAD"))
	private static void betterSmoke$spawnCustomServerSideCampfireSmoke(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
		RandomGenerator randomGenerator = world.getRandom();
		if (world instanceof ServerWorld sw && randomGenerator.nextInt(10) <= 3) {
			boolean isSignal = sw.getBlockState(pos).get(CampfireBlock.SIGNAL_FIRE);

			Vec3d smokeVector = SmokeManager.lookupSmoke(sw, pos);

			PlayerLookup.tracking(sw, pos).forEach(serverPlayerEntity -> {
				if (ServerPlayNetworking.canSend(serverPlayerEntity, BetterSmokeMain.INSTANCE.getSMOKE_PACKET_CHANNEL())) {
					PacketByteBuf buf = PacketByteBufs.create();

					buf.writeBoolean(isSignal);
					NetworkingUtilKt.writeVec3d(buf,
						new Vec3d(
							(double) pos.getX() + 0.5 + randomGenerator.nextDouble() / 3.0 * (double) (randomGenerator.nextBoolean() ? 1 : -1),
							(double) pos.getY() + randomGenerator.nextDouble() + randomGenerator.nextDouble(),
							(double) pos.getZ() + 0.5 + randomGenerator.nextDouble() / 3.0 * (double) (randomGenerator.nextBoolean() ? 1 : -1)
						)
					);
					NetworkingUtilKt.writeVec3d(buf, smokeVector);

					ServerPlayNetworking.send(
						serverPlayerEntity,
						BetterSmokeMain.INSTANCE.getSMOKE_PACKET_CHANNEL(),
						buf
					);
				}
			});
		}
	}
}
