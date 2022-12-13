package day13

import java.io.File

fun main() {
    val dividerPackets = listOf(listOf(listOf(2)), listOf(listOf(6)))

    val input = File("input/day13/input.txt")
        .readLines()
        .filter { it.isNotEmpty() }
        .map { it.parse() } + dividerPackets

    val result = input.sortedWith(IntListComparator).let { sorted ->
        (sorted.indexOf(dividerPackets[0]) + 1) * (sorted.indexOf(dividerPackets[1]) + 1)
    }

    println(result)
}
