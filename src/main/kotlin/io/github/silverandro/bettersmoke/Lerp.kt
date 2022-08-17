package io.github.silverandro.bettersmoke

import kotlin.math.PI

fun angleDist(a: Double, b: Double): Double {
    val max = PI * 2
    val delta = (b - a) % max
    return 2 * delta % max - delta
}

fun Double.lerpAngle(other: Double, t: Double): Double {
    return this + angleDist(this, other) * t
}

fun Double.lerp(other: Double, t: Double): Double {
    return this + (other - this) * t
}
