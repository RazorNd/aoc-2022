package day18

import java.io.File

internal enum class Surface {
    TOP, BOTTOM, LEFT, RIGHT, FRONT, BACK;

    companion object {
        fun diagonals(): Set<Pair<Surface, Surface>> = setOf(
            TOP to LEFT, TOP to RIGHT, TOP to FRONT, TOP to BACK,
            BOTTOM to LEFT, BOTTOM to RIGHT, BOTTOM to FRONT, BOTTOM to BACK,

            LEFT to TOP, LEFT to BOTTOM, LEFT to FRONT, LEFT to BACK,
            RIGHT to TOP, RIGHT to BOTTOM, RIGHT to FRONT, RIGHT to BACK,

            FRONT to LEFT, FRONT to RIGHT, FRONT to TOP, FRONT to BOTTOM,
            BACK to LEFT, BACK to RIGHT, BACK to TOP, BACK to BOTTOM,
        )
    }
}

internal operator fun Coordinate.plus(diagonal: Pair<Surface, Surface>): Coordinate =
    this + diagonal.first + diagonal.second

internal data class Coordinate(val x: Int, val y: Int, val z: Int) : Comparable<Coordinate> {
    operator fun plus(surface: Surface): Coordinate = when (surface) {
        Surface.LEFT -> copy(x = x - 1)
        Surface.TOP -> copy(y = y - 1)
        Surface.FRONT -> copy(z = z - 1)

        Surface.RIGHT -> copy(x = x + 1)
        Surface.BOTTOM -> copy(y = y + 1)
        Surface.BACK -> copy(z = z + 1)
    }

    override fun compareTo(other: Coordinate): Int = when {
        x != other.x -> x - other.x
        y != other.y -> y - other.y
        else -> z - other.z
    }
}


internal fun File.readCoordinates(): List<Coordinate> = useLines { lines -> lines.map { it.toCoordinate() }.toList() }

private fun String.toCoordinate(): Coordinate {
    val (x, y, z) = split(",").map { it.toInt() }
    return Coordinate(x, y, z)
}
