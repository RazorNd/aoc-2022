package day20

import java.io.File

fun main() {
    val input = File("input/day20/input.txt").readLines().map { it.toLong() * 811589153 }

    val sorted = input.toOrdered().apply { repeat(10) { mix() } }.sortedByOrder()
    val zeroPosition = sorted.indexOf(0)

    println(listOf(1_000, 2_000, 3_000).map { (zeroPosition + it) % sorted.size }.sumOf { sorted[it] })
}
