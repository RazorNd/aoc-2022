package day14

import java.io.File

fun main() {
    val caveScan = File("input/day14/input.txt").readCaveScan()

    val border = caveScan.endlessBorder()
    val simulation = Simulation(caveScan) { it.y > border }

    println(simulation.simulate())
}
