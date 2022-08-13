import kotlin.math.PI

fun angleDist(a: Double, b: Double): Double {
    val max = PI * 2
    val delta = (b - a) % max
    return 2 * delta % max - delta
}

fun Double.lerp(other: Double, t: Double): Double {
    return this + angleDist(this, other) * t
}
