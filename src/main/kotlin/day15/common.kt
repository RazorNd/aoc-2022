package day15

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

internal data class Position(val x: Int, val y: Int) {

    infix fun distanceTo(other: Position): Int = (other.x - x).absoluteValue + (other.y - y).absoluteValue
}

internal fun MutableCollection<IntRange>.addIntersection(range: IntRange) {
    val intersected = filter { it isIntersectWith range }
    if (intersected.isEmpty()) {
        add(range)
    } else if (intersected.none { range in it }) {
        removeAll(intersected.toSet())
        add(intersected.fold(range, IntRange::merge))
    }
}

private infix fun IntRange.isIntersectWith(other: IntRange): Boolean =
    other.before in this || other.after in this || before in other || after in other

internal val IntRange.before get() = first - 1
internal val IntRange.after get() = last + 1

private fun IntRange.merge(other: IntRange): IntRange = min(first, other.first)..max(last, other.last)
internal fun Pair<Position, Position>.mapToRow(row: Int): IntRange? {
    val (sensor, beacon) = this
    val delta = (sensor distanceTo beacon) - (row - sensor.y).absoluteValue
    if (delta < 0) return null
    return (sensor.x - delta)..(sensor.x + delta)
}

internal fun File.readPositionsPair() = readLines()
    .map { it.split(": ", limit = 2) }
    .map { (sensor, beacon) ->
        val sensorPosition = sensor.substringAfter("Sensor at ").toPosition()
        val beaconPosition = beacon.substringAfter("closest beacon is at ").toPosition()

        sensorPosition to beaconPosition
    }

private fun String.toPosition(): Position = split(", ", limit = 2).let { (x, y) ->
    Position(
        x.substringAfter("x=").toInt(),
        y.substringAfter("y=").toInt()
    )
}

internal operator fun IntRange.contains(other: IntRange): Boolean = first <= other.first && other.last <= last
