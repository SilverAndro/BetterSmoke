package io.github.silverandro.bettersmoke

import angleDist
import lerp
import net.minecraft.util.math.*
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World
import org.quiltmc.qsl.networking.api.PlayerLookup
import kotlin.math.*

object SmokeManager {
    private val keepers = mutableMapOf<RegistryKey<World>, MutableMap<BlockPos, SmokeKeeper>>()

    @JvmStatic
    fun lookupSmoke(world: World, pos: BlockPos, tooMuchSmoke: Boolean): Vec3d {
        val keeperLookup = keepers.computeIfAbsent(world.registryKey) { mutableMapOf() }
        val keeper = keeperLookup.computeIfAbsent(pos) { SmokeKeeper() }
        return keeper.computeNewSmoke(world, pos, tooMuchSmoke)
    }

    private class SmokeKeeper {
        var current = 0.0
        var target = 0.0

        fun computeNewSmoke(world: World, pos: BlockPos, tooMuchSmoke: Boolean): Vec3d {
            val targetPlayer = world
                .getClosestPlayer(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 6.5) {
                    it.y >= pos.y - 0.1
                }

            target = if (targetPlayer == null) {
                0.0
            } else {
                atan2(targetPlayer.x - (pos.x + 0.5), targetPlayer.z - (pos.z + 0.5))
            }

            current = current.lerp(target, 0.06)

            BetterSmokeMain.logger.info(targetPlayer?.entityName)
            BetterSmokeMain.logger.info(target.toString())
            BetterSmokeMain.logger.info(current.toString())

            if (angleDist(current, target) < 0.01 && targetPlayer == null) {
                return if (tooMuchSmoke) HIGH_SMOKE else LOW_SMOKE
            }

            return Vec3d(
                sin(current) / 10,
                if (tooMuchSmoke) HIGH_SMOKE.y else LOW_SMOKE.y,
                cos(current) / 10
            )
        }

        companion object {
            val LOW_SMOKE = Vec3d(0.0, 0.07, 0.0)
            val HIGH_SMOKE = Vec3d(0.0, 0.005, 0.0)
        }
    }
}
