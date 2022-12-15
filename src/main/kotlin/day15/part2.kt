package day15

import java.io.File

const val MAX_POSITION = 4000000

fun main() {
    val nearbyRange = 0..MAX_POSITION
    val positionsPair = File("input/day15/input.txt").readPositionsPair()
    val rows = nearbyRange.map { row ->
        positionsPair.mapNotNull { it.mapToRow(row) }
            .fold(mutableListOf<IntRange>()) { acc, range -> acc.addIntersection(range); acc }
    }
    val position = rows.withIndex()
        .filter { (_, row) -> row.any { nearbyRange !in it } }
        .map { (y, row) -> Position(row.passes().single(), y) }
        .single()
    println(position.tuningFrequency)
}

private val Position.tuningFrequency: Long get() = x.toLong() * 4000000 + y

private fun Collection<IntRange>.passes() = windowed(2).flatMap { (p, n) -> p.after..n.before }
