package day04

import java.io.File

fun main() {
    val count = File("input/day04/input.txt")
        .readLines()
        .map { parsePair(it) }
        .count { (a, b) -> a.overlap(b) || b.overlap(a) }

    println(count)
}

private fun IntRange.overlap(other: IntRange): Boolean =
    (first >= other.first && first <= other.last) || (last <= other.last && last >= other.first)
