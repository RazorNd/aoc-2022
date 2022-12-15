package day15

import java.io.File


fun main() {
    val positions = File("input/day15/input.txt").readPositionsPair()

    val row = 2000000
    val rowPositions = mutableSetOf<IntRange>()
    positions.mapNotNull { it.mapToRow(row) }.forEach { rowPositions.addIntersection(it) }

    val beaconInRow = positions.map { it.second }.filter { it.y == row }.distinct().count()

    println(rowPositions.sumOf { it.count() } - beaconInRow)
}
