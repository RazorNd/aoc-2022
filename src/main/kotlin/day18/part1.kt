package day18

import java.io.File

fun main() {
    val file = File("input/day18/input.txt")

    val coordinates = file.readCoordinates().toSet()

    val sum = coordinates.sumOf { coordinate ->
        Surface.values()
            .map { coordinate + it }
            .count { it !in coordinates }
    }

    println(sum)
}

