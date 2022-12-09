package day09

import java.io.File

fun main() {
    val file = File("input/day09/input.txt")

    val simulation = Simulation(size = 10)
    file.readMoves().forEach { (move, step) -> simulation.moveHead(move, step) }

    println(simulation.tailVisited.size)
}
