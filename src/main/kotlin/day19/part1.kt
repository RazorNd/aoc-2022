package day19

import java.io.File

fun main() {
    val qualityLevel = File("input/day19/input.txt").readBluePrints().sumOf { blueprint ->
        Decision().findMaxGeode(blueprint) * blueprint.number
    }

    println(qualityLevel)
}

