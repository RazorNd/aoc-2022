package day02

import java.io.File

fun main() {
    val score = File("input/day02/input.txt").useLines { lines ->
        lines.map { it.parseGuide() }
            .map { (opponent, you) -> calculateScore(opponent, you) }
            .sum()
    }

    println(score)
}

private fun String.parseGuide(): Pair<Shape, Shape> {
    val (opponent, you) = this.split(" ")

    return opponentChose(opponent) to youShapeChose(you)
}

private fun youShapeChose(value: String): Shape = when (value) {
    "X" -> Shape.ROCK
    "Y" -> Shape.PAPER
    "Z" -> Shape.SCISSORS
    else -> throw IllegalArgumentException("Wrong opponent chose: $value")
}
