package day01.day05

import java.io.File

fun main() {
    solution(File("input/day05/input.txt")) { move, stacks -> move(stacks) }
}

operator fun Move.invoke(stacks: List<ArrayList<Char>>) {
    repeat(count) {
        val c = stacks[fromIndex].removeLast()
        stacks[toIndex].add(c)
    }
}
