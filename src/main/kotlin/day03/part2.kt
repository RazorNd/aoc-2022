package day03

import java.io.File

fun main() {

    val sum = File("input/day03/input.txt").useLines { lines ->

        lines.map { indexBitSet(it) }
            .chunked(3)
            .map { (a, b, c) ->
                a.and(b)
                a.and(c)
                a
            }
            .map { it.nextSetBit(0) + 1 }
            .sum()
    }

    println(sum)

}
