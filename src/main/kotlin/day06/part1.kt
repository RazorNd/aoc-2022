package day06

import java.io.File

fun main() {
    val position = packetPosition(File("input/day06/input.txt").readText(), 4)

    println(position)
}
