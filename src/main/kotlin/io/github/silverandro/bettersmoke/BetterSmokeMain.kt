package io.github.silverandro.bettersmoke

import net.minecraft.util.Identifier
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.LoggerFactory

object BetterSmokeMain : ModInitializer {
    val SMOKE_PACKET_CHANNEL = Identifier("bettersmoke:custom_smoke")
    val logger = LoggerFactory.getLogger("BetterSmoke")

    override fun onInitialize(mod: ModContainer) {
        logger.info("Your smoke quality has been \"improved\"!")
    }
}
