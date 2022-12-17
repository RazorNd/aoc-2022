package day17

import java.io.File

private const val REPEAT = 1000000000000L

fun main() {
    val chamber = Chamber(File("input/day17/input.txt").readJets())

    val (from, size, heightDifference) = findCycle(chamber)

    val left = REPEAT - from
    val skippedDifference = (left / size) * heightDifference

    repeat((left % size).toInt()) { chamber.nextRock() }

    println(chamber.highestRockPosition + skippedDifference)
}


private data class Cycle(val from: Int, val size: Int, val heightDifference: Long)

private fun findCycle(chamber: Chamber): Cycle {
    data class Index(
        val jetsIndex: Int,
        val rockIndex: Int,
        val rocks: String
    )

    fun Chamber.toIndex(): Index = Index(jetsIndex, rockFactoryIndex, toString())

    val indexes = mutableMapOf<Index, Pair<Int, Long>>()

    repeat(100_000) { i ->
        chamber.nextRock()

        indexes.put(chamber.toIndex(), i to chamber.highestRockPosition)
            ?.let { (prevI, prevH) -> return Cycle(i + 1, i - prevI, chamber.highestRockPosition - prevH) }
    }

    throw IllegalStateException("Cycle not found")
}


