package day03

import java.io.File

fun main() {

    val priority = File("input/day03/input.txt").useLines { lines ->
        lines.map {
            indexBitSet(
                it.substring(0, it.length / 2)
            ).apply { this.and(indexBitSet(it.substring(it.length / 2))) }
        }
            .map { it.nextSetBit(0) + 1 }
            .sum()
    }

    println(priority)
}
