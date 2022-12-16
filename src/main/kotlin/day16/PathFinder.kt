package day16

import kotlin.math.min

internal data class Tunnel(val valveRate: Int, val name: String, val directions: List<String>)
private typealias Direction = Pair<String, String>

internal class PathFinder(tunnels: Collection<Tunnel>) {

    private val distances = Distances(tunnels)
    private val noneBrokenValves = tunnels.filter { it.valveRate > 0 }
    fun findAllPaths(time: Int): MutableList<Path> {
        val paths = ArrayDeque(listOf(Path("AA", timeLeft = time)))

        val result = mutableListOf<Path>()

        while (paths.isNotEmpty()) {
            val current = paths.removeFirst()

            result.add(current)

            noneBrokenValves
                .filter { it.name !in current.openedValves }
                .mapNotNullTo(paths) {
                    current.moveTo(it)
                }
        }

        return result
    }

    private fun Path.moveTo(tunnel: Tunnel): Path? {
        val timeAfterOpen = timeLeft - distances[positions to tunnel.name] - TIME_TO_OPEN

        if (timeAfterOpen < 0) return null

        return copy(
            positions = tunnel.name,
            openedValves = openedValves + tunnel.name,
            timeLeft = timeAfterOpen,
            rate = rate + (timeAfterOpen * tunnel.valveRate)
        )
    }

    internal data class Path(
        val positions: String,
        val openedValves: Collection<String> = listOf(),
        val timeLeft: Int = 30,
        val rate: Int = 0
    )

    private class Distances(tunnels: Collection<Tunnel>) {

        private val distances: MutableMap<Direction, Int> = tunnels
            .flatMap { (_, name, directions) -> directions.map { name to it } }
            .associateWithTo(mutableMapOf()) { 1 }

        init {
            val valvesNames = distances.keys.flatMap { it.toList() }.distinct()
            val destinationMap = tunnels.associate { it.name to it.directions }
            tunnels.map { it.name }.forEach { from -> calcDistance(from, valvesNames, destinationMap) }
        }

        operator fun get(direction: Direction): Int {
            return distances[direction] ?: Int.MAX_VALUE
        }

        private fun calcDistance(from: String, valves: List<String>, destinationMap: Map<String, List<String>>) {
            val noneVisited = ArrayDeque(valves.filter { it != from })

            while (noneVisited.isNotEmpty()) {
                noneVisited.sortBy { this[from to it] }

                val current = noneVisited.removeFirst()

                val destinations = destinationMap[current] ?: listOf()

                destinations.map { it to 1 }
                    .forEach { (destination, distance) ->
                        distances[from to destination] =
                            min(distance + this[from to current], this[from to destination])
                    }
            }
        }
    }

    companion object {
        private const val TIME_TO_OPEN = 1
    }
}
