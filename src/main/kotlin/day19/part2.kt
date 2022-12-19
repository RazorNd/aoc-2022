package day19

import java.io.File

fun main() {
    val geocodeCounts = File("input/day19/input.txt").readBluePrints().take(3).map { blueprint ->
        Decision(timeLeft = 32).findMaxGeode(blueprint)
    }

    println(geocodeCounts.reduce { a, b -> a * b })
}
