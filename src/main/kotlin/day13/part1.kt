package day13

import java.io.File

fun main() {
    val pairs = File("input/day13/input.txt").readPairs()

    val message = pairs.withIndex()
        .filter { (_, pair) -> IntListComparator.compare(pair.first, pair.second) < 0 }
        .sumOf { (i) -> i + 1 }

    println(message)
}

private fun File.readPairs(): List<Pair<Any, Any>> {
    return readLines().chunked(3) { lines ->
        lines.take(2).map { it.parse() }.let { (a, b) -> a to b }
    }
}

