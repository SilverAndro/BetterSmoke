package io.github.silverandro.bettersmoke

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.config.QuiltConfig
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.LoggerFactory

object BetterSmokeMain : ModInitializer {
    val logger = LoggerFactory.getLogger("BetterSmoke")

    val clientConfig = QuiltConfig.create("betterSmoke", "client_config")
    val serverConfig = QuiltConfig.create("betterSmoke", "server_config")

    override fun onInitialize(mod: ModContainer) {
        logger.info("Your smoke quality has been improved!")
    }
}
