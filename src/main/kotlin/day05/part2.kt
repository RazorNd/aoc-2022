package day05

import java.io.File

fun main() {
    solution(File("input/day05/input.txt")) { move, stacks ->
        val moved = ArrayList<Char>(move.count)
        val from = stacks[move.fromIndex]
        repeat(move.count) { moved.add(from.removeLast()) }
        stacks[move.toIndex].addAll(moved.reversed())
    }
}
