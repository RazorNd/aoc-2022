package day14

import day14.CaveScan.Tile
import java.io.File
import kotlin.math.max
import kotlin.math.min

internal data class Position(val x: Int, val y: Int) {
    operator fun rangeTo(end: Position): PositionRange = PositionRange(this, end)

    class PositionRange(private val start: Position, private val end: Position) : Iterable<Position> {
        init {
            require(start.x == end.x || start.y == end.y) { "Position range must be line" }
        }

        override fun iterator() = when {
            start.y == end.y -> iterateHorizontal()
            start.x == end.x -> iterateVertically()
            else -> throw IllegalStateException()
        }

        private fun iterateHorizontal(): Iterator<Position> =
            (min(start.x, end.x)..max(start.x, end.x)).map { Position(it, start.y) }.iterator()

        private fun iterateVertically(): Iterator<Position> =
            (min(start.y, end.y)..max(start.y, end.y)).map { Position(start.x, it) }.iterator()

    }
}

internal interface CaveScan {
    enum class Tile {
        AIR, ROCK, SAND
    }

    val sandCount: Int

    fun addSand(position: Position)

    fun toMap(): Map<Position, Tile>

    operator fun get(x: Int, y: Int): Tile

    operator fun get(position: Position): Tile = this[position.x, position.y]
}

private class DefaultCaveScan : CaveScan {

    private val scan = mutableMapOf<Position, Tile>()

    override val sandCount: Int get() = scan.values.count { it == Tile.SAND }

    fun addRock(position: Position) {
        scan[position] = Tile.ROCK
    }

    override fun addSand(position: Position) {
        scan[position] = Tile.SAND
    }

    override fun toMap() = scan

    override fun get(x: Int, y: Int): Tile = this[Position(x, y)]

    override fun get(position: Position): Tile = scan.getOrDefault(position, Tile.AIR)
}

internal fun File.readCaveScan(): CaveScan = useLines { lines -> lines.toCaveScan() }
private fun Sequence<String>.toCaveScan(): DefaultCaveScan = DefaultCaveScan().apply {
    val paths = flatMap { it.toRockPath() }
    paths.forEach { addRock(it) }
}

private fun String.toRockPath() = split(" -> ").map { it.toPosition() }.windowed(2).flatMap { (from, to) -> from..to }
private fun String.toPosition(): Position = split(",").map { it.toInt() }.let { (x, y) -> Position(x, y) }
internal fun CaveScan.endlessBorder(): Int =
    toMap().entries.filter { (_, t) -> t == Tile.ROCK }.maxOf { (p) -> p.y }
