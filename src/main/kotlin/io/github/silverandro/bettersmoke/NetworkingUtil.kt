package io.github.silverandro.bettersmoke

import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.Vec3d

fun PacketByteBuf.writeVec3d(vec3d: Vec3d) {
    writeDouble(vec3d.x)
    writeDouble(vec3d.y)
    writeDouble(vec3d.z)
}

fun PacketByteBuf.readVec3d(): Vec3d {
    return Vec3d(
        readDouble(),
        readDouble(),
        readDouble()
    )
}
