package day17

import java.io.File
import kotlin.math.max

internal enum class Movement {
    LEFT, RIGHT, DOWN
}

internal class Jets(private val movements: List<Movement>) {

    private var i = 0
    val index get() = i
    fun next(): Movement = movements[i].also { i = (i + 1) % movements.size }
}

internal class Chamber(private val jets: Jets) {

    private val horizontalBorder: IntRange = 0 until 7

    private val rockFactory: RockFactory = RockFactory()

    private val rocks = mutableListOf<RockPart>()

    private var highestInColumn = LongArray(7) { -1 }
    private var minimal = -1L
    private var hight = -1L
    val highestRockPosition: Long get() = hight + 1

    val rockFactoryIndex get() = rockFactory.index
    val jetsIndex get() = jets.index

    fun nextRock() {
        val rock = rockFactory.createNextRock(leftEdge = 2, bottomEdge = highestRockPosition + 3)

        var rest = false

        while (!rest) {
            rock.tryMove(jets.next())
            rest = !rock.tryMove(Movement.DOWN)
        }

        updateHighest(rock.parts)

        rocks.removeIf { it.y < minimal - 10 }

        rocks.addAll(rock.parts)
    }

    private fun updateHighest(parts: List<RockPart>) {
        parts.forEach {
            highestInColumn[it.x] = max(it.y, highestInColumn[it.x])
        }
        hight = highestInColumn.max()
        minimal = highestInColumn.min()
    }

    private fun Rock.tryMove(movement: Movement): Boolean = tryMove(movement) {
        it.x in horizontalBorder && it.y > minimal && it !in rocks
    }

    internal data class RockPart(val x: Int, val y: Long) {
        operator fun plus(movement: Movement): RockPart = when (movement) {
            Movement.RIGHT -> copy(x = x + 1)
            Movement.LEFT -> copy(x = x - 1)
            Movement.DOWN -> copy(y = y - 1)
        }
    }


    private class Rock(var parts: List<RockPart>) {

        fun tryMove(movement: Movement, predicate: (RockPart) -> Boolean): Boolean {
            val newPositions = partsAfterMove(movement)

            val success = newPositions.all(predicate)

            if (success) {
                parts = newPositions
            }

            return success
        }

        private fun partsAfterMove(movement: Movement): List<RockPart> = parts.map { it + movement }
    }

    private class RockFactory {

        private var i = 0
        val index get() = i

        fun createNextRock(leftEdge: Int, bottomEdge: Long): Rock = Rock(
            when (i) {
                0 -> createHorizontalLine(leftEdge, bottomEdge)
                1 -> createCross(leftEdge, bottomEdge)
                2 -> createLShape(leftEdge, bottomEdge)
                3 -> createVerticalLine(leftEdge, bottomEdge)
                4 -> createSquare(leftEdge, bottomEdge)
                else -> throw IllegalStateException()
            }
        ).also { i = (i + 1) % SIZE }

        private fun createHorizontalLine(leftEdge: Int, bottomEdge: Long): List<RockPart> = listOf(
            RockPart(leftEdge, bottomEdge),
            RockPart(leftEdge + 1, bottomEdge),
            RockPart(leftEdge + 2, bottomEdge),
            RockPart(leftEdge + 3, bottomEdge),
        )

        private fun createCross(leftEdge: Int, bottomEdge: Long): List<RockPart> = listOf(
            RockPart(leftEdge + 1, bottomEdge + 0),
            RockPart(leftEdge + 1, bottomEdge + 1),
            RockPart(leftEdge + 1, bottomEdge + 2),

            RockPart(leftEdge + 0, bottomEdge + 1),
            RockPart(leftEdge + 2, bottomEdge + 1),
        )

        private fun createLShape(leftEdge: Int, bottomEdge: Long): List<RockPart> = listOf(
            RockPart(leftEdge + 0, bottomEdge + 0),
            RockPart(leftEdge + 1, bottomEdge + 0),
            RockPart(leftEdge + 2, bottomEdge + 0),
            RockPart(leftEdge + 2, bottomEdge + 1),
            RockPart(leftEdge + 2, bottomEdge + 2),
        )

        private fun createVerticalLine(leftEdge: Int, bottomEdge: Long): List<RockPart> = listOf(
            RockPart(leftEdge, bottomEdge + 0),
            RockPart(leftEdge, bottomEdge + 1),
            RockPart(leftEdge, bottomEdge + 2),
            RockPart(leftEdge, bottomEdge + 3),
        )

        private fun createSquare(leftEdge: Int, bottomEdge: Long): List<RockPart> = listOf(
            RockPart(leftEdge + 0, bottomEdge + 0),
            RockPart(leftEdge + 1, bottomEdge + 0),
            RockPart(leftEdge + 0, bottomEdge + 1),
            RockPart(leftEdge + 1, bottomEdge + 1),
        )

        companion object {
            private const val SIZE = 5
        }
    }

    override fun toString(): String = buildString {
        val max = highestRockPosition
        (max downTo minimal).forEach { y ->
            append('|')
            horizontalBorder.forEach { x ->
                append(
                    when (RockPart(x, y)) {
                        in rocks -> '#'
                        else -> '.'
                    }
                )
            }
            append('|')
            appendLine()
        }
        if (minimal == -1L) {
            append("+-------+")
        }
    }

}

internal fun File.readJets(): Jets = Jets(readLines().single().map { it.toMovement() })
private fun Char.toMovement(): Movement = when (this) {
    '>' -> Movement.RIGHT
    '<' -> Movement.LEFT
    else -> throw IllegalStateException("Unsupported movement: $this")
}
