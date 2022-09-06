package io.github.silverandro.bettersmoke

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object SmokeManager {
    // This leaks memory, but I cant think of an easy way to make it *not* without
    // Either using up a bunch of extra memory or causing lag spikes
    // With lots of campfires in the world (or being unloaded)
    // TODO: Maybe consider parallel processing for eventual correctness?
    private val keepers = mutableMapOf<RegistryKey<World>, MutableMap<BlockPos, SmokeKeeper>>()

    @JvmStatic
    fun lookupSmoke(world: World, pos: BlockPos): Vec3d {
        val keeperLookup = keepers.computeIfAbsent(world.registryKey) { mutableMapOf() }
        val keeper = keeperLookup.computeIfAbsent(pos) { SmokeKeeper() }
        return keeper.computeNewSmoke(world, pos)
    }

    private class SmokeKeeper {
        var current = 0.0
        var target = 0.0
        var currentDiv = 100.0
        var targetDiv = 100.0

        fun computeNewSmoke(world: World, pos: BlockPos): Vec3d {
            val targetPlayer = world
                .getClosestPlayer(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 4.2) {
                    it.y >= pos.y - 0.1
                }

            val dist = targetPlayer?.pos?.squaredDistanceTo(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

            target = if (targetPlayer == null) {
                0.0
            } else {
                atan2(targetPlayer.x - (pos.x + 0.5), targetPlayer.z - (pos.z + 0.5)).also {
                    if (angleDist(current, target) < 0.01 && currentDiv > 40) {
                        current = it
                    }
                }
            }

            targetDiv = if (dist != null) {
                val range = 1 - (sqrt(dist) / 4.2)
                (range * 10) + 10
            } else {
                100.0
            }

            currentDiv = currentDiv.lerp(targetDiv, 0.002)
            current = current.lerpAngle(target, 0.003)

            if (angleDist(current, target) < 0.01 && targetPlayer == null) {
                return Vec3d(0.0, 0.07, 0.0)
            }

            return Vec3d(
                sin(current) / currentDiv,
                0.07,
                cos(current) / currentDiv
            )
        }
    }
}
