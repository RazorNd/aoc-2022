package day01.day05

import java.io.File

data class Move(val count: Int, val from: Int, val to: Int) {
    val fromIndex = from - 1
    val toIndex = to - 1

}

fun parseMoves(lines: List<String>) = lines.map { parseMove(it) }
private const val FROM = " from "
private const val TO = " to "
fun parseMove(moveString: String): Move {
    val fromIndex = moveString.indexOf(FROM)
    val toIndex = moveString.indexOf(TO)

    val count = moveString.substring("move ".length, fromIndex).toInt()
    val from = moveString.substring(fromIndex + FROM.length, toIndex).toInt()
    val to = moveString.substring(toIndex + TO.length, moveString.length).toInt()

    return Move(count, from, to)
}

fun parseStacks(lines: List<String>): List<ArrayList<Char>> {
    val count = lines.last().chunked(4).map { it.trim() }.count()

    val stacks = List<ArrayList<Char>>(count) { ArrayList() }

    lines.subList(fromIndex = 0, toIndex = lines.lastIndex)
        .reversed()
        .map { line -> line.chunked(4).map { it.trim() } }
        .forEach { line ->
            line.forEachIndexed { index, value ->
                if (value.isNotEmpty()) {
                    stacks[index].add(value[1])
                }
            }
        }

    return stacks
}

internal fun solution(input: File, moveOperation: (Move, List<ArrayList<Char>>) -> Unit) {
    val lines = input.readLines()

    val delimiterIndex = lines.indexOf("")

    val stacks = parseStacks(lines.subList(0, delimiterIndex))

    parseMoves(lines.subList(delimiterIndex + 1, lines.size)).forEach { move -> moveOperation(move, stacks) }

    stacks.forEach { print(it.last()) }
}
