package day09

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

internal enum class Move {
    RIGHT,
    LEFT,
    UP,
    DOWN
}

internal data class Position(val x: Int, val y: Int)
internal data class Distance(val x: Int, val y: Int)

internal operator fun Position.minus(other: Position): Distance = Distance(x - other.x, y - other.y)
internal operator fun Position.plus(distance: Distance): Position = Position(x + distance.x, y + distance.y)
internal fun String.toMove(): Move = when(this) {
    "R" -> Move.RIGHT
    "L" -> Move.LEFT
    "U" -> Move.UP
    "D" -> Move.DOWN
    else -> throw IllegalArgumentException(this)
}

internal class Simulation(startPosition: Position = Position(0, 0), size: Int = 2) {
    private var parts = MutableList(size) { startPosition }
    val tailVisited = hashSetOf(startPosition)

    fun moveHead(move: Move) {
        parts[0] = parts[0] + move
        for (i in 1 until parts.size) {
            parts[i] = parts[i].moveTo(parts[i - 1])
        }
        tailVisited.add(parts.last())
    }

    fun moveHead(move: Move, step: Int) {
        repeat(step) { moveHead(move) }
    }

    private operator fun Position.plus(move: Move): Position = when (move) {
        Move.RIGHT -> copy(x = x + 1)
        Move.LEFT -> copy(x = x - 1)
        Move.UP -> copy(y = y + 1)
        Move.DOWN -> copy(y = y - 1)
    }

    private fun Position.moveTo(destination: Position): Position =
        (destination - this).takeIf { it.length > 1 }?.let { this + it.normal } ?: this

    private val Distance.length get() = max(x.absoluteValue, y.absoluteValue)
    private val Distance.normal get() = copy(x = x.sign, y = y.sign)
}

internal fun File.readMoves(): List<Pair<Move, Int>> =
    readLines().map { it.split(" ").let { (move, step) -> move.toMove() to step.toInt() } }
