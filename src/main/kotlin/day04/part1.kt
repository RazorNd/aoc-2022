package day04

import java.io.File

fun main() {
    val count = File("input/day04/input.txt")
        .readLines()
        .map { line -> parsePair(line) }
        .count { (a, b) -> a in b || b in a }

    println(count)
}

private operator fun IntRange.contains(other: IntRange): Boolean = first <= other.first && last >= other.last
