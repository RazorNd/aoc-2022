package day06

import java.io.File

fun main() {
    val position = packetPosition(File("input/day06/input.txt").readText(), 14)

    println(position)
}
