package day11

import java.io.File

fun main() {
    val monkeys = File("input/day11/input.txt").readMonkeys()

    val simulation = Simulation(monkeys) { item -> item.worryLevel /= 3 }
    repeat(20) {
        simulation.round()
    }

    println(simulation.monkeyBusiness())
}


