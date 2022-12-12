package day12

import java.io.File


fun main() {
    val map = File("input/day12/input.txt").readMap()

    val path = PathFinder(map).findPath()

    println(path.steps)
}

