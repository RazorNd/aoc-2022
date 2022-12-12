package day12

import java.io.File

fun main() {
    val heightmap = File("input/day12/input.txt").readMap()

    val startPositions = heightmap.positions.filter { heightmap[it] == 0 }.toList()

    val (steps) = PathFinder(heightmap).findPath(startPositions)

    println(steps)
}
