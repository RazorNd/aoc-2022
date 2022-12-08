package day08

import java.io.File

fun main() {
    val grid = File("input/day08/input.txt").readGrid()

    val visibleInnerTree = grid.treePositionSequence().count { visible(it, grid) }

    println(visibleInnerTree)
}

private fun visible(tree: Pair<GridIndex, Int>, grid: List<List<Int>>): Boolean {
    val (index, treeHigh) = tree

    val left = grid.leftTree(index).all { it < treeHigh }
    val right = grid.rightTree(index).all { it < treeHigh }
    val top = grid.topTree(index).all { it < treeHigh }
    val bottom = grid.bottomTree(index).all { it < treeHigh }

    return left || right || top || bottom
}
