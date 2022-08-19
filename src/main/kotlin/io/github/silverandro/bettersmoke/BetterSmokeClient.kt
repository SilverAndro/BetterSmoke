package io.github.silverandro.bettersmoke

import net.minecraft.particle.ParticleTypes
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking

object BetterSmokeClient : ClientModInitializer {
    override fun onInitializeClient(mod: ModContainer?) {
        ClientPlayNetworking
            .registerGlobalReceiver(BetterSmokeMain.SMOKE_PACKET_CHANNEL) { minecraftClient,
                                                                            _,
                                                                            packetByteBuf,
                                                                            _ ->
                val isSignal = packetByteBuf.readBoolean()
                val pos = packetByteBuf.readVec3d()
                val vec = packetByteBuf.readVec3d()

                val defaultParticleType =
                    if (isSignal) ParticleTypes.CAMPFIRE_SIGNAL_SMOKE else ParticleTypes.CAMPFIRE_COSY_SMOKE

                minecraftClient.execute {
                    minecraftClient.world?.addImportantParticle(
                        defaultParticleType,
                        true,
                        pos.x,
                        pos.y,
                        pos.z,
                        vec.x,
                        vec.y,
                        vec.z
                    )
                }
            }
    }
}
