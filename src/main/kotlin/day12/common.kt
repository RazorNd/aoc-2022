package day12

import java.io.File
import kotlin.math.sign

internal class PathFinder(private val map: Heightmap) {

    private val endPosition = map.endPosition
    fun findPath(startPositions: Collection<Position> = listOf(map.startPosition)): Path {

        val paths = ArrayDeque(startPositions.map { Path(it) }.sortedBy { it.estimatedDistance })
        val considered = startPositions.toHashSet()

        while (paths.isNotEmpty()) {
            val current = paths.removeFirst()

            if (current.lastPosition == endPosition) {
                return current
            }

            current.possibleWays()
                .filter { considered.add(it) }
                .map { current + it }
                .forEach { paths.add(it) }

            paths.sortBy { it.estimatedDistance }
        }

        throw IllegalStateException("Path doesn't exists")
    }

    private fun Path.possibleWays(): Collection<Position> {
        val currentHeight = map[lastPosition]

        val directions = listOf(
            Position(x = lastPosition.x + 1, y = lastPosition.y),
            Position(x = lastPosition.x - 1, y = lastPosition.y),
            Position(x = lastPosition.x, y = lastPosition.y + 1),
            Position(x = lastPosition.x, y = lastPosition.y - 1),
        )
        return directions
            .filter { it in map }
            .filter { map[it] - currentHeight <= 1 }
    }

    private val Path.estimatedDistance: Int get() = steps + (lastPosition distanceTo endPosition)


    private operator fun Path.plus(position: Position): Path = Path(steps + 1, positions + position)

    private operator fun Heightmap.contains(position: Position): Boolean =
        position.x in 0 until horizontalSize && position.y in 0 until verticalSize

    private infix fun Position.distanceTo(end: Position): Int = (end.x - x).sign + (end.y - y).sign

    class Heightmap(lines: List<String>) {
        val horizontalSize: Int
        val verticalSize: Int
        private val store: IntArray

        init {
            horizontalSize = lines.first().length
            verticalSize = lines.size

            store = lines.flatMap { line ->
                require(line.length == horizontalSize)

                line.map { c ->
                    when (c) {
                        'S' -> 0
                        'E' -> 'z' - 'a'
                        else -> c - 'a'
                    }
                }
            }.toIntArray()
        }

        val startPosition: Position = findPosition(lines, 'S')

        val endPosition: Position = findPosition(lines, 'E')

        private fun findPosition(lines: List<String>, c: Char): Position {
            val y = lines.indexOfFirst { c in it }
            val x = lines[y].indexOf(c)
            return Position(x, y)
        }

        val positions: Sequence<Position> = sequence {
            for (x in 0 until horizontalSize) {
                for (y in 0 until verticalSize) {
                    yield(Position(x, y))
                }
            }
        }

        operator fun get(position: Position): Int = store[position.y * horizontalSize + position.x]
    }

    data class Position(val x: Int, val y: Int)

    data class Path(val steps: Int, val positions: List<Position>) {

        val lastPosition: Position = positions.last()

        constructor(startPosition: Position) : this(0, listOf(startPosition))
    }
}

internal fun File.readMap(): PathFinder.Heightmap = PathFinder.Heightmap(readLines())
