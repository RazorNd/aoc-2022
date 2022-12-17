package day17

import java.io.File

fun main() {
    val jets = File("input/day17/input.txt").readJets()

    val chamber = Chamber(jets)

    repeat(2022) {
        chamber.nextRock()
    }
    println(chamber.highestRockPosition)
}

