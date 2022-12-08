package day08

import java.io.File

internal data class GridIndex(val row: Int, val column: Int)

internal val <E> List<List<E>>.columnSize get() = first().size
internal val <E> List<List<E>>.rowSize get() = size
internal fun <E> List<List<E>>.column(column: Int): Sequence<E> =
    (0 until columnSize).asSequence().map { this[it][column] }

internal fun <E> List<List<E>>.row(row: Int): Sequence<E> = this[row].asSequence()
internal fun List<List<Int>>.treePositionSequence(): Sequence<Pair<GridIndex, Int>> {
    val grid = this

    return sequence {
        (0 until grid.rowSize).forEach { row ->
            (0 until grid.columnSize).forEach { column ->
                yield(GridIndex(row, column) to grid[row][column])
            }
        }
    }
}

internal fun File.readGrid(): List<List<Int>> = readLines().map { line -> line.map { it.digitToInt() } }
internal fun List<List<Int>>.leftTree(index: GridIndex) = row(index.row).take(index.column)
internal fun List<List<Int>>.rightTree(index: GridIndex) = row(index.row).drop(index.column + 1)
internal fun List<List<Int>>.topTree(index: GridIndex) = column(index.column).take(index.row)
internal fun List<List<Int>>.bottomTree(index: GridIndex) = column(index.column).drop(index.row + 1)
