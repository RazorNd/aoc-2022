package day08

import java.io.File

fun main() {
    val grid = File("input/day08/input.txt").readGrid()

    println(grid.treePositionSequence().maxOf { scenicScore(it, grid) })
}

private fun scenicScore(tree: Pair<GridIndex, Int>, grid: List<List<Int>>): Int {
    val (index, high) = tree

    val predicate: (Int) -> Boolean = { it < high }

    val left = grid.leftTree(index).toList().reversed().countWhileInclusive(predicate)
    val right = grid.rightTree(index).countWhileInclusive(predicate)
    val top = grid.topTree(index).toList().reversed().countWhileInclusive(predicate)
    val bottom = grid.bottomTree(index).countWhileInclusive(predicate)

    return left * right * top * bottom
}

private fun <E> Iterable<E>.countWhileInclusive(predicate: (E) -> Boolean): Int =
    asSequence().countWhileInclusive(predicate)

private fun <E> Sequence<E>.countWhileInclusive(predicate: (E) -> Boolean): Int {
    var counter = 0
    forEach {
        counter++
        if (!predicate(it)) {
            return counter
        }
    }
    return counter
}
