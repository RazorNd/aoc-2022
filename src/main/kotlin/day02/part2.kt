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

    val opponentChose = opponentChose(opponent)
    return opponentChose to youShapeChose(opponentChose, you)
}

private fun youShapeChose(opponent: Shape, value: String): Shape = when (value) {
    "X" -> opponent.defeatShape()
    "Y" -> opponent
    "Z" -> opponent.loseShape()
    else -> throw IllegalArgumentException()
}
