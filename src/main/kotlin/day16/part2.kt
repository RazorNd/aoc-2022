package day16

import java.io.File


fun main() {
    val tunnels = File("input/day16/input.txt").parseTunnels()

    println(findMaxRate(tunnels))
}

private fun <T> List<T>.permutations(): Sequence<Pair<T, T>> = asSequence()
    .flatMapIndexed { i, v -> drop(i + 1).map { v to it } }

private fun findMaxRate(tunnels: List<Tunnel>): Int {
    val (me, elephant) = PathFinder(tunnels).findAllPaths(26)
        .sortedByDescending { it.rate }
        .permutations()
        .first { (a, b) -> a.openedValves.none { it in b.openedValves } }

    return me.rate + elephant.rate
}
